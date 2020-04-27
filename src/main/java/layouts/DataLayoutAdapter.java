package layouts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.representation.R;

import java.util.ArrayList;

import layouteditor.LayoutEditor;

public class DataLayoutAdapter extends ArrayAdapter<DataLayout> {
    private Context mContext;
    public DataLayoutAdapter(Context context, ArrayList<DataLayout> layouts) {
        super(context, 0, layouts);
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final DataLayout dataLayout = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_data_layout_view, parent, false);
        }

        final TextView layoutTitle = convertView.findViewById(R.id.layout_title);
        layoutTitle.setText(dataLayout.getLayoutTitle());

        // Bind listener to deleteButton to make it work.
        // If layout is default button should be disabled and inform user why it is disabled.
        final Button deleteButton = convertView.findViewById(R.id.delete_layout_button);
        if(dataLayout.isDefaultChoice()){
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(mContext, R.string.CANNOT_DELETE_DEFAULT_LAYOUT, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mContext instanceof LayoutsList) {
                        ((LayoutsList) mContext).deleteButtonHandler(position);
                    }
                    notifyDataSetChanged();
                }
            });
        }

        // Bind listener to editButton to move to LayoutEditor
        final Button editButton = convertView.findViewById(R.id.edit_layout_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(mContext, LayoutEditor.class);
                i.putExtra("layoutPosition", position);
                mContext.startActivity(i);
            }
        });

        // Bind listener to selectButton to select clicked Layout as the one which will be displayed in the view
        // Only one Layout can be selected
        final Button selectButton = convertView.findViewById(R.id.select_layout_button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mContext instanceof LayoutsList) {
                    ((LayoutsList)mContext).selectButtonHandler(position);
                }
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
