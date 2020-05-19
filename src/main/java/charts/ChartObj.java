package charts;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartObj {
    private List<BarEntry> entries;
    private ArrayList<Float> valuesX;
    private ArrayList<Float> valuesY;
    private String FirstDate;
    private String lastDate;
    private String unitX;
    private String unitY;
    private String description;
    private int type;
    private ArrayList<Date> dates;
    //type 1 - bar Y-values, X-time
    //type 2 - bar Y-values, X-values
    //type 3 - line Y-values, X-time
    //type 4 - line Y-values, X-values


    //type false - bar
    //type true - line
    ChartObj(ArrayList<Float> values, String FirstDate, String lastDate, String unit, String description, boolean type) {
        if (type) {
            this.type = 1;
        } else this.type = 3;

        this.dates = getIntervals(FirstDate, lastDate, values.size());
//        for (int i = 0; i < dates.size(); i++) {
//            long tempX = dates.get(i).getTime() / 1000L;
//            this.valuesX.add(tempX);
//        }
        this.valuesY = values;
        this.unitY = unit;
        this.description = description;
    }

    ChartObj(ArrayList<Float> valuesX, ArrayList<Float> valuesY, String unitX, String unitY, String description, boolean type) {
        if (type) {
            this.type = 2;
        } else this.type = 4;

        this.valuesX = valuesX;
        this.valuesY = valuesY;
        this.unitX = unitX;
        this.unitY = unitY;
        this.description = description;
    }


    BarDataSet getBarData() {
        //type 1 - bar Y-values, X-time
        if(type == 1){
            if (!getDates().isEmpty() && !getValuesY().isEmpty() && getDates().size() == getValuesY().size()) {
                List<BarEntry> entries = new ArrayList<>();
                for (int i = 0; i < getDates().size(); i++) {
                    long tempX = getDates().get(i).getTime() / 1000L;
                    float tempY = valuesY.get(i);
                    entries.add(new BarEntry(tempX, tempY));
                }
                BarDataSet set = new BarDataSet(entries, getUnitY());
                set.setColors(ColorTemplate.COLORFUL_COLORS);
                return set;
            } else return null;
        }
        //type 2 - bar Y-values, X-values
        else if (type == 2) {
            if (!getValuesX().isEmpty() && !getValuesY().isEmpty() && getValuesX().size() == getValuesY().size()) {
                List<BarEntry> entries = new ArrayList<>();
                for (int i = 0; i < getValuesX().size(); i++) {
                    float tempX = valuesX.get(i);
                    float tempY = valuesY.get(i);
                    entries.add(new BarEntry(tempX, tempY));
                }
                BarDataSet set = new BarDataSet(entries, "Y: " + getUnitY() + "X: " + getUnitX());
                set.setColors(ColorTemplate.COLORFUL_COLORS);
                return set;
            } else return null;
        }
        else return null;
    }

    LineDataSet getLineData() {
        //type 1 - bar Y-values, X-time
        if(type == 3){
            if (!getDates().isEmpty() && !getValuesY().isEmpty() && getDates().size() == getValuesY().size()) {
                List<Entry> entries = new ArrayList<>();
                for (int i = 0; i < getDates().size(); i++) {
                    long tempX = getDates().get(i).getTime() / 1000L;
                    float tempY = valuesY.get(i);
                    entries.add(new Entry(tempX, tempY));
                }
                LineDataSet set = new LineDataSet(entries, getUnitY());
                set.setColors(ColorTemplate.COLORFUL_COLORS);
//                set.setAxisDependency(YAxis.AxisDependency.LEFT);
//                set.setHighlightEnabled(true);
//                set.setLineWidth(2);
//                set.setColor(Color.BLUE);
//                set.setCircleColor(Color.CYAN);
//                set.setCircleRadius(6);
//                set.setCircleHoleRadius(3);
//                set.setDrawHighlightIndicators(true);
//                set.setHighLightColor(Color.RED);
//                set.setValueTextSize(12);
//                set.setValueTextColor(Color.BLACK);
                return set;
            } else return null;
        }
        //type 2 - bar Y-values, X-values
        else if (type == 4) {
            if (!getValuesX().isEmpty() && !getValuesY().isEmpty() && getValuesX().size() == getValuesY().size()) {
                List<Entry> entries = new ArrayList<>();
                for (int i = 0; i < getValuesX().size(); i++) {
                    float tempX = valuesX.get(i);
                    float tempY = valuesY.get(i);
                    entries.add(new Entry(tempX, tempY));
                }
                LineDataSet set = new LineDataSet(entries, "Y: " + getUnitY() + "X: " + getUnitX());
                set.setColors(ColorTemplate.COLORFUL_COLORS);
                return set;
            } else return null;
        }
        else return null;
    }


    private ArrayList<Date> getIntervals(String date1, String date2, int numberOfElements) {
        if (numberOfElements != 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date DateTime1 = new Date();
            Date DateTime2 = new Date();
            ArrayList<Date> dates = new ArrayList<Date>();
            {
                try {
                    DateTime1 = simpleDateFormat.parse(date1);
                    DateTime2 = simpleDateFormat.parse(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (DateTime1 != null && DateTime2 != null) {
                dates.add(DateTime1);
                if (numberOfElements > 1) {
                    long dateTimeDiff = DateTime2.getTime() / 1000L - DateTime1.getTime() / 1000L;
                    Long singlePer = dateTimeDiff / (numberOfElements - 1);
                    Long temp = DateTime1.getTime() / 1000L;
                    for (int x = 2; x <= numberOfElements; x++) {
                        if (x == numberOfElements) {
                            dates.add(DateTime2);
                        } else {
                            temp = temp + singlePer;
                            dates.add(new java.util.Date((long) temp * 1000));
                        }
                    }
                }

                ArrayList<Long> dates2 = new ArrayList<>();
                for (int i = 0; i < dates.size(); i++) {
                    dates2.add(dates.get(i).getTime() / 100000L - DateTime1.getTime() / 100000L);
                }
                for (int i = 0; i < dates2.size(); i++) {
                    Log.d("TestActivity", "" + dates2.get(i));
                }
            }
            return dates;
        } else return null;
    }



   List<BarEntry> getEntries() {
        return entries;
    }

    ArrayList<Float> getValuesX() {
        return valuesX;
    }

    ArrayList<Float> getValuesY() {
        return valuesY;
    }

    String getFirstDate() {
        return FirstDate;
    }

    String getLastDate() {
        return lastDate;
    }

    String getUnitX() {
        return unitX;
    }

    String getUnitY() {
        return unitY;
    }

    String getDescription() {
        return description;
    }

    int getType() {
        return type;
    }

    ArrayList<Date> getDates() {
        return dates;
    }

}
