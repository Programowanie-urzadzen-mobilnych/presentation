package layouts;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;

import layouteditor.DataBlock;
import pl.grupa33inf.watchdog.exceptions.ELayoutName;

public class DataLayout implements Serializable {
    private String layoutTitle;
    private ArrayList<DataBlock> dataBlocks;
    private boolean selected;
    private boolean defaultChoice;
    private boolean quickMenuElement;

    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks, boolean selected, boolean defaultChoice, boolean quickMenuElement) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = new ArrayList<>(dataBlocks);
        this.selected = selected;
        this.defaultChoice = defaultChoice;
        this.quickMenuElement = quickMenuElement;
    }
    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks, boolean selected, boolean defaultChoice) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = new ArrayList<>(dataBlocks);
        this.selected = selected;
        this.defaultChoice = defaultChoice;
        this.quickMenuElement = false;
    }
    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks, boolean selected) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = new ArrayList<>(dataBlocks);
        this.selected = selected;
        this.defaultChoice = false;
        this.quickMenuElement = false;
    }
    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = new ArrayList<>(dataBlocks);
        this.selected = false;
        this.defaultChoice = false;
        this.quickMenuElement = false;
    }
    public DataLayout(String layoutTitle) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = new ArrayList<>();
        this.selected = false;
        this.defaultChoice = false;
        this.quickMenuElement = false;
    }
    public DataLayout() {
        this.layoutTitle = null;
        this.dataBlocks = new ArrayList<>();
        this.selected = false;
        this.defaultChoice = false;
        this.quickMenuElement = false;
    }

    public DataLayout(DataLayout dataLayout) {
        this.layoutTitle = dataLayout.layoutTitle;
        this.dataBlocks = new ArrayList<>(dataLayout.dataBlocks);
        this.selected = dataLayout.selected;
        this.defaultChoice = dataLayout.defaultChoice;
        this.quickMenuElement = dataLayout.quickMenuElement;
    }

    public String getLayoutTitle() {
        return layoutTitle;
    }
    public void setLayoutTitle(String layoutTitle) {
        this.layoutTitle = layoutTitle;
    }
    public ArrayList<DataBlock> getDataBlocks() {
        return dataBlocks;
    }
    public void setDataBlocks(ArrayList<DataBlock> dataBlocks) {
        this.dataBlocks = dataBlocks;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isDefaultChoice() {
        return defaultChoice;
    }
    public void setDefaultChoice(boolean defaultChoice) {
        this.defaultChoice = defaultChoice;
    }
    public boolean isQuickMenuElement() {
        return quickMenuElement;
    }
    public void setQuickMenuElement(boolean quickMenuElement) {
        this.quickMenuElement = quickMenuElement;
    }

    public void displayContent() {
        Log.println(Log.ERROR, "DataLayout", "content");
        Log.println(Log.INFO, "DataLayout", "layoutTitle: " + layoutTitle + "\n" +
                "selected: " + selected + "\n" +
                "defaultChoice: " + defaultChoice + "\n" +
                "quickMenuElement: " + quickMenuElement);
        for (DataBlock dataBlock: dataBlocks) {
            dataBlock.displayContent();
        }
    }

    public void saveToFile(Context mContext, String pathText, String pathFileName) {
        String path = pathText+"/"+pathFileName+".ssf";
        //path = "/storage/emulated/0/saves/test.txt";
        System.out.println(pathText+" + "+pathFileName);


        File filePath = new File(path);

        if(filePath.exists()){
            System.out.println("File already Exists");
            String fixedPath = ELayoutName.getNewPath(pathText,pathFileName,"ssf");
            System.out.println("New File path "+fixedPath);

            filePath = new File(fixedPath);
            if(!filePath.exists()){
                System.out.println("What th efuck?");
            }
        }

        try {
            FileWriter out = new FileWriter(filePath);
            out.write(layoutTitle);
            out.write('\n');

            for (DataBlock db: dataBlocks) {
                out.write(db.toString());
                out.write('\n');
            }

            out.close();

        } catch (Exception e) {
            Log.e("main", "error "+e.toString());
        }
    }
}
