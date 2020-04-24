package layouteditor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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

import layouts.DataLayout;

public class LayoutEditor extends AppCompatActivity {
    public static DataLayout layout;
    public static DataBlockAdapter dataBlockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_editor);

        // Configure action bar
        Toolbar actionbar = findViewById(R.id.action_bar);
        actionbar.setTitle(R.string.LAYOUT_EDITOR_ACTION_BAR_TITLE);
        setSupportActionBar(actionbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.save_layout_in_storage) {
            Toast.makeText(getApplicationContext(), "Zapisz układ na dysku", Toast.LENGTH_SHORT).show();
            // TODO: Functionality of saving layouts to the xml (or another extension) file in device storage
        } else if (itemId == R.id.load_layout_from_file) {
            Toast.makeText(getApplicationContext(), "Wczytaj układ z pliku", Toast.LENGTH_SHORT).show();
            // TODO: Functionality of loading files from file to the app
        } else if (itemId == R.id.save_Layout_in_app) {
            Toast.makeText(getApplicationContext(), "Zapisz układ", Toast.LENGTH_SHORT).show();
            // TODO: Functionality od saving layouts inside the app
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // TODO: define what should happen when you click back button
        Log.println(Log.INFO,"LayoutEditor: ", "Back Button Pressed");
    }
}
