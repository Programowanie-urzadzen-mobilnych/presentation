package charts;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.representation.R;
import java.util.ArrayList;

public class ChartLine extends FrameLayout {

    public ChartLine(Context context, AttributeSet attrs) {
        super(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.chart_line, this);

        LineChart chart = findViewById(R.id.chartLine);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);


        ArrayList<Entry> lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(0, 1));
        lineEntries.add(new Entry(1, 2));
        lineEntries.add(new Entry(2, 3));
        lineEntries.add(new Entry(3, 4));
        lineEntries.add(new Entry(4, 2));
        lineEntries.add(new Entry(5, 3));
        lineEntries.add(new Entry(6, 1));
        lineEntries.add(new Entry(7, 5));
        lineEntries.add(new Entry(8, 7));
        lineEntries.add(new Entry(9, 6));
        lineEntries.add(new Entry(10, 4));
        lineEntries.add(new Entry(11, 5));


        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Battery Voltage");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setCircleColor(Color.CYAN);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(lineDataSet);

        chart.setDrawMarkers(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.animateY(1000);
        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1.0f);
        chart.getXAxis().setLabelCount(lineDataSet.getEntryCount());


        chart.setData(lineData);
        Description description = new Description();
        description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        description.setText("Chart Description");
        description.setTextSize(12);
        chart.setDescription(description);

        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

}
