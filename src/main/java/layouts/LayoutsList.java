package layouts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.representation.R;
import com.representation.Utils;

import java.util.ArrayList;

import data.Database;
import layouteditor.LayoutEditor;
import lib.folderpicker.FolderPicker;

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
        this.dataLayoutAdapter = new DataLayoutAdapter(this, layouts);

        // Attach the adapter to a ListView
        ListView list = findViewById(R.id.layout_list_list_view);
        list.setAdapter(this.dataLayoutAdapter);

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
        this.dataLayoutAdapter.notifyDataSetChanged();
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
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.LAYOUT_HAS_BEEN_CHOSEN) +
                layouts.get(position).getLayoutTitle(), Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void displayDeletionPopup(final int position) {
        // Display Popup and darken the background.
        final PopupWindow popup = new PopupWindow(this);
        final View popupLayout = getLayoutInflater().inflate(R.layout.ensure_deletion_of_layout_popup, null);

        final ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        Utils.applyDim(root, 0.5f);

        TextView popupTitle = popupLayout.findViewById(R.id.popup_title);
        String updatedTitle = getResources().getString(R.string.LAYOUT_DELETION) + ": " + layouts.get(position).getLayoutTitle();
        popupTitle.setText(updatedTitle);

        Button confirmDeletion = popupLayout.findViewById(R.id.confirm_button);
        confirmDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButtonHandler(position);
                dataLayoutAdapter.notifyDataSetChanged();
                popup.dismiss();
            }
        });

        Button declineDeletion = popupLayout.findViewById(R.id.decline_button);
        declineDeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        // Set listener to remove dark overlay when clicked outside the window.
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.clearDim(root);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void displayPopupWindow(int itemId) {
        // Display Popup and darken the background.
        final PopupWindow popup = new PopupWindow(this);
        final View popupLayout = getLayoutInflater().inflate(R.layout.save_on_device_popup_window, null);

        final ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        Utils.applyDim(root, 0.5f);

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
                dl.saveToFile(mContext, pathText.getText().toString(), pathFileName.getText().toString());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.LAYOUT_HAS_BEEN_SAVED), Toast.LENGTH_SHORT).show();
                popup.dismiss();
            }
        });

        // Set listener to remove dark overlay when clicked outside the window.
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.clearDim(root);
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
        Log.d("SAVING","pls result");
        if(resultCode != RESULT_CANCELED){
            if(data!= null){
                if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {
                    String path = data.getExtras().getString("data");
                    Log.d("SAVING","pls path"+path);
                    pathText.setText(path);
                    /*if(dl!=null){
                        LayoutSaver.SaveLayout(dl,path);
                    }*/
                }
            }else{
                Log.i("error","Failed to provide with URI - no data");
            }
        }else{
            Log.i("error","Request cancelled");
        }
    }
}
