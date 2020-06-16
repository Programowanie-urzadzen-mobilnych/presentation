package SaveData;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.representation.R;
import com.representation.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import charts.ChartObj;
import charts.ChartView;
import data.ExampleRecord;
import layouteditor.DataBlock;
import lib.folderpicker.FolderPicker;
import pl.grupa33inf.ssi.data_store.api.DataStoreApi;
import pl.grupa33inf.ssi.data_store.api.INodeDataStore;
import pl.grupa33inf.ssi.data_store.api.NodeValue;
import pl.grupa33inf.ssi.data_store.api.NodeVariable;
import pl.grupa33inf.watchdog.exceptions.ELayoutName;

import static com.representation.Utils.WRITE_EXTERNAL_STORAGE_STATUS;

/*
	Export results to a PDF / XML file with an ability to choose save location#41
	Client want to be able to create export files. We are going to let him do it.
	There should be a drop-down list next to menu drop-down in "Pomiary" activity.
	If user is displaying specific layout (or just the default one) then he is also able to "print" the data, which is displayed.
	On the same view he clicks the drop-down list in up-right corner and choose the file type and later the location, where file is going to be saved.
*/

public class SaveData extends Activity {

    private static final int FOLDERPICKER_CODE = 9998;

    ArrayList<DataBlock> dataBlocks = new ArrayList<>();

