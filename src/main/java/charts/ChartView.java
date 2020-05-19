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
                chart.setTouchEnabled(true);
                chart.setPinchZoom(true);
                chart.getDescription().setEnabled(false);
                chart.getAxisRight().setEnabled(false);
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.setExtraBottomOffset(10);
                barSet.setColors(ColorTemplate.MATERIAL_COLORS);

                BarData data = new BarData(barSet);

                if (chartObj.getType() == 1) {
                    chart.getXAxis().setGranularityEnabled(true);
                    chart.getXAxis().setGranularity(1.0f);
                    chart.getXAxis().setLabelCount(data.getEntryCount());
                    ArrayList<String> labels = chartObj.getDatesToString();
                    chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                }

                chart.setData(data);
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
                chart.getDescription().setEnabled(false);
                chart.getAxisRight().setEnabled(false);
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.setExtraBottomOffset(10);

                lineSet.setColors(ColorTemplate.LIBERTY_COLORS);
                lineSet.setHighlightEnabled(true);
                lineSet.setLineWidth(2);
                lineSet.setColor(Color.BLUE);
                lineSet.setCircleColor(Color.CYAN);
                lineSet.setCircleRadius(6);
                lineSet.setCircleHoleRadius(3);
                lineSet.setDrawHighlightIndicators(true);
                lineSet.setHighLightColor(Color.RED);
                lineSet.setValueTextSize(12);
                lineSet.setValueTextColor(Color.BLACK);
                LineData lineData = new LineData(lineSet);

                if (chartObj.getType() == 3) {
                    chart.getXAxis().setGranularityEnabled(true);
                    chart.getXAxis().setGranularity(1.0f);
                    chart.getXAxis().setLabelCount(lineData.getEntryCount());
                    ArrayList<String> labels = chartObj.getDatesToString();
                    chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                }

                chart.setData(lineData);
                chart.animateY(2000);
                chart.invalidate();
            } else {
                layoutInflater.inflate(R.layout.chart_no_data, this);
            }
        }
    }
}