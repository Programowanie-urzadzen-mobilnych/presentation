package layouteditor;

import android.content.Context;
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
import java.util.Arrays;
import java.util.List;

public class DataBlockAdapter extends ArrayAdapter<DataBlock> {
    private Context mContext;
    private ViewHolder viewHolder;
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
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Bind listener to deleteButton to make it work
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                remove(dataBlock);
            }
        });

        // Bind listener to moveDownButton to make it work
        viewHolder.moveDownButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).moveDownButtonHandler(position);
                }
            }
        });

        // Bind listener to moveUpButton to make it work
        viewHolder.moveUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).moveUpButtonHandler(position);
                }
            }
        });

        // If EditText already have a TextWatcher then remove it
        TextWatcher oldWatcher = (TextWatcher) viewHolder.blockTitleInput.getTag();
        if(oldWatcher != null)
            viewHolder.blockTitleInput.removeTextChangedListener(oldWatcher);

        // Apply data (from ArrayList connected to the adapter) to EditText
        viewHolder.blockTitleInput.setText(dataBlock.getBlockTitle());

        // Declare new TextWatcher and put it in editText tag. Also bind it to this EditText
        CustomTextWatcher newWatcher = new CustomTextWatcher(position, mContext);
        viewHolder.blockTitleInput.setTag(newWatcher);
        viewHolder.blockTitleInput.addTextChangedListener(newWatcher);

        if(dataBlock.getBlockType() != DataBlock.BlockTypeEnum.UNDEFINED)
            viewHolder.blockTypeInput.setSelection(dataBlock.getBlockType().id());

        // Handle block type spinner. Set values provided to adapter by ArrayList
        switch(dataBlock.getBlockType()){
            case VALUE:
                viewHolder.valueBlockContent.setVisibility(View.VISIBLE);
                viewHolder.tableBlockContent.setVisibility(View.GONE);
                viewHolder.chartBlockContent.setVisibility(View.GONE);
                configureValueViewControls(dataBlock, position);
                break;
            case TABLE:
                viewHolder.valueBlockContent.setVisibility(View.GONE);
                viewHolder.tableBlockContent.setVisibility(View.VISIBLE);
                viewHolder.chartBlockContent.setVisibility(View.GONE);
                //setProperLayoutBelowSpinner(convertView, R.layout.table_block_content, dataBlock, position);
                break;
            case CHART:
                viewHolder.valueBlockContent.setVisibility(View.GONE);
                viewHolder.tableBlockContent.setVisibility(View.GONE);
                viewHolder.chartBlockContent.setVisibility(View.VISIBLE);
                //setProperLayoutBelowSpinner(convertView, R.layout.chart_block_content, dataBlock, position);
                break;
        }

        // Apply custom OnItemSelectedListener, which will change date in ArrayList connected witch Adapter,
        // when user apply changes to the spinner on the ListView
        viewHolder.blockTypeInput.setOnItemSelectedListener(new CustomOnItemSelectedListener(position, mContext, this));

        // Return the completed view to render on screen
        return convertView;
    }

    private void configureValueViewControls(final DataBlock dataBlock, final int itemPosition) {
        List<String> myArraySpinner = new ArrayList<>();

        if(dataBlock.getMagnitude() != DataBlock.Magnitude.UNDEFINED)
            viewHolder.valueMagnitudeSpinner.setSelection(dataBlock.getMagnitude().id());

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
            case BATTERY_VOLTAGE: case SOLAR_PANEL_VOLTAGE: case NODE_VOLTAGE:
                myArraySpinner = Arrays.asList(mContext.getResources().getStringArray(R.array.VOLTAGE_SPINNER));
                break;
            case BATTERY_CURRENT: case SOLAR_PANEL_CURRENT: case NODE_CURRENT:
                myArraySpinner = Arrays.asList(mContext.getResources().getStringArray(R.array.CURRENT_SPINNER));
                break;
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, myArraySpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.valueUnitSpinner.setAdapter(spinnerArrayAdapter);

        viewHolder.valueMagnitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).setMagnitude(itemPosition, pos);
                    notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    static class ViewHolder {
        EditText blockTitleInput;
        Button deleteButton;
        Button moveDownButton;
        Button moveUpButton;
        Spinner blockTypeInput;
        View valueBlockContent;
        View tableBlockContent;
        View chartBlockContent;

        Spinner valueMagnitudeSpinner;
        Spinner valueUnitSpinner;

        public ViewHolder(View convertView) {
            this.blockTitleInput = convertView.findViewById(R.id.block_title_input);
            this.deleteButton = convertView.findViewById(R.id.delete_block_button);
            this.moveDownButton = convertView.findViewById(R.id.move_block_down_button);
            this.moveUpButton = convertView.findViewById(R.id.move_block_up_button);
            this.blockTypeInput = convertView.findViewById(R.id.block_type_spinner);
            this.valueBlockContent = convertView.findViewById(R.id.value_block_content);
            this.tableBlockContent = convertView.findViewById(R.id.table_block_content);
            this.chartBlockContent = convertView.findViewById(R.id.chart_block_content);

            this.valueMagnitudeSpinner = convertView.findViewById(R.id.value_block_magnitude_spinner);
            this.valueUnitSpinner = convertView.findViewById(R.id.value_block_unit_spinner);
        }
    }
}

