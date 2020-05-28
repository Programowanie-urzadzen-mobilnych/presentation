package charts;

import android.util.Log;

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

@SuppressWarnings("WeakerAccess")
public class ChartObj {
    private ArrayList<Float> valuesX;
    private ArrayList<Float> valuesY;
//    private Date firstDate;
//    private Date lastDate;
    private String firstDateFormatted;
    private String lastDateFormatted;
    private String unitX;
    private String unitY;
    private int type;
    private ArrayList<Date> dates;
    private ArrayList<String> datesToString;
    private ArrayList<Float> XAxisFloatNumbers;

    //type 1 - bar Y-values, X-time
    //type 2 - bar Y-values, X-values
    //type 3 - line Y-values, X-time
    //type 4 - line Y-values, X-values

    //type false - bar
    //type true - line
    public ChartObj(ArrayList<Float> values, Date firstDate, Date lastDate, String unit, boolean type) {
        if (type) this.type = 1;
        else this.type = 3;
//        this.firstDate = firstDate;
//        this.lastDate = lastDate;
        firstDateFormatted = ParseDateToString(firstDate);
        lastDateFormatted = ParseDateToString(lastDate);
        dates = getTimeline(firstDate, lastDate, values.size());
        XAxisFloatNumbers = getXAxisFloatNumbers(dates);
        datesToString = getTimelineToString(dates);
        valuesY = values;
        unitY = unit;
    }

    public ChartObj(ArrayList<Float> valuesY, ArrayList<Float> valuesX, String unitX, String unitY, boolean type) {
        if (type) this.type = 2;
        else this.type = 4;
        this.valuesX = valuesX;
        this.valuesY = valuesY;
        this.unitX = unitX;
        this.unitY = unitY;
    }

    BarDataSet getBarData() {
        //type 1 - bar Y-values, X-time
        if (type == 1) {
            if (!getDates().isEmpty() && !getValuesY().isEmpty() && getDates().size() == getValuesY().size()) {
                List<BarEntry> entries = new ArrayList<>();
                for (int i = 0; i < getDates().size(); i++) {
                    float tempX = XAxisFloatNumbers.get(i);
                    float tempY = valuesY.get(i);
                    entries.add(new BarEntry(tempX, tempY));
                }
                return new BarDataSet(entries, getUnitY() + "   Okres: " + getFirstDate() + " - " + getLastDate());
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
                return new BarDataSet(entries, "Y: " + getUnitY() + "  X: " + getUnitX());
            } else return null;
        } else return null;
    }

    LineDataSet getLineData() {
        //type 3 - line Y-values, X-time
        if (type == 3) {
            if (!getDates().isEmpty() && !getValuesY().isEmpty() && getDates().size() == getValuesY().size()) {
                List<Entry> entries = new ArrayList<>();
                for (int i = 0; i < getDates().size(); i++) {
                    float tempX = XAxisFloatNumbers.get(i);
                    float tempY = valuesY.get(i);
                    entries.add(new Entry(tempX, tempY));
                }
                return new LineDataSet(entries, getUnitY() + "      Okres: " + getFirstDate() + "  --  " + getLastDate());
            } else return null;
        }
        //type 4 - line Y-values, X-values
        else if (type == 4) {
            if (!getValuesX().isEmpty() && !getValuesY().isEmpty() && getValuesX().size() == getValuesY().size()) {
                List<Entry> entries = new ArrayList<>();
                for (int i = 0; i < getValuesX().size(); i++) {
                    float tempX = valuesX.get(i);
                    float tempY = valuesY.get(i);
                    entries.add(new Entry(tempX, tempY));
                }
                return new LineDataSet(entries, "Y: " + getUnitY() + " /  X: " + getUnitX());
            } else return null;
        } else return null;
    }

    String ParseDateToString(Date input) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm  dd.MM.yyyy");
        try {
            return simpleDateFormat.format(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    Date ParseStringToDate(String input) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date output;
        try {
            output = simpleDateFormat.parse(input);
            return output;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<Date> getTimeline(Date DateTime1, Date DateTime2, int numberOfElements) {
        if (numberOfElements != 0) {
            ArrayList<Date> dates = new ArrayList<>();
            if (DateTime1 != null && DateTime2 != null) {
                dates.add(DateTime1);
                if (numberOfElements > 1) {
                    long dateTimeDiff = DateTime2.getTime() - DateTime1.getTime();
                    Long singlePer = dateTimeDiff / (numberOfElements - 1);
                    Long temp = DateTime1.getTime();
                    for (int x = 2; x <= numberOfElements; x++) {
                        if (x == numberOfElements) {
                            dates.add(DateTime2);
                        } else {
                            temp = temp + singlePer;
                            dates.add(new Date(temp));
                        }
                    }
                }
            }
            return dates;
        } else return null;
    }

    ArrayList<String> getTimelineToString(ArrayList<Date> dates) {
        if (dates.size() != 0) {
            ArrayList<String> datesToString = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            for (int i = 0; i < dates.size(); i++) {
                datesToString.add(simpleDateFormat.format(dates.get(i)));
            }
            return datesToString;
        } else return null;
    }

    ArrayList<Float> getXAxisFloatNumbers(ArrayList<Date> dates) {
        ArrayList<Float> temp = new ArrayList<>();
        if (dates.size() != 0) {
            for (int x = 0; x < dates.size(); x++) {
                temp.add((float) x);
            }
            return temp;
        } else return null;
    }

    ArrayList<String> getDatesToString() {
        return datesToString;
    }

    ArrayList<Float> getValuesX() {
        return valuesX;
    }

    ArrayList<Float> getValuesY() {
        return valuesY;
    }

    String getFirstDate() {
        return firstDateFormatted;
    }

    String getLastDate() {
        return lastDateFormatted;
    }

    String getUnitX() {
        return unitX;
    }

    String getUnitY() {
        return unitY;
    }

    int getType() {
        return type;
    }

    ArrayList<Date> getDates() {
        return dates;
    }
}
