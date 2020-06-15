package layouteditor;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.representation.R;
import com.representation.Utils;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import data.LayoutsXml;
import layouts.DataLayout;

public class LayoutEditor extends AppCompatActivity {
    private DataLayout layout;
    private DataBlockAdapter dataBlockAdapter;
    private int layoutPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_editor);

        // Configure action bar
        Toolbar actionbar = findViewById(R.id.layout_editor_action_bar);
        actionbar.setTitle(getResources().getString(R.string.LAYOUT_EDITOR_ACTION_BAR_TITLE));
        setSupportActionBar(actionbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // TODO: Replace data collecting method
            // Collect needed data from DataBase
            this.layoutPosition = extras.getInt("layoutPosition");
            try {
                ArrayList<DataLayout> list = LayoutsXml.readData(getApplicationContext());
                this.layout = list.get(layoutPosition);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } else {
            this.layout = new DataLayout();
            this.layoutPosition = -1;
        }

        // Create the adapter to convert the array to views
        dataBlockAdapter = new DataBlockAdapter(this, layout.getDataBlocks());

        dataBlockAdapter.setNotifyOnChange(true);

        // Attach the adapter to a ListView
        ListView list = findViewById(R.id.layout_editor_list_view);
        list.setAdapter(dataBlockAdapter);

        // Define buttons actions
        final Button addButton = findViewById(R.id.add_block_to_layout);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dataBlockAdapter.add(new DataBlock());
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.save_Layout_in_app || itemId == android.R.id.home) {
            displayWarningPopup();
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void displayWarningPopup() {
        // Display Popup and darken the background.
        final PopupWindow popup = new PopupWindow(this);
        final View popupLayout = getLayoutInflater().inflate(R.layout.layout_editor_warning_popup, null);

        final ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        Utils.applyDim(root, 0.5f);

        Button leaveButton = popupLayout.findViewById(R.id.leave_without_saving_button);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                finish();
            }
        });

        Button saveChangesButton = popupLayout.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.clearDim(root);
                popup.dismiss();
                displayPopupWindow();
            }
        });

        // Set listener to remove dark overlay when clicked outside the window.
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
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
        final PopupWindow popup = new PopupWindow(this);
        final View popupLayout = getLayoutInflater().inflate(R.layout.save_layout_popup_window, null);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        Utils.applyDim(root, 0.5f);

        // Collect controls from popup form and fill with data (if there is some data)
        final EditText layoutTitle = popupLayout.findViewById(R.id.layout_title_input);
        layoutTitle.setText(layout.getLayoutTitle());

        final CheckBox isDefault = popupLayout.findViewById(R.id.layout_default_checkbox);
        isDefault.setChecked(layout.isDefaultChoice());

        // If layout is default u shouldn't be able to undefault it,
        // because there won't be any default layouts. In that case checkbox is deactivated.
        if(layout.isDefaultChoice()){
            isDefault.setEnabled(false);
        }

        final CheckBox isInQuickMenu = popupLayout.findViewById(R.id.layout_quick_select_checkbox);
        isInQuickMenu.setChecked(layout.isQuickMenuElement());

        // Apply listener for save button
        Button saveButton = popupLayout.findViewById(R.id.save_layout_in_app_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Defaultly set newly added layout as selected. Before it deselect all layouts
                // to prevent situation where there are multiple layouts selected.
                LayoutsXml.deselectAllLayouts(getApplicationContext());
                layout.setSelected(true);

                // If newly added layout is selected as default and checkbox is enabled than all
                // layouts need to be undefaulted before making this one default. This will prevent
                // multiple default layouts.
                if(isDefault.isChecked()){
                    if(isDefault.isEnabled()){
                        LayoutsXml.undefaultAllLayouts(getApplicationContext());
                    }
                    layout.setDefaultChoice(true);
                } else {
                    layout.setDefaultChoice(false);
                }

                // There may be many elements in Quick menu...
                layout.setQuickMenuElement(isInQuickMenu.isChecked());
                if(layoutTitle.getText().toString().equals("") || layoutTitle.getText().toString().isEmpty()) {
                    layout.setLayoutTitle(getResources().getString(R.string.DEFAULT_LAYOUT_TITLE));
                } else {
                    layout.setLayoutTitle(layoutTitle.getText().toString());
                }

                LayoutsXml.replaceLayout(getApplicationContext(), layout, layoutPosition);

                // Move to LayoutsList activity where new added element should be listed
                popup.dismiss();
                finish();
            }
        });

        // Set listener to remove dark overlay when clicked outside the window.
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
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

    public void moveDownButtonHandler(int position) {
        if(layout.getDataBlocks().size()>position+1) {
            Collections.swap(layout.getDataBlocks(), position, position + 1);
        }
        dataBlockAdapter.notifyDataSetChanged();
    }

    public void moveUpButtonHandler(int position) {
        if(position>0) {
            Collections.swap(layout.getDataBlocks(), position, position - 1);
        }
        dataBlockAdapter.notifyDataSetChanged();
    }

    public void changeBlockTitle(int itemPosition, String title) {
        layout.getDataBlocks().get(itemPosition).setBlockTitle(title);
    }

    public void setBlockType(int itemPosition, Utils.BlockTypeEnum type) {
        layout.getDataBlocks().get(itemPosition).setBlockType(type);
    }

    public void setMagnitude(int itemPosition, int position) {
        layout.getDataBlocks().get(itemPosition).setMagnitude(Utils.Magnitude.fromId(position));
        dataBlockAdapter.notifyDataSetChanged();
    }

    public void setUnit(int itemPosition, int position, Utils.Magnitude magnitude) {
        layout.getDataBlocks().get(itemPosition).setUnit(Utils.Unit.fromId(position, magnitude));
        // displayLayoutContent();
    }

    public void setStartTimeText(Date date, int itemPosition) {
        layout.getDataBlocks().get(itemPosition).setDateStart(date, false);
        dataBlockAdapter.notifyDataSetChanged();
    }

    public void setStartDateText(Date date, int itemPosition) {
        layout.getDataBlocks().get(itemPosition).setDateStart(date, true);
        dataBlockAdapter.notifyDataSetChanged();
    }

    public void setEndTimeText(Date date, int itemPosition) {
        layout.getDataBlocks().get(itemPosition).setDateEnd(date, false);
        dataBlockAdapter.notifyDataSetChanged();
    }

    public void setEndDateText(Date date, int itemPosition) {
        layout.getDataBlocks().get(itemPosition).setDateEnd(date, true);
        dataBlockAdapter.notifyDataSetChanged();
    }

//    public void displayLayoutContent(){
//        layout.displayContent();
//    }
}
