package layouteditor;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.representation.R;

import java.util.ArrayList;
import java.util.Collections;

public class DataBlockAdapter extends ArrayAdapter<DataBlock> {
    public DataBlockAdapter(Context context, ArrayList<DataBlock> dataBlocks) {
        super(context, 0, dataBlocks);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final DataBlock dataBlock = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_data_block_view, parent, false);
        }

        // Bind listener to deleteButton to make it work
        final Button deleteButton = convertView.findViewById(R.id.delete_block_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutEditor.layout.getDataBlocks().remove(position);
                LayoutEditor.dataBlockAdapter.notifyDataSetChanged();
            }
        });

        // Bind listener to moveDownButton to make it work
        final Button moveDownButton = convertView.findViewById(R.id.move_block_down_button);
        moveDownButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(LayoutEditor.layout.getDataBlocks().size()>position+1) {
                    Collections.swap(LayoutEditor.layout.getDataBlocks(), position, position + 1);
                    LayoutEditor.dataBlockAdapter.notifyDataSetChanged();
                }
            }
        });

        // Bind listener to moveUpButton to make it work
        final Button moveUpButton = convertView.findViewById(R.id.move_block_up_button);
        moveUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(position>0) {
                    Collections.swap(LayoutEditor.layout.getDataBlocks(), position, position - 1);
                    LayoutEditor.dataBlockAdapter.notifyDataSetChanged();
                }
            }
        });

        // Configure EditText's TextWatcher. Create new ViewHolder and apply EditText to it.
        EditText blockTitleInput = convertView.findViewById(R.id.block_title_input);
        ViewHolder holder = new ViewHolder();
        holder.editText = blockTitleInput;

        // If EditText already have a TextWatcher then remove it
        TextWatcher oldWatcher = (TextWatcher) holder.editText.getTag();
        if(oldWatcher != null)
            holder.editText.removeTextChangedListener(oldWatcher);

        // Apply data (from ArrayList connected to the adapter) to EditText
        blockTitleInput.setText(dataBlock.getBlockTitle());

        // Declare new TextWatcher and put it in editText tag. Also bind it to this EditText
        CustomTextWatcher newWatcher = new CustomTextWatcher(position);
        holder.editText.setTag(newWatcher);
        holder.editText.addTextChangedListener(newWatcher);

        // Handle block type spinner. Set values provided to adapter by ArrayList
        Spinner blockTypeInput = convertView.findViewById(R.id.block_type_spinner);
        switch(dataBlock.getBlockType()){
            case VALUE:
                blockTypeInput.setSelection(0);
                break;
            case TABLE:
                blockTypeInput.setSelection(1);
                break;
            case CHART:
                blockTypeInput.setSelection(2);
                break;
        }
        // Apply custom OnItemSelectedListener, which will change date in ArrayList connected witch Adapter,
        // when user apply changes to the spinner on the ListView
        blockTypeInput.setOnItemSelectedListener(new CustomOnItemSelectedListener(position));

        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder {
        EditText editText;
    }
}

