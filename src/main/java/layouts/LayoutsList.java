package layouts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;

import com.representation.R;

import java.util.ArrayList;

import layouteditor.DataBlock;
import layouteditor.DataBlockAdapter;

public class LayoutsList extends AppCompatActivity {
    private ArrayList<DataLayout> layouts;
    private DataLayoutAdapter dataLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layouts_list2);

        // Configure action bar
        Toolbar actionbar = findViewById(R.id.action_bar);
        actionbar.setTitle(R.string.LAYOUT_LIST_ACTION_BAR_TTLE);
        setSupportActionBar(actionbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Construct the data source
        layouts = new ArrayList<>();
        layouts.add(new DataLayout());
        layouts.add(new DataLayout());
        layouts.add(new DataLayout());

        // Create the adapter to convert the array to views
        dataLayoutAdapter = new DataLayoutAdapter(this, layouts);

        // Attach the adapter to a ListView
        ListView list = findViewById(R.id.layout_list_list_view);
        list.setAdapter(dataLayoutAdapter);
    }
}
