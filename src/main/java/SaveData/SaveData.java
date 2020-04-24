package SaveData;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.representation.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import lib.folderpicker.FolderPicker;

/*
	Export results to a PDF / XML file with an ability to choose save location#41
	Client want to be able to create export files. We are going to let him do it.
	There should be a drop-down list next to menu drop-down in "Pomiary" activity.
	If user is displaying specific layout (or just the default one) then he is also able to "print" the data, which is displayed.
	On the same view he clicks the drop-down list in up-right corner and choose the file type and later the location, where file is going to be saved.
*/

public class SaveData extends Activity {

    private static final int WRITE_EXTERNAL_STORAGE_STATUS = 0;
    private static final int FOLDERPICKER_CODE = 9998;

    ArrayList<ExportData> exportDataArrayList = new ArrayList<ExportData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);

        //TODO: unpack data from bundle
        //lets say we are getting string with variable names separated by ';'
        Intent intent = getIntent();
        String variableNames = intent.getStringExtra("variableNames");
        //test string
        variableNames = "Temperatura;Cisnienie;Wilgotnosc;Napiecie";

        String names[] = variableNames.split(";");

        Log.i("OnCreate","Start "+names.length);
        //now lets unload those variables
        for (String name: names) {

            ExportData tempExportData = new ExportData(name);

            //String values = intent.getStringExtra(name);
            //String timeStamps = intent.getStringExtra(name+"_timeStamps");

            //test strings;
            String values =        "1.23;      2.03;       3.33;       4.20;       6.12;       5.66;       4.44;       3.22";
            String timeStamps =    "06:43:19;  08:22:21;   09:42:43;   10:25:19;   11:13:43;   13:33:19;   15:43:19;   18:43:19";

            String[] tempV = values.split(";");
            String[] tempT = timeStamps.split(";");

            if(tempV.length!=tempT.length){
                //throw new MissingDataException;
                Log.i("error","dane są nie równe");
            }else{
                for (int i = 0; i < tempV.length-1; i++) {
                    tempExportData.addValue(tempV[i],tempT[i]);
                    Log.i("ExportData: ",tempV[i]+" "+tempT[i]);
                }
            }

            exportDataArrayList.add(tempExportData);
        }

        final Button button = findViewById(R.id.ZapiszDaneDoPDFButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FolderPicker.class);
                startActivityForResult(intent, FOLDERPICKER_CODE);
            }
        });
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
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createFile(String filePath)  {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_STATUS);
            }
        }

        String temp = String.valueOf(WRITE_EXTERNAL_STORAGE_STATUS);
        if(WRITE_EXTERNAL_STORAGE_STATUS==0){
            Log.i("Test","Write external storage status: "+temp);
            createPdf(filePath);
        }else{
            //createFile(filePath); //nie jestem pewny czy to bezpieczne będzie :D
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

        for (ExportData exportData: exportDataArrayList) {

            //subtitle
            String subtitle = exportData.getValuesName();
            canvas.drawText(subtitle,40,y,paint);
            y+=30;

            //values as value | timestamp
            String workingString = "";
            Log.i("PDFCreator name: ",subtitle);
            Log.i("PDFCreator dataSize: ",String.valueOf(exportData.getDataLength()));
            for(int a = 0; a < exportData.getDataLength()-1; a++){
                workingString = exportData.getStringOf(a);
                Log.i("PDFCreator string: ",workingString);
                canvas.drawText(workingString,20,y,paint);
                y+=20;
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
        File filePath = new File(targetPdf,"test-2.pdf");
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }
}
