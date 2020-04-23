package SaveData;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.representation.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*
	Export results to a PDF / XML file with an ability to choose save location#41
	Client want to be able to create export files. We are going to let him do it.
	There should be a drop-down list next to menu drop-down in "Pomiary" activity.
	If user is displaying specific layout (or just the default one) then he is also able to "print" the data, which is displayed.
	On the same view he clicks the drop-down list in up-right corner and choose the file type and later the location, where file is going to be saved.
*/

public class SaveData extends Activity {

    private static final int WRITE_EXTERNAL_STORAGE_STATUS = 0;
    String dane = "Losowe dane mile widziane";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);

        //TODO: unpack data from bundle

        final Button button = findViewById(R.id.ZapiszDaneDoPDFButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                saveDataToPDF();
            }
        });
    }

    private void saveDataToPDF(){
        selectFilePath();
    }

    private void selectFilePath() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 9999:
                if(data!=null){
                    Log.i("Test", "Result URI " + data.getData());

                    Uri uri = data.getData();
                    Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                            DocumentsContract.getTreeDocumentId(uri));
                    String filePath = getPath(this, docUri);


                    Log.i("Test", "Result Path " + filePath);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        //stringtopdf(filePath);
                        createFile(filePath);
                    }
                }else{
                    Log.i("error","Failed to provide with URI - no data");
                }
                break;
            default:
                Log.i("error","Failed to provide with URI - ?");
                break;
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
            createPdf(dane,filePath);
/*            File path  = new File(filePath);
            String fileName = "myfile.txt";
            String value = "BENIZ";

            File output = new File(filePath,"myfile.txt");

            try {
                FileOutputStream fileout = new FileOutputStream(output.getAbsolutePath());
                OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                outputWriter.write(value);
                outputWriter.close();
                //display file saved message
                Log.i("Success","File saved in "+filePath);
                Toast.makeText(getBaseContext(), "File saved successfully!",
                        Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }*/
        }else{
            //createFile(filePath); //nie jestem pewny czy to bezpieczne będzie :D
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(String data,String path){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(data, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
        // draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void stringtopdf(String filePath) {

        String extstoragedir = filePath;

        File dir = new File(extstoragedir, "a directory");
        if(!dir.exists()){
            // do something
            Log.i("Success","Utworzono folder w "+extstoragedir+"/a directory");
        }
        File file = new File(extstoragedir + "/a directory/" + "a file");
        if(!file.exists()){
            Log.i("Success","Utworzono plik w "+extstoragedir + "/a directory/" + "a file");

        }

        try {

            Log.i("Success","Zapisano plik w "+extstoragedir);

        } catch (Exception e) {
            Log.i("error", e.getLocalizedMessage());
            Log.i("error","Nie można zapisać pliku w "+extstoragedir);
        }
    }
}
