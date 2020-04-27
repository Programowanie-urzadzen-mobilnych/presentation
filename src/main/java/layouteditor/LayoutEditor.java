package layouteditor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.representation.R;

import java.util.ArrayList;

import layouts.DataLayout;

public class LayoutEditor extends AppCompatActivity {
    public static DataLayout layout;
    public static DataBlockAdapter dataBlockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_editor);

        // Configure action bar
        Toolbar actionbar = findViewById(R.id.layout_editor_action_bar);
        actionbar.setTitle(R.string.LAYOUT_EDITOR_ACTION_BAR_TITLE);
        setSupportActionBar(actionbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);


        // Construct the data source
        layout = new DataLayout("", new ArrayList<DataBlock>());
        layout.getDataBlocks().add(new DataBlock("Przykładowy tytuł", DataBlock.BlockTypeEnum.CHART));
        layout.getDataBlocks().add(new DataBlock("Drugi tytuł", DataBlock.BlockTypeEnum.VALUE));
        layout.getDataBlocks().add(new DataBlock("Trzeci tytuł", DataBlock.BlockTypeEnum.TABLE));

        // Create the adapter to convert the array to views
        dataBlockAdapter = new DataBlockAdapter(this, layout.getDataBlocks());

        // Attach the adapter to a ListView
        ListView list = findViewById(R.id.layout_editor_list_view);
        list.setAdapter(dataBlockAdapter);

        // Define buttons actions
        final Button addButton = findViewById(R.id.add_block_to_layout);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layout.getDataBlocks().add(new DataBlock());
                dataBlockAdapter.notifyDataSetChanged();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.save_layout_in_storage) {
            Toast.makeText(getApplicationContext(), "Zapisz układ na dysku", Toast.LENGTH_SHORT).show();
            // TODO: Functionality of saving layouts to the xml (or another extension) file in device storage
        } else if (itemId == R.id.load_layout_from_file) {
            Toast.makeText(getApplicationContext(), "Wczytaj układ z pliku", Toast.LENGTH_SHORT).show();
            // TODO: Functionality of loading files from file to the app
        } else if (itemId == R.id.save_Layout_in_app || itemId == android.R.id.home) {
            displayPopupWindow();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Set the menu for upper right corner dropdown
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onBackPressed(){
        // define what happens when back button pressed
        displayPopupWindow();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void displayPopupWindow() {
        // Display Popup and darken the background.
        // Set listener to remove dark overlay when clicked outside the window.
        final PopupWindow popup = new PopupWindow(this);
        View layout = getLayoutInflater().inflate(R.layout.save_layout_popup_window, null);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        applyDim(root, 0.5f);

        // Collect controls from popup form and fill with data (if there is some data)
        final EditText layoutTitle = popupLayout.findViewById(R.id.layout_title_input);
        layoutTitle.setText(layout.getLayoutTitle());

        final CheckBox isDefault = popupLayout.findViewById(R.id.layout_default_checkbox);
        isDefault.setChecked(layout.isDefaultChoice());

        final CheckBox isInQuickMenu = popupLayout.findViewById(R.id.layout_quick_select_checkbox);
        isInQuickMenu.setChecked(layout.isQuickMenuElement());

        // Apply listener for save button
        Button saveButton = popupLayout.findViewById(R.id.save_layout_in_app_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setSelected(true);
                layout.setDefaultChoice(isDefault.isChecked());
                layout.setQuickMenuElement(isInQuickMenu.isChecked());
                if(layoutTitle.getText().toString().equals("") || layoutTitle.getText().toString().isEmpty()) {
                    layout.setLayoutTitle(getResources().getString(R.string.DEFAULT_LAYOUT_TITLE));
                } else {
                    layout.setLayoutTitle(layoutTitle.getText().toString());
                }

                // if editing remove edited layout position to prevent duplicates
                if(layoutPosition > -1) {
                    Database.layouts.remove(layoutPosition);
                }

                Database.layouts.add(layout);
                Intent i = new Intent(v.getContext(), LayoutsList.class);
                v.getContext().startActivity(i);
            }
        });

        // Set listener to remove dark overlay when clicked outside the window.
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
                clearDim(root);
            }
        });

        popup.setContentView(layout);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        int popupWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        int popupHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.showAtLocation(popupLayout, Gravity.CENTER, 0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        // Applies dark overlay to the background of given view
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void clearDim(@NonNull ViewGroup parent) {
        // removes dark overlay from background
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }
}
