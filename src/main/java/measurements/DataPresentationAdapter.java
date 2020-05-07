package measurements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.representation.R;
import com.representation.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import data.Database;
import data.ExampleRecord;
import layouteditor.DataBlock;

public class DataPresentationAdapter extends ArrayAdapter<DataBlock> {
    private Context mContext;
    private ArrayList<DataBlock> dataBlocks;
    private ValueViewHolder valueViewHolder;
    private TableViewHolder tableViewHolder;
    private ChartViewHolder chartViewHolder;

    public DataPresentationAdapter(Context context, ArrayList<DataBlock> dataBlocks) {
        super(context, 0, dataBlocks);
        this.mContext = context;
        this.dataBlocks = dataBlocks;
    }

    @Override
    public int getItemViewType(int position) {
        switch(dataBlocks.get(position).getBlockType()){
            case VALUE:
                return 0;
            case TABLE:
                return 1;
            case CHART:
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final DataBlock dataBlock = getItem(position);
        int type = getItemViewType(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (type == 0) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.value_view, parent, false);
                valueViewHolder = new ValueViewHolder(convertView);
                convertView.setTag(valueViewHolder);
            } else {
                valueViewHolder = (ValueViewHolder) convertView.getTag();
            }

            // get data from db TODO: data should be already prepared
            ExampleRecord rec = Database.getLatestValue(dataBlock.getMagnitude());

            // Set title
            valueViewHolder.blockTitle.setText(dataBlock.getBlockTitle());

            // Fill the view of this block with data
            valueViewHolder.valueTextView.setText((rec.getValue() + " [" + Utils.Unit.getName(mContext, rec.getUnit()) + "]"));
            valueViewHolder.magnitudeOfValueTextView.setText((mContext.getResources().getString(R.string.MAGNITUDE) + Utils.Magnitude.getName(mContext, rec.getMagnitude())));
            SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.DATETIME_FORMAT, Locale.getDefault());
            valueViewHolder.dateOfValueTextView.setText(("Data pomiaru:\n" + dateFormat.format(rec.getTimestamp())));
        } else if (type == 1){
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.table_view, parent, false);
                tableViewHolder = new TableViewHolder(convertView);
                convertView.setTag(tableViewHolder);
            } else {
                tableViewHolder = (TableViewHolder) convertView.getTag();
            }

            // Get Data from DB TODO: data should be already prepared
            ArrayList<ExampleRecord> data = Database.getDataBetween(dataBlock.getDateStart(), dataBlock.getDateEnd(), dataBlock.getMagnitude());

            // Set title
            tableViewHolder.blockTitle.setText(dataBlock.getBlockTitle());

            if(tableViewHolder.tableLayout.getChildCount()==0) {
                // Create header
                TableRow headerRow = constructHeader(dataBlock);
                tableViewHolder.tableLayout.addView(headerRow, 0);

                // define style
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                TableRow.LayoutParams cellLayoutParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                cellLayoutParams.setMargins(2, 2, 2, 2);
                SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.DATETIME_FORMAT, Locale.getDefault());

                int i = 1;
                for (ExampleRecord rec : data) {
                    TableRow tableRow = constructRow(rec, layoutParams, cellLayoutParams, dateFormat, dataBlock);
                    tableViewHolder.tableLayout.addView(tableRow, i);
                    i++;
                }
            }
        } else if(type == 2) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.chart_view, parent, false);
                chartViewHolder = new ChartViewHolder(convertView);
                convertView.setTag(chartViewHolder);
            } else {
                chartViewHolder = (ChartViewHolder) convertView.getTag();
            }

            // Set title
            chartViewHolder.blockTitle.setText(dataBlock.getBlockTitle());

            // TODO: Chart View Case
            // Get Data from DB...
        }

        // Return the completed view to render on screen
        return convertView;
    }

    private TableRow constructRow(ExampleRecord rec, TableRow.LayoutParams layoutParams, TableRow.LayoutParams cellLayoutParams, SimpleDateFormat dateFormat, DataBlock dataBlock) {
        TableRow tableRow = new TableRow(mContext);
        tableRow.setLayoutParams(layoutParams);

        // Construct cell one
        LinearLayout cell1 = new LinearLayout(mContext);

        cell1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.PALE_GREY));
        cell1.setLayoutParams(cellLayoutParams);

        TextView timestamp = new TextView(mContext);
        timestamp.setText(dateFormat.format(rec.getTimestamp()));
        timestamp.setPadding(20, 10, 20, 10);
        timestamp.setTextColor(ContextCompat.getColor(mContext, R.color.BLACK));

        cell1.addView(timestamp);

        // Construct cell two
        LinearLayout cell2 = new LinearLayout(mContext);

        cell2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.PALE_GREY));
        cell2.setLayoutParams(cellLayoutParams);


        TextView value = new TextView(mContext);

        value.setText(RecalculateValue.recalculate(dataBlock.getUnit(), rec.getValue()));

        value.setPadding(20, 10, 20, 10);
        value.setTextColor(ContextCompat.getColor(mContext, R.color.BLACK));

        cell2.addView(value);

        // Finally add views to row
        tableRow.addView(cell1);
        tableRow.addView(cell2);

        return tableRow;
    }

    private TableRow constructHeader(DataBlock dataBlock) {
        TableRow headerRow = new TableRow(mContext);
        TableRow.LayoutParams LayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        headerRow.setLayoutParams(LayoutParams);

        // Set layout params for header cell
        TableRow.LayoutParams headerCellLayoutParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerCellLayoutParams.setMargins(4, 4, 4, 4);

        // Header cell one
        LinearLayout headerCellOne = new LinearLayout(mContext);

        headerCellOne.setBackgroundColor(ContextCompat.getColor(mContext, R.color.PALE_GREY));
        headerCellOne.setLayoutParams(headerCellLayoutParams);

        TextView timestampHeader = new TextView(mContext);
        timestampHeader.setText("Data pomiaru");
        timestampHeader.setPadding(20, 10, 20, 10);
        timestampHeader.setTextColor(ContextCompat.getColor(mContext, R.color.BLACK));
        timestampHeader.setTextSize(18);

        headerCellOne.addView(timestampHeader);

        // Header cell two
        LinearLayout headerCellTwo = new LinearLayout(mContext);

        headerCellTwo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.PALE_GREY));
        headerCellTwo.setLayoutParams(headerCellLayoutParams);

        TextView valueHeader = new TextView(mContext);
        String magnitudeStr = Utils.Magnitude.getName(mContext, dataBlock.getMagnitude());
        String unitStr = Utils.Unit.getName(mContext, dataBlock.getUnit());
        String finalString = magnitudeStr + " [" + unitStr + "]";

        valueHeader.setText(finalString);
        valueHeader.setPadding(20, 10, 20, 10);
        valueHeader.setTextColor(ContextCompat.getColor(mContext, R.color.BLACK));
        valueHeader.setTextSize(18);

        headerCellTwo.addView(valueHeader);

        // fill row and layout
        headerRow.addView(headerCellOne);
        headerRow.addView(headerCellTwo);

        return headerRow;
    }

    protected class ValueViewHolder {
        TextView blockTitle;
        TextView valueTextView;
        TextView magnitudeOfValueTextView;
        TextView dateOfValueTextView;

        public ValueViewHolder(View convertView) {
            blockTitle = convertView.findViewById(R.id.block_title_text_view);
            valueTextView = convertView.findViewById(R.id.value_text_view);
            magnitudeOfValueTextView = convertView.findViewById(R.id.magnitude_of_value_text_view);
            dateOfValueTextView = convertView.findViewById(R.id.date_of_value_text_view);
        }
    }

    protected class TableViewHolder {
        TextView blockTitle;
        TableLayout tableLayout;
        public TableViewHolder(View convertView) {
            blockTitle = convertView.findViewById(R.id.block_title_text_view);
            tableLayout = convertView.findViewById(R.id.table_view);
        }
    }

    protected class ChartViewHolder {
        TextView blockTitle;
        public ChartViewHolder(View convertView) {
            blockTitle = convertView.findViewById(R.id.block_title_text_view);
        }
    }
}
