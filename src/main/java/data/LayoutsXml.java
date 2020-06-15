package data;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.representation.ThisApplication;
import com.representation.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import layouteditor.DataBlock;
import layouts.DataLayout;

public class LayoutsXml {
    private static String FILENAME = "layouts.xml";
    public static void saveLayouts(Context ctx, ArrayList<DataLayout> list) throws IOException {
        FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        Log.println(Log.INFO, "LayoutsXml", "Layouts file saved at " + ctx.getFilesDir());
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(fos, "UTF-8");
        serializer.startDocument(null, Boolean.TRUE);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        serializer.startTag(null, "layouts");
        for (DataLayout layout: list) {
            serializer.startTag(null, "layout");
            serializer.startTag(null, "layoutTitle");
            if (layout.getLayoutTitle() != null)
                serializer.text(layout.getLayoutTitle());
            serializer.endTag(null, "layoutTitle");

            serializer.startTag(null, "selected");
            serializer.text(String.valueOf(layout.isSelected()));
            serializer.endTag(null, "selected");

            serializer.startTag(null, "default");
            serializer.text(String.valueOf(layout.isDefaultChoice()));
            serializer.endTag(null, "default");

            serializer.startTag(null, "quickMenu");
            serializer.text(String.valueOf(layout.isQuickMenuElement()));
            serializer.endTag(null, "quickMenu");

            List<DataBlock> blocks = layout.getDataBlocks();
            if(blocks.size()>0)
                serializer.startTag(null, "blocks");
            for (DataBlock block: blocks) {
                serializer.startTag(null, "block");
                serializer.startTag(null, "blockTitle");
                if (block.getBlockTitle() != null)
                    serializer.text(block.getBlockTitle());
                serializer.endTag(null, "blockTitle");

                serializer.startTag(null, "type");
                serializer.text(block.getBlockType().name());
                serializer.endTag(null, "type");

                serializer.startTag(null, "magnitude");
                serializer.text(block.getMagnitude().name());
                serializer.endTag(null, "magnitude");

                serializer.startTag(null, "unit");
                serializer.text(block.getUnit().name());
                serializer.endTag(null, "unit");

                if(block.getBlockType() != Utils.BlockTypeEnum.VALUE) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.DATETIME_FORMAT, Locale.getDefault());
                    serializer.startTag(null, "dateStart");
                    serializer.text(simpleDateFormat.format(block.getDateStart()));
                    serializer.endTag(null, "dateStart");

                    serializer.startTag(null, "dateEnd");
                    serializer.text(simpleDateFormat.format(block.getDateStart()));
                    serializer.endTag(null, "dateEnd");
                }

                serializer.endTag(null, "block");
            }

