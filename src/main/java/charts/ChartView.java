package charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.representation.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressLint("ViewConstructor")
@RequiresApi(api = Build.VERSION_CODES.O)
public class ChartView extends FrameLayout {
    // Log.d("TestActivity", "");
    public ChartView(Context context, ChartObj chartObj) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //type 1 - bar Y-values, X-time
        //type 2 - bar Y-values, X-values
        //type 3 - line Y-values, X-time
        //type 4 - line Y-values, X-values

        if (chartObj.getType() == 1 || chartObj.getType() == 2) {
            BarDataSet barSet = chartObj.getBarData();
            if (barSet != null) {
                layoutInflater.inflate(R.layout.chart_bar, this);
                BarChart chart = findViewById(R.id.chartBar);
                BarData data = new BarData(barSet);
                chart.setData(data);
                Description description = new Description();
                description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
                description.setText(chartObj.getDescription());
                chart.setDescription(description);
                chart.animateXY(2000, 2000);
                chart.invalidate();
            } else {
                layoutInflater.inflate(R.layout.chart_no_data, this);
            }


        } else if (chartObj.getType() == 3 || chartObj.getType() == 4) {
            LineDataSet lineSet = chartObj.getLineData();
            if (lineSet != null) {

                layoutInflater.inflate(R.layout.chart_line, this);
                LineChart chart = findViewById(R.id.chartLine);

                chart.setTouchEnabled(true);
                chart.setPinchZoom(true);
                LineData lineData = new LineData(lineSet);


                Description description = new Description();
                description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[3]);
                description.setText(chartObj.getDescription());
                description.setTextSize(12);
                chart.setDescription(description);


                YAxis rightYAxis = chart.getAxisRight();
                rightYAxis.setEnabled(false);
                XAxis topXAxis = chart.getXAxis();
                topXAxis.setEnabled(false);

                chart.setDrawMarkers(true);
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.getXAxis().setGranularityEnabled(true);
                chart.getXAxis().setGranularity(1.0f);
                chart.getXAxis().setLabelCount(lineData.getEntryCount());
                chart.setData(lineData);

                            ArrayList<String> labels = new ArrayList<String>();
            labels.add("JAN");
            labels.add("FEB");
            labels.add("MAR");
            labels.add("APR");
            labels.add("MAY");
            labels.add("JUN");
            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

                chart.animateY(2000);
                chart.invalidate();
            } else {
                layoutInflater.inflate(R.layout.chart_no_data, this);
            }
        }
    }
}


//            ArrayList<String> labels = new ArrayList<String>();
//            labels.add("JAN");
//            labels.add("FEB");
//            labels.add("MAR");
//            labels.add("APR");
//            labels.add("MAY");
//            labels.add("JUN");
//            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));


//            //limitline
//            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
//            ll1.setLineWidth(4f);
//            ll1.enableDashedLine(10f, 10f, 0f);
//            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//            ll1.setTextSize(10f);
//
//            YAxis leftAxis = barChart.getAxisLeft();
//            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//            leftAxis.addLimitLine(ll1);
//
//            barChart.getAxisLeft().setAxisMinimum(0);
//            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
//

//            ArrayList<String> labels = new ArrayList<String>();
//            labels.add("JAN");
//            labels.add("FEB");
//            labels.add("MAR");
//            labels.add("APR");
//            labels.add("MAY");
//            labels.add("JUN");
//            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
//
//            barChart.animateY(1000);
//
//            barChart.getXAxis().setGranularityEnabled(true);
//            barChart.getXAxis().setGranularity(1.0f);
//            barChart.getXAxis().setLabelCount(barDataSet.getEntryCount());
//
