package measurements;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.representation.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

import data.Database;
import data.ExampleRecord;
import layouteditor.DataBlock;
import layouteditor.LayoutEditor;
import layouts.DataLayout;
import layouts.LayoutsList;

import static data.Database.getDataBetween;

public class DataPresentationAdapter extends ArrayAdapter<DataBlock> {
    private Context mContext;
    private ViewHolder viewHolder;

    public DataPresentationAdapter(Context context, ArrayList<DataBlock> dataBlocks) {
        super(context, 0, dataBlocks);
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final DataBlock dataBlock = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if(dataBlock.getBlockType() == DataBlock.BlockTypeEnum.VALUE)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.value_view, parent, false);
        else if(dataBlock.getBlockType() == DataBlock.BlockTypeEnum.TABLE)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.table_view, parent, false);
        else if(dataBlock.getBlockType() == DataBlock.BlockTypeEnum.CHART)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chart_view, parent, false);

        TextView blockTitle = convertView.findViewById(R.id.block_title_text_view);
        blockTitle.setText(dataBlock.getBlockTitle());

        if(dataBlock.getBlockType() == DataBlock.BlockTypeEnum.VALUE){
        } else if (dataBlock.getBlockType() == DataBlock.BlockTypeEnum.TABLE){
            TableLayout tableLayout = convertView.findViewById(R.id.table_view);
            Log.println(Log.INFO, "Testing", tableLayout.toString());
            ArrayList<ExampleRecord> data = Database.getDataBetween(new Date(), new Date());
            int i = 0;
            for (ExampleRecord rec: data) {
                TableRow tableRow = new TableRow(mContext);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                tableRow.setLayoutParams(lp);

                TextView timestamp = new TextView(mContext);
                timestamp.setText(rec.getTimestamp().toString());

                TextView value = new TextView(mContext);
                value.setText(String.valueOf(rec.getValue()));

                tableRow.addView(timestamp);
                tableRow.addView(value);

                tableLayout.addView(tableRow, i);
                i++;
            }

        } else if (dataBlock.getBlockType() == DataBlock.BlockTypeEnum.CHART){
        }

        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder {

        public ViewHolder(View convertView) {
        }
    }
}
