package measurements;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.representation.R;

import java.util.ArrayList;

import data.Database;
import layouteditor.LayoutEditor;
import layouts.DataLayout;
import layouts.LayoutsList;

public class Measurements extends AppCompatActivity {
    private ArrayList<DataLayout> layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);

        // TODO: Replace data collecting method
        // Collect needed data from DataBase
        this.layouts = Database.layouts;

        // Configure action bar
        Toolbar actionbar = findViewById(R.id.measurements_action_bar);
        actionbar.setTitle(R.string.MEASUREMENTS_ACTION_BAR_TITLE);
        setSupportActionBar(actionbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        refreshCurrentlySelectedLayout();
    }

    private void refreshCurrentlySelectedLayout() {
        TextView currentlySelectedLayout = findViewById(R.id.currently_selected_layout);
        String selectedLayoutsTitles = "";
        for (DataLayout layout: layouts) {
            if(layout.isSelected()){
                selectedLayoutsTitles += layout.getLayoutTitle() + "\n";
            }
        }
        currentlySelectedLayout.setText(selectedLayoutsTitles);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.measurements_add_new_layout) {
            Intent i = new Intent(this, LayoutEditor.class);
            this.startActivity(i);
        } else if (itemId == R.id.measurements_show_layouts_list) {
            Intent i = new Intent(this, LayoutsList.class);
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
                            refreshCurrentlySelectedLayout();
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
}
