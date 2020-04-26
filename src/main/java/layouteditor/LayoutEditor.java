package layouteditor;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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
        } else if (itemId == R.id.save_Layout_in_app) {
            displayPopupWindow();
        }

        return super.onOptionsItemSelected(item);
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
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }


    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.save_layout_popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
