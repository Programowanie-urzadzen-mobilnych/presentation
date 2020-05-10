package layouts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.representation.R;
import com.representation.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

import data.Database;
import layouteditor.DataBlock;
import layouteditor.LayoutEditor;
import lib.folderpicker.FolderPicker;
import measurements.Measurements;

import static com.representation.Utils.FOLDERPICKER_CODE;
import static com.representation.Utils.PICKFILE_RESULT_CODE;

public class LayoutsList extends AppCompatActivity {
    private ArrayList<DataLayout> layouts;
    private static final int FOLDERPICKER_CODE = 9998;
    private final Context mContext = this;
    private EditText pathText;
    private EditText pathFileName;
    private DataLayoutAdapter dataLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layouts_list);

        // Configure action bar
        Toolbar actionbar = findViewById(R.id.layouts_list_action_bar);
        actionbar.setTitle(R.string.LAYOUT_LIST_ACTION_BAR_TTLE);
        setSupportActionBar(actionbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // TODO: Replace data collecting method
        // Collect needed data from DataBase
        this.layouts = Database.layouts;

        // Create the adapter to convert the array to views
        dataLayoutAdapter = new DataLayoutAdapter(this, layouts);

        // Attach the adapter to a ListView
        ListView list = findViewById(R.id.layout_list_list_view);
        list.setAdapter(dataLayoutAdapter);

        // define action for addNewLayoutButton. It moves user to LayoutEditor.
        final Button addNewLayoutButton = findViewById(R.id.add_new_layout_button);
        addNewLayoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), LayoutEditor.class);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.layouts_list_add_new_layout) {
            Intent i = new Intent(this, LayoutEditor.class);
            this.startActivity(i);
        }
        if(itemId == R.id.layouts_list_save_layout_on_device){
            //filepicker
            Intent intent = new Intent(this, FolderPicker.class);
            intent.putExtra("pickFiles", true);
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: Replace data pushing method
        // Push changed data to DataBase
        Database.layouts = this.layouts;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Replace data collecting method
        // Collect needed data from DataBase
        this.layouts = Database.layouts;
    }

    public void deleteButtonHandler(int position) {
        // Remove chosen layout from list
        this.layouts.remove(position);
    }

    public void selectButtonHandler(int position) {
        // Deselect all layouts
        for (int i = 0; i < layouts.size(); i++) {
            layouts.get(i).setSelected(false);
        }
        // Select the layout on given position
        layouts.get(position).setSelected(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void displayPopupWindow(final ViewGroup parentGroup, int itemId) {
        // Display Popup and darken the background.
        final PopupWindow popup = new PopupWindow(this);
        final View popupLayout = LayoutInflater.from(this).inflate(R.layout.save_on_device_popup_window,parentGroup,false);

        final DataLayout dl = layouts.get(itemId);

        pathFileName = popupLayout.findViewById(R.id.layout_title_choose_path_input);
        pathFileName.setText(dl.getLayoutTitle());

        pathText = popupLayout.findViewById(R.id.layout_path);
        pathText.setText("/storage/emulated/0/saves");

        final Button pathPickerButton = popupLayout.findViewById(R.id.choose_path);
        pathPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FolderPicker.class);
                startActivityForResult(intent,FOLDERPICKER_CODE);
            }
        });

        final Button saveButton = popupLayout.findViewById(R.id.save_layout_in_storage_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //actual saving finally jesus christ
                dl.saveToFile(mContext,pathText.getText().toString(),pathFileName.getText().toString());
                popup.dismiss();
            }
        });


        popup.setContentView(popupLayout);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);

        int popupWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        int popupHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.showAtLocation(popupLayout, Gravity.CENTER, 0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                Log.d("LOADING","pls result");
                if (resultCode == -1) {
                    String path = data.getExtras().getString("data");
                    Log.d("LOADING","pls path"+path);
                    LoadDataLayoutFromDisk(path);
                }

                break;

            case FOLDERPICKER_CODE:
                Log.d("SAVING","pls result");
                if(data!= null && resultCode == Activity.RESULT_OK){
                    String path = data.getExtras().getString("data");
                    Log.d("SAVING","pls path"+path);
                    pathText.setText(path);
                }
                break;
        }

    }

    private void LoadDataLayoutFromDisk(String path) {
        DataLayout dl = new DataLayout();
        ArrayList<DataBlock> dbs = new ArrayList<>();

        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            //1st line - datalayout name
            dl.setLayoutTitle(br.readLine());

            //rest are blocks
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(";");
                dbs.add(new DataBlock(temp[0],
                        Utils.BlockTypeEnum.fromString(temp[1]),
                        Utils.Magnitude.fromString(temp[2]),
                        Utils.Unit.fromString(temp[3]),
                        new Date(temp[4]),
                        new Date(temp[5])
                        ));
            }
            br.close();
            dl.setDataBlocks(dbs);
            //layouts.add(dl);
            Database.layouts.add(dl);
            dataLayoutAdapter.notifyDataSetInvalidated();
        }
        catch (Exception e) {
            //You'll need to add proper error handling here
            //Abrasie*
        }
    }
}
