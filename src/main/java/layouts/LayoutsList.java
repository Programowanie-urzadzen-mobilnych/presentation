package layouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.representation.R;

import java.util.ArrayList;

import layouteditor.DataBlock;
import layouteditor.LayoutEditor;

public class LayoutsList extends AppCompatActivity {
    public static ArrayList<DataLayout> layouts;
    public static DataLayoutAdapter dataLayoutAdapter;

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

        // Construct the data source
        layouts = new ArrayList<>();
        layouts.add(new DataLayout("Tytuł pierwszy"));
        layouts.add(new DataLayout("Tytuł drugi", new ArrayList<DataBlock>()));
        layouts.add(new DataLayout("Tytuł trzeci", new ArrayList<DataBlock>(), false));

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
