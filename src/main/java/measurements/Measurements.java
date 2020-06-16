package measurements;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.representation.R;
import com.representation.Utils;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import SaveData.SaveData;
import data.Database;
import data.LayoutsXml;
import layouteditor.DataBlock;
import layouteditor.LayoutEditor;
import layouts.DataLayout;
import layouts.LayoutsList;

public class Measurements extends AppCompatActivity {
    private ArrayList<DataLayout> layouts;
    private DataLayout currentLayout;
    private TextView layoutTitle;
    private DataPresentationAdapter dataPresentationAdapter;
    private ArrayList<DataBlock> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Utils.isDark)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.LightTheme);

        setContentView(R.layout.activity_measurements);

        // TODO: Replace data collecting method
        // Collect needed data from DataBase
        try {
            this.layouts = LayoutsXml.readData(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        // Configure action bar
        Toolbar actionbar = findViewById(R.id.measurements_action_bar);
        actionbar.setTitle(R.string.MEASUREMENTS_ACTION_BAR_TITLE);
        setSupportActionBar(actionbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // change currentLayout value to the layout which is currently selected (selected = true)
        setCurrentLayout();

        // Display selected layout title
        this.layoutTitle = findViewById(R.id.layout_title_text_view);
        this.layoutTitle.setText(currentLayout.getLayoutTitle());

        this.data = new ArrayList<>();
        this.data.addAll(this.currentLayout.getDataBlocks());
        // Create the adapter to convert the array to views
        this.dataPresentationAdapter = new DataPresentationAdapter(this, data);

        // Attach the adapter to a ListView
        ListView list = findViewById(R.id.data_presentation);
        list.setAdapter(dataPresentationAdapter);
    }

    private void setCurrentLayout() {
        for (DataLayout layout : layouts) {
            if (layout.isSelected()) {
                this.currentLayout = new DataLayout(layout);
            }
        }
    }

    private void displayCurrentLayout(){
        // Display selected layout title
        this.layoutTitle.setText(currentLayout.getLayoutTitle());

        // Select current DataBlocks
        this.data.removeAll(this.data);
        this.data.addAll(currentLayout.getDataBlocks());
        this.dataPresentationAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.measurements_add_new_layout) {
            Intent i = new Intent(this, LayoutEditor.class);
            this.startActivity(i);
        } else if (itemId == R.id.measurements_show_layouts_list) {
            Intent i = new Intent(this, LayoutsList.class);
            this.startActivity(i);
        } else if(itemId == R.id.measurements_export_to_pdf){
            Intent i = new Intent(this, SaveData.class);
            i.putExtra("layoutName",currentLayout.getLayoutTitle());

            ArrayList<DataBlock> dataBlockArrayList = currentLayout.getDataBlocks();
            int size = dataBlockArrayList.size();
            i.putExtra("datablockAmount",String.valueOf(size));

            for (int j = 0; j < dataBlockArrayList.size(); j++) {
                i.putExtra("datablock"+j,dataBlockArrayList.get(j).toString());
            }

            this.startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.measurements_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int position = 0; position < layouts.size(); position++) {
            // Check the menu item already added or not
            if (menu.findItem(position) == null) {
                // If it don't exist in menu then check if it should appear in quick select menu
                if (layouts.get(position).isQuickMenuElement()){
                    // add the menu item to menu
                    MenuItem item = menu.add(
                        Menu.NONE, // groupId
                        position, // itemId
                        position, // order
                        layouts.get(position).getLayoutTitle() // title
                    );

                    final int finalPosition = position;
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            // Deselect all layouts
                            for (int i = 0; i < layouts.size(); i++) {
                                layouts.get(i).setSelected(false);
                            }
                            // Select the layout on given position
                            layouts.get(finalPosition).setSelected(true);
                            setCurrentLayout();
                            displayCurrentLayout();
                            return true;
                        }
                    });
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            LayoutsXml.saveLayouts(getApplicationContext(), layouts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            this.layouts = LayoutsXml.readData(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
