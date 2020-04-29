package layouteditor;

import android.content.Context;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.representation.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBlockAdapter extends ArrayAdapter<DataBlock> {
    private Context mContext;
    public DataBlockAdapter(Context context, ArrayList<DataBlock> dataBlocks) {
        super(context, 0, dataBlocks);
        this.mContext = context;
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
                remove(dataBlock);
            }
        });

        // Bind listener to moveDownButton to make it work
        final Button moveDownButton = convertView.findViewById(R.id.move_block_down_button);
        moveDownButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).moveDownButtonHandler(position);
                }
                notifyDataSetChanged();
            }
        });

        // Bind listener to moveUpButton to make it work
        final Button moveUpButton = convertView.findViewById(R.id.move_block_up_button);
        moveUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).moveUpButtonHandler(position);
                }
                notifyDataSetChanged();
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
        CustomTextWatcher newWatcher = new CustomTextWatcher(position, mContext);
        holder.editText.setTag(newWatcher);
        holder.editText.addTextChangedListener(newWatcher);

        // Handle block type spinner. Set values provided to adapter by ArrayList
        Spinner blockTypeInput = convertView.findViewById(R.id.block_type_spinner);
        switch(dataBlock.getBlockType()){
            case VALUE:
                blockTypeInput.setSelection(0);
                convertView.findViewById(R.id.two).setVisibility(View.GONE);
                convertView.findViewById(R.id.three).setVisibility(View.GONE);
                //setProperLayoutBelowSpinner(convertView, R.layout.value_block_content, dataBlock, position);
                break;
            case TABLE:
                blockTypeInput.setSelection(1);
                convertView.findViewById(R.id.one).setVisibility(View.GONE);
                convertView.findViewById(R.id.three).setVisibility(View.GONE);
                //setProperLayoutBelowSpinner(convertView, R.layout.table_block_content, dataBlock, position);
                break;
            case CHART:
                blockTypeInput.setSelection(2);
                convertView.findViewById(R.id.one).setVisibility(View.GONE);
                convertView.findViewById(R.id.two).setVisibility(View.GONE);
                //setProperLayoutBelowSpinner(convertView, R.layout.chart_block_content, dataBlock, position);
                break;
        }
        // Apply custom OnItemSelectedListener, which will change date in ArrayList connected witch Adapter,
        // when user apply changes to the spinner on the ListView
        blockTypeInput.setOnItemSelectedListener(new CustomOnItemSelectedListener(position, mContext, this));

        Log.println(Log.INFO, "DSADASD", convertView.findViewById(R.id.value_block_magnitude_spinner).toString());
        // Return the completed view to render on screen
        return convertView;
    }

    /*private void setProperLayoutBelowSpinner(View convertView, int view_id, final DataBlock dataBlock, final int itemPosition) {
        LinearLayout rl = convertView.findViewById(R.id.specific_block_type_content);

        LayoutInflater inflater = ((LayoutEditor)mContext).getLayoutInflater();
        View view = inflater.inflate(view_id, null);

        rl.removeAllViews();
        rl.addView(view);

        if(view_id == R.layout.value_block_content) {
            final Spinner magnitudeSpinner = rl.findViewById(R.id.value_block_magnitude_spinner);
            final Spinner unitSpinner = rl.findViewById(R.id.value_block_unit_spinner);
            List<String> myArraySpinner = new ArrayList<>();

            if(dataBlock.getMagnitude() != DataBlock.Magnitude.UNDEFINED)
                magnitudeSpinner.setSelection(dataBlock.getMagnitude().id()-1);

            switch (dataBlock.getMagnitude()) {
                case TEMPERATURE:
                    myArraySpinner = Arrays.asList(mContext.getResources().getStringArray(R.array.TEMPERATURE_SPINNER));
                    break;
                case HUMIDITY:
                    myArraySpinner = Arrays.asList(mContext.getResources().getStringArray(R.array.HUMIDITY_SPINNER));
                    break;
                case PRESSURE:
                    myArraySpinner = Arrays.asList(mContext.getResources().getStringArray(R.array.PRESSURE_SPINNER));
                    break;
                case BATTERY_VOLTAGE:
                case SOLAR_PANEL_VOLTAGE:
                case NODE_VOLTAGE:
                    myArraySpinner = Arrays.asList(mContext.getResources().getStringArray(R.array.VOLTAGE_SPINNER));
                    break;
                case BATTERY_CURRENT:
                case SOLAR_PANEL_CURRENT:
                case NODE_CURRENT:
                    myArraySpinner = Arrays.asList(mContext.getResources().getStringArray(R.array.CURRENT_SPINNER));
                    break;
            }

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, myArraySpinner);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            unitSpinner.setAdapter(spinnerArrayAdapter);

            magnitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mContext instanceof LayoutEditor) {
                        ((LayoutEditor)mContext).setMagnitude(position);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }*/

    static class ViewHolder {
        EditText editText;
    }
}