            if(blocks.size()>0)
                serializer.endTag(null, "blocks");
            serializer.endTag(null, "layout");
        }

        serializer.endDocument();
        serializer.flush();

        fos.close();
    }

    public static void replaceLayout(Context ctx, DataLayout layout, int position){
        try {
            ArrayList<DataLayout> list = readData(ctx);
            if(position > -1) {
                list.remove(position);
            }
            list.add(layout);
            saveLayouts(ctx, list);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public static void deselectAllLayouts(Context ctx){
        // Deselect all layouts
        try {
            ArrayList<DataLayout> list = readData(ctx);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSelected(false);
            }
            saveLayouts(ctx, list);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public static void undefaultAllLayouts(Context ctx) {
        // Undefault all layouts
        try {
            ArrayList<DataLayout> list = readData(ctx);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setDefaultChoice(false);
            }
            saveLayouts(ctx, list);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public static void selectDefaultLayout(Context ctx){
        deselectAllLayouts(ctx);
        try {
            ArrayList<DataLayout> list = readData(ctx);
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).isDefaultChoice()){
                    list.get(i).setSelected(true);
                    return;
                }
            }
            saveLayouts(ctx, list);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<DataLayout> readData(Context ctx) throws IOException, ParserConfigurationException, SAXException {
        FileInputStream fis = null;
        try{
            fis = ctx.openFileInput(FILENAME);
        } catch (FileNotFoundException ex) {
            InitalizeLayoutFile(ctx);
            fis = ctx.openFileInput(FILENAME);
        }

        InputStreamReader isr = new InputStreamReader(fis);

        char[] inputBuffer = new char[fis.available()];
        isr.read(inputBuffer);
        String data = new String(inputBuffer);
        isr.close();
        fis.close();

        /*
         * Converting the String data to XML format so
         * that the DOM parser understands it as an XML input.
         */

        InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ArrayList<DataLayout> list = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(is);

        // Normalize the document
        dom.getDocumentElement().normalize();
        NodeList layouts = dom.getElementsByTagName("layout");

        for (int i = 0; i < layouts.getLength(); i++) {
            DataLayout dataLayout = new DataLayout();

            NodeList layoutAttributes = layouts.item(i).getChildNodes();
            for (int z = 0; z < layoutAttributes.getLength(); z++){
                switch(layoutAttributes.item(z).getNodeName()){
                    case "layoutTitle":
                        dataLayout.setLayoutTitle(layoutAttributes.item(z).getTextContent());
                        break;
                    case "selected":
                        dataLayout.setSelected(Boolean.parseBoolean(layoutAttributes.item(z).getTextContent()));
                        break;
                    case "default":
                        dataLayout.setDefaultChoice(Boolean.parseBoolean(layoutAttributes.item(z).getTextContent()));
                        break;
                    case "quickMenu":
                        dataLayout.setQuickMenuElement(Boolean.parseBoolean(layoutAttributes.item(z).getTextContent()));
                        break;
                    case "blocks":
                        NodeList blocks = ((Element)layoutAttributes.item(z)).getElementsByTagName("block");

                        for (int j = 0; j < blocks.getLength(); j++) {
                            DataBlock block = new DataBlock();
                            NodeList blockAttributes = blocks.item(j).getChildNodes();
                            for (int k = 0; k < blockAttributes.getLength(); k++) {
                                switch (blockAttributes.item(k).getNodeName()) {
                                    case "blockTitle":
                                        block.setBlockTitle(blockAttributes.item(k).getTextContent());
                                        break;
                                    case "type":
                                        block.setBlockType(Utils.BlockTypeEnum.fromString(blockAttributes.item(k).getTextContent()));
                                        break;
                                    case "magnitude":
                                        block.setMagnitude(Utils.Magnitude.fromString(blockAttributes.item(k).getTextContent()));
                                        break;
                                    case "unit":
                                        block.setUnit(Utils.Unit.fromString(blockAttributes.item(k).getTextContent()));
                                        break;
                                    case "dateStart":
                                        block.setDateStart(blockAttributes.item(k).getTextContent());
                                        break;
                                    case "dateEnd":
                                        block.setDateEnd(blockAttributes.item(k).getTextContent());
                                        break;
                                }
                            }
                            dataLayout.getDataBlocks().add(block);
                        }
                        break;
                }
            }
            list.add(dataLayout);
        }
        return list;
    }

    private static void InitalizeLayoutFile(Context ctx) {
        ArrayList<DataLayout> layouts = new ArrayList<>();
        ArrayList<DataBlock> dataBlocks = new ArrayList<>();
        dataBlocks.add(new DataBlock("Blok pierwszy", Utils.BlockTypeEnum.VALUE));
        dataBlocks.add(new DataBlock("Blok drugi", Utils.BlockTypeEnum.CHART));
        dataBlocks.add(new DataBlock("Blok trzeci", Utils.BlockTypeEnum.TABLE));

        layouts.add(new DataLayout("Tytuł pierwszy", new ArrayList<DataBlock>(), false, false, true));
        layouts.add(new DataLayout("Tytuł drugi", new ArrayList<>(dataBlocks), true, false));
        layouts.add(new DataLayout("Przykładowy tytuł układu", new ArrayList<>(dataBlocks), false, false, true));
        dataBlocks.remove(0);

        dataBlocks.add(new DataBlock("Blok czwarty", Utils.BlockTypeEnum.CHART));
        dataBlocks.add(new DataBlock("Blok piąty", Utils.BlockTypeEnum.VALUE));
        layouts.add(new DataLayout("Tytuł czwarty", new ArrayList<>(dataBlocks), false, true, true));
        try {
            saveLayouts(ctx, layouts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
