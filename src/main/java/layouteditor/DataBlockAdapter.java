package layouteditor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.representation.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

        if(dataBlock.getBlockType() != Utils.BlockTypeEnum.UNDEFINED)
            viewHolder.blockTypeInput.setSelection(dataBlock.getBlockType().id());

        // Handle block type spinner. Set values provided to adapter by ArrayList
        switch(dataBlock.getBlockType()){
            case VALUE:
                viewHolder.valueBlockContent.setVisibility(View.VISIBLE);
                viewHolder.tableBlockContent.setVisibility(View.GONE);
                viewHolder.chartBlockContent.setVisibility(View.GONE);
                viewHolder.chartTwoBlockContent.setVisibility(View.GONE);
                configureValueViewControls(dataBlock, position);
                break;
            case TABLE:
                viewHolder.valueBlockContent.setVisibility(View.GONE);
                viewHolder.tableBlockContent.setVisibility(View.VISIBLE);
                viewHolder.chartBlockContent.setVisibility(View.GONE);
                viewHolder.chartTwoBlockContent.setVisibility(View.GONE);
                configureTableViewControls(dataBlock, position);
                break;
            case CHART:
                viewHolder.valueBlockContent.setVisibility(View.GONE);
                viewHolder.tableBlockContent.setVisibility(View.GONE);
                viewHolder.chartBlockContent.setVisibility(View.VISIBLE);
                viewHolder.chartTwoBlockContent.setVisibility(View.GONE);
                configureChartViewControls(dataBlock, position);
                break;
            case CHARTTWO:
                viewHolder.valueBlockContent.setVisibility(View.GONE);
                viewHolder.tableBlockContent.setVisibility(View.GONE);
                viewHolder.chartBlockContent.setVisibility(View.GONE);
                viewHolder.chartTwoBlockContent.setVisibility(View.VISIBLE);
                configureChartTwoViewControls(dataBlock, position);
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

        if(dataBlock.getMagnitude() != Utils.Magnitude.UNDEFINED)
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

        if(dataBlock.getUnit() != Utils.Unit.UNDEFINED){
            viewHolder.valueUnitSpinner.setSelection(dataBlock.getUnit().id());
        }

        viewHolder.valueMagnitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).setUnit(itemPosition, 0, dataBlock.getMagnitude());
                    ((LayoutEditor)mContext).setMagnitude(itemPosition, pos);
                    notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        viewHolder.valueUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).setUnit(itemPosition, pos, dataBlock.getMagnitude());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void configureTableViewControls(final DataBlock dataBlock, final int itemPosition) {
        List<String> myArraySpinner = new ArrayList<>();

        if(dataBlock.getMagnitude() != Utils.Magnitude.UNDEFINED)
            viewHolder.tableMagnitudeSpinner.setSelection(dataBlock.getMagnitude().id());

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
        viewHolder.tableUnitSpinner.setAdapter(spinnerArrayAdapter);

        if(dataBlock.getUnit() != Utils.Unit.UNDEFINED){
            viewHolder.tableUnitSpinner.setSelection(dataBlock.getUnit().id());
        }

        viewHolder.tableMagnitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).setUnit(itemPosition, 0, dataBlock.getMagnitude());
                    ((LayoutEditor)mContext).setMagnitude(itemPosition, pos);
                    notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        viewHolder.tableUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).setUnit(itemPosition, pos, dataBlock.getMagnitude());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.DATE_FORMAT, Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat(Utils.TIME_FORMAT, Locale.getDefault());

        viewHolder.tableStartDateInput.setText(dateFormat.format(dataBlock.getDateStart()));
        viewHolder.tableStartTimeInput.setText(timeFormat.format(dataBlock.getDateStart()));
        viewHolder.tableEndDateInput.setText(dateFormat.format(dataBlock.getDateEnd()));
        viewHolder.tableEndTimeInput.setText(timeFormat.format(dataBlock.getDateEnd()));

        viewHolder.tableStartDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDatePickerDialog dialog = new CustomDatePickerDialog(mContext, itemPosition, CustomDatePickerDialog.DialogType.START);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, dialog, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
                datePickerDialog.show();
            }
        });

        viewHolder.tableEndDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDatePickerDialog dialog = new CustomDatePickerDialog(mContext, itemPosition, CustomDatePickerDialog.DialogType.START);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, dialog, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
                datePickerDialog.show();
            }
        });

        viewHolder.tableStartTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTimePickerDialog dialog = new CustomTimePickerDialog(mContext, itemPosition, CustomTimePickerDialog.DialogType.END);
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, dialog, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                timePickerDialog.show();
            }
        });

        viewHolder.tableEndTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTimePickerDialog dialog = new CustomTimePickerDialog(mContext, itemPosition, CustomTimePickerDialog.DialogType.END);
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, dialog, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                timePickerDialog.show();
            }
        });
    }

    private void configureChartViewControls(final DataBlock dataBlock, final int itemPosition) {
        List<String> myArraySpinner = new ArrayList<>();

        if(dataBlock.getMagnitude() != Utils.Magnitude.UNDEFINED)
            viewHolder.chartMagnitudeSpinner.setSelection(dataBlock.getMagnitude().id());

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
        viewHolder.chartUnitSpinner.setAdapter(spinnerArrayAdapter);

        if(dataBlock.getUnit() != Utils.Unit.UNDEFINED){
            viewHolder.chartUnitSpinner.setSelection(dataBlock.getUnit().id());
        }

        viewHolder.chartMagnitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).setUnit(itemPosition, 0, dataBlock.getMagnitude());
                    ((LayoutEditor)mContext).setMagnitude(itemPosition, pos);
                    notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        viewHolder.chartUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).setUnit(itemPosition, pos, dataBlock.getMagnitude());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.DATE_FORMAT, Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat(Utils.TIME_FORMAT, Locale.getDefault());

        viewHolder.chartStartDateInput.setText(dateFormat.format(dataBlock.getDateStart()));
        viewHolder.chartStartTimeInput.setText(timeFormat.format(dataBlock.getDateStart()));
        viewHolder.chartEndDateInput.setText(dateFormat.format(dataBlock.getDateEnd()));
        viewHolder.chartEndTimeInput.setText(timeFormat.format(dataBlock.getDateEnd()));

        viewHolder.chartStartDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDatePickerDialog dialog = new CustomDatePickerDialog(mContext, itemPosition, CustomDatePickerDialog.DialogType.START);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, dialog, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
                datePickerDialog.show();
            }
        });

        viewHolder.chartEndDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDatePickerDialog dialog = new CustomDatePickerDialog(mContext, itemPosition, CustomDatePickerDialog.DialogType.START);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, dialog, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
                datePickerDialog.show();
            }
        });

        viewHolder.chartStartTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTimePickerDialog dialog = new CustomTimePickerDialog(mContext, itemPosition, CustomTimePickerDialog.DialogType.END);
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, dialog, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                timePickerDialog.show();
            }
        });

        viewHolder.chartEndTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTimePickerDialog dialog = new CustomTimePickerDialog(mContext, itemPosition, CustomTimePickerDialog.DialogType.END);
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, dialog, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                timePickerDialog.show();
            }
        });
    }

    private void configureChartTwoViewControls(final DataBlock dataBlock, final int itemPosition) {
        List<String> myArraySpinner = new ArrayList<>();

        if(dataBlock.getMagnitude() != Utils.Magnitude.UNDEFINED)
            viewHolder.chartTwoXMagnitudeSpinner.setSelection(dataBlock.getMagnitude().id());

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
        viewHolder.chartTwoXUnitSpinner.setAdapter(spinnerArrayAdapter);

        if(dataBlock.getUnit() != Utils.Unit.UNDEFINED){
            viewHolder.chartTwoXUnitSpinner.setSelection(dataBlock.getUnit().id());
        }

        viewHolder.chartTwoXMagnitudeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).setUnit(itemPosition, 0, dataBlock.getMagnitude());
                    ((LayoutEditor)mContext).setMagnitude(itemPosition, pos);
                    notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        viewHolder.chartTwoXUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (mContext instanceof LayoutEditor) {
                    ((LayoutEditor)mContext).setUnit(itemPosition, pos, dataBlock.getMagnitude());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


    }


    public static class ViewHolder {
        EditText blockTitleInput;
        Button deleteButton;
        Button moveDownButton;
        Button moveUpButton;
        Spinner blockTypeInput;
        View valueBlockContent;
        View tableBlockContent;
        View chartBlockContent;
        View chartTwoBlockContent;

        Spinner valueMagnitudeSpinner;
        Spinner valueUnitSpinner;

        Spinner tableMagnitudeSpinner;
        Spinner tableUnitSpinner;
        EditText tableStartDateInput;
        EditText tableStartTimeInput;
        EditText tableEndDateInput;
        EditText tableEndTimeInput;

        Spinner chartTypeSpinner;
        Spinner chartMagnitudeSpinner;
        Spinner chartUnitSpinner;
        EditText chartStartDateInput;
        EditText chartStartTimeInput;
        EditText chartEndDateInput;
        EditText chartEndTimeInput;

        Spinner chartTwoTypeSpinner;
        Spinner chartTwoXMagnitudeSpinner;
        Spinner chartTwoXUnitSpinner;
        Spinner chartTwoYMagnitudeSpinner;
        Spinner chartTwoYUnitSpinner;


        public ViewHolder(View convertView) {
            this.blockTitleInput = convertView.findViewById(R.id.block_title_input);
            this.deleteButton = convertView.findViewById(R.id.delete_block_button);
            this.moveDownButton = convertView.findViewById(R.id.move_block_down_button);
            this.moveUpButton = convertView.findViewById(R.id.move_block_up_button);
            this.blockTypeInput = convertView.findViewById(R.id.block_type_spinner);
            this.valueBlockContent = convertView.findViewById(R.id.value_block_content);
            this.tableBlockContent = convertView.findViewById(R.id.table_block_content);
            this.chartBlockContent = convertView.findViewById(R.id.chart_block_content);
            this.chartTwoBlockContent = convertView.findViewById(R.id.chart_two_block_content);

            this.valueMagnitudeSpinner = convertView.findViewById(R.id.value_block_magnitude_spinner);
            this.valueUnitSpinner = convertView.findViewById(R.id.value_block_unit_spinner);

            this.tableMagnitudeSpinner = convertView.findViewById(R.id.table_block_magnitude_spinner);
            this.tableUnitSpinner = convertView.findViewById(R.id.table_block_unit_spinner);
            this.tableStartDateInput = convertView.findViewById(R.id.table_block_start_date_input);
            this.tableStartTimeInput = convertView.findViewById(R.id.table_block_start_time_input);
            this.tableEndDateInput = convertView.findViewById(R.id.table_block_end_date_input);
            this.tableEndTimeInput = convertView.findViewById(R.id.table_block_end_time_input);

            this.chartTypeSpinner = convertView.findViewById(R.id.chart_block_type_spinner);
            this.chartMagnitudeSpinner = convertView.findViewById(R.id.chart_block_magnitude_spinner);
            this.chartUnitSpinner = convertView.findViewById(R.id.chart_block_unit_spinner);
            this.chartStartDateInput = convertView.findViewById(R.id.chart_block_start_date_input);
            this.chartStartTimeInput = convertView.findViewById(R.id.chart_block_start_time_input);
            this.chartEndDateInput = convertView.findViewById(R.id.chart_block_end_date_input);
            this.chartEndTimeInput = convertView.findViewById(R.id.chart_block_end_time_input);

            this.chartTwoTypeSpinner = convertView.findViewById(R.id.chart_two_block_type_spinner);
            this.chartTwoXMagnitudeSpinner = convertView.findViewById(R.id.chart_two_block_x_magnitude_spinner);
            this.chartTwoXUnitSpinner = convertView.findViewById(R.id.chart_two_block_x_unit_spinner);
            this.chartTwoYMagnitudeSpinner = convertView.findViewById(R.id.chart_two_block_y_magnitude_spinner);
            this.chartTwoYUnitSpinner = convertView.findViewById(R.id.chart_two_block_y_unit_spinner);

        }
    }
}