    private String layoutName = "test";
    private int datablockAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);

        try{
            Intent intent = getIntent();
            layoutName = intent.getStringExtra("layoutName");
            String datablockAmountString = intent.getStringExtra("datablockAmount");
            datablockAmount = Integer.parseInt(datablockAmountString);

            for (int j = 0; j < datablockAmount; j++) {
                dataBlocks.add(new DataBlock(intent.getStringExtra("datablock"+j)));
            }

            Intent intent2 = new Intent(getApplicationContext(), FolderPicker.class);
            startActivityForResult(intent2, FOLDERPICKER_CODE);
        }catch (Exception e){
            finish();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED){
            if(data!= null){
                if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {
                    String folderLocation = data.getExtras().getString("data");
                    Log.i( "folderLocation", folderLocation );
                    createFile(folderLocation);
                }
            }else{
                Log.i("error","Failed to provide with URI - no data");
            }
        }else{
            Log.i("error","Request cancelled");
        }
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createFile(String filePath)  {

        String temp = String.valueOf(WRITE_EXTERNAL_STORAGE_STATUS);
        if(WRITE_EXTERNAL_STORAGE_STATUS==0){
            Log.i("Test","Write external storage status: "+temp);
            createPdf(filePath);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(String path){

        int pageWidth = 300;
        int pageHeight = 600;

        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        //prepare paint
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        //write data
        //canvas.drawText(data, 80, 50, paint);

        float x=0,y=50;

        //TODO: if y > pageheight + offset -> nextpage

        canvas.drawText("Export danych",60,y,paint);
        y+=30;

        for (int i = 0; i < dataBlocks.size(); i++) {

            String subtitle = dataBlocks.get(i).getBlockTitle();
            canvas.drawText(subtitle,40,y,paint);
            y+=30;

            String workingString = "";
            switch(dataBlocks.get(i).getBlockType()){
                case VALUE:
                    ExampleRecord er = getLatestValue(dataBlocks.get(i).getMagnitude());
                        workingString = er.toString();

                        canvas.drawText(workingString,20,y,paint);
                        y+=20;
                    break;
                case TABLE:
                    List<ExampleRecord> exampleRecordList = getDataBetween(dataBlocks.get(i).getDateStart(),dataBlocks.get(i).getDateEnd(),dataBlocks.get(i).getMagnitude());

                    for(int a = 0; a < exampleRecordList.size(); a++){
                        workingString = exampleRecordList.get(a).toString();
                        canvas.drawText(workingString,20,y,paint);
                        y+=20;
                    }

                    break;
                case CHART:
                    List<ExampleRecord> exampleRecordListChart = getDataBetween(dataBlocks.get(i).getDateStart(),dataBlocks.get(i).getDateEnd(),dataBlocks.get(i).getMagnitude());

                    ArrayList<Float> values = new ArrayList<>();
                    for (ExampleRecord rec : exampleRecordListChart) {
                        values.add((float)rec.getValue());
                    }

                    ChartObj obj = new ChartObj(values, dataBlocks.get(i).getDateStart(), dataBlocks.get(i).getDateEnd(), dataBlocks.get(i).getUnit().toString(), false);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        ChartView chart = new ChartView(this, obj);
                        BarChart mChart = new BarChart(this);
                        setChartData(mChart);
                        //Bitmap bmp = mChart.getChartBitmap();
                        //bmp.setWidth(mChart.getWidth());
                        //bmp.setHeight(mChart.getHeight());
                        //canvas.drawBitmap(bmp,x,y,null);
                        //y+=100;
                    }else{
                        Log.d("SaveDataChart","Brak api 26+");
                    }

                    break;
                case CHARTTWO:

                    break;
                case UNDEFINED:

                    break;
            }
        }

        document.finishPage(page);

        // write the document content
        String directory_path = path;
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path;

        String filename = layoutName;
        String extension = "pdf";

        String fixedPath = "";

        File filePath = new File(targetPdf,filename+"."+extension);

        if(filePath.exists()){
            Log.d("Saving","File already Exists");
            fixedPath = ELayoutName.getNewPath(directory_path,filename,extension);
            filePath = new File(fixedPath);
        }

        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done as "+filePath, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    private void setChartData(BarChart mChart) {
        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            float mult = (500 + 1);
            float val = (float) (Math.random() * mult);

            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }


    private static ExampleRecord getLatestValue(Utils.Magnitude magnitude) {
        ExampleRecord exampleRecord = new ExampleRecord();
        INodeDataStore ds = DataStoreApi.getNodeDataStore();
        String tag = "dbNodesTestLastValueSaveData";

        try {
            UUID uid =  UUID.fromString("14ed798f-37a4-4501-8489-5fa5528a20ec");
            Map<String, NodeVariable> temp = ds.readAllVariables(uid);
            Collection<NodeVariable> nodeCol = temp.values();
            String dbMagnitude = temp.keySet().toString();
            ArrayList<NodeVariable> nodeArrList = new ArrayList<>(nodeCol);

            if(nodeArrList.get(0).getCurrentValue()!=null){
                Log.d("VAL: ",nodeArrList.get(0).getCurrentValue().toString());
                Double value = Double.parseDouble(nodeArrList.get(0).getCurrentValue().toString());
                exampleRecord.setValue(value);
            }else{
                //get 1st from history
                Double value = Double.parseDouble(nodeArrList.get(0).getHistory().get(0).getValue());
                exampleRecord.setValue(value);
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return exampleRecord;
    }

    public static ArrayList<ExampleRecord> getDataBetween(Date data1, Date data2, final Utils.Magnitude magnitude) {
        INodeDataStore ds = DataStoreApi.getNodeDataStore();
        ArrayList<ExampleRecord> result = new ArrayList<>();
        String tag = "dbNodesTestSaveData";

        try {
            UUID uid =  UUID.fromString("14ed798f-37a4-4501-8489-5fa5528a20ec");
            Map<String, NodeVariable> temp = ds.readAllVariables(uid);
            Collection<NodeVariable> nodeCol = temp.values();

            String dbMagnitude = temp.keySet().toString();

            ArrayList<NodeVariable> nodeArrList = new ArrayList<>(nodeCol);
            ArrayList<NodeValue> nodeValArrList = new ArrayList<>();
            for (int i = 0; i < nodeArrList.size(); i++) {
                List<NodeValue> tempList = nodeArrList.get(i).getHistory();
                for (int j = 0; j < tempList.size(); j++) {
                    Log.d(tag,tempList.get(j).getValue()+" "+tempList.get(j).getTimestamp());
                    nodeValArrList.add(tempList.get(j));
                }
            }

            for (int i = 0; i < nodeValArrList.size(); i++) {
                Log.d(tag,nodeValArrList.get(i).getValue().toString());
                //if(rec.getMagnitude()==magnitude)
                Double val = Double.parseDouble(nodeValArrList.get(i).getValue());
                result.add(new ExampleRecord(nodeValArrList.get(i).getTimestamp(),magnitude,Utils.Unit.CELSIUS, val));

            }
            /*history.stream().filter( v => v.getTimestamp().before(od) && v.getTimestamp().after(do)).collect(Collectors.toList())*/

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}
