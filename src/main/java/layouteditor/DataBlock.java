package layouteditor;

import android.annotation.SuppressLint;
import android.util.Log;

import com.representation.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

public class DataBlock {
    private String blockTitle;
    private Utils.BlockTypeEnum blockType;
    private Utils.Magnitude magnitude;
    private Utils.Unit unit;
    private Date dateStart;
    private Date dateEnd;

    public DataBlock() {
        this.blockTitle = "";
        this.blockType = Utils.BlockTypeEnum.VALUE;
        this.magnitude = Utils.Magnitude.TEMPERATURE;
        this.unit = Utils.Unit.CELSIUS;
        this.dateStart = new Date();
        this.dateEnd = new Date();
    }

    public DataBlock(String blockTitle, Utils.BlockTypeEnum blockType) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
        this.magnitude = Utils.Magnitude.TEMPERATURE;
        this.unit = Utils.Unit.KELWIN;
        if (blockType == Utils.BlockTypeEnum.VALUE) {
            this.dateStart = new Date();
            this.dateEnd = new Date();
        } else {
            this.dateStart = Calendar.getInstance().getTime();
            this.dateEnd = Calendar.getInstance().getTime();
        }
    }

    public DataBlock(String blockTitle, Utils.BlockTypeEnum blockType, Utils.Magnitude magnitude, Utils.Unit unit) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
        this.magnitude = magnitude;
        this.unit = unit;
        if (blockType == Utils.BlockTypeEnum.VALUE) {
            this.dateStart = new Date();
            this.dateEnd = new Date();
        } else {
            this.dateStart = Calendar.getInstance().getTime();
            this.dateEnd = Calendar.getInstance().getTime();
        }
    }

    public DataBlock(String blockTitle, Utils.BlockTypeEnum blockType, Utils.Magnitude magnitude, Utils.Unit unit, Date dateStart, Date dateEnd) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
        this.magnitude = magnitude;
        this.unit = unit;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public DataBlock(String dataBlock){
        String[] tempvals = dataBlock.split(";");
        this.blockTitle = tempvals[0];
        this.blockType = Utils.BlockTypeEnum.fromString(tempvals[1]);
        this.magnitude = Utils.Magnitude.fromString(tempvals[2]);
        this.unit = Utils.Unit.fromString(tempvals[3]);
        try {
            this.dateStart = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(tempvals[4]);
        } catch (ParseException e) {
            this.dateStart = new Date();
        }
        try {
            this.dateEnd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(tempvals[5]);
        } catch (ParseException e) {
            this.dateEnd = new Date();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return blockTitle+";"+blockType.toString()+";"+magnitude.toString()+";"+unit.toString()+";"+dateStart.toString()+";"+dateEnd.toString();
    }

    public String getBlockTitle() {
        return blockTitle;
    }

    public void setBlockTitle(String blockTitle) {
        this.blockTitle = blockTitle;
    }

    public Utils.BlockTypeEnum getBlockType() {
        return blockType;
    }

    public void setBlockType(Utils.BlockTypeEnum blockType) {
        this.blockType = blockType;
    }

    public Utils.Magnitude getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Utils.Magnitude magnitude) {
        this.magnitude = magnitude;
    }

    public Utils.Unit getUnit() {
        return unit;
    }

    public void setUnit(Utils.Unit unit) {
        this.unit = unit;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(dateStart);
        this.dateStart = newCalendar.getTime();
    }

    public void setDateStart(Date dateStart, boolean isDate) {
        Calendar newCalendar = Calendar.getInstance();
        Calendar oldCalendar = Calendar.getInstance();

        newCalendar.setTime(dateStart);
        oldCalendar.setTime(this.dateStart);

        if(isDate){
            Log.println(Log.INFO, "setDateStart", "isDate = true");
            newCalendar.set(newCalendar.get(Calendar.YEAR),
                    newCalendar.get(Calendar.MONTH),
                    newCalendar.get(Calendar.DAY_OF_MONTH),
                    oldCalendar.get(Calendar.HOUR_OF_DAY),
                    oldCalendar.get(Calendar.MINUTE),
                    oldCalendar.get(Calendar.SECOND));
        } else {
            Log.println(Log.INFO, "setDateStart", "isDate = false");
            newCalendar.set(oldCalendar.get(Calendar.YEAR),
                    oldCalendar.get(Calendar.MONTH),
                    oldCalendar.get(Calendar.DAY_OF_MONTH),
                    newCalendar.get(Calendar.HOUR_OF_DAY),
                    newCalendar.get(Calendar.MINUTE),
                    newCalendar.get(Calendar.SECOND));
        }

        Log.println(Log.ERROR, "setDateStart", "date = " + newCalendar.getTime());
        this.dateStart = newCalendar.getTime();
    }

    public void setDateStart(String dateStart) {
        try {
            Date date = new SimpleDateFormat(Utils.DATETIME_FORMAT, Locale.getDefault()).parse(dateStart);
            Log.println(Log.ERROR, "Error", "date Start: " + dateStart);
            setDateStart(date);
        } catch (ParseException e) {
            Log.println(Log.ERROR, "setDateStart", "Error while parsing date string");
        }
    }

    public void setDateStart(String dateStart, boolean isDate) {
        try {
            Date date = new SimpleDateFormat(Utils.DATETIME_FORMAT, Locale.getDefault()).parse(dateStart);
            Log.println(Log.ERROR, "Error", "date Start: " + dateStart);
            setDateStart(date, isDate);
        } catch (ParseException e) {
            Log.println(Log.ERROR, "setDateStart", "Error while parsing date string");
        }
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(dateEnd);
        this.dateEnd = newCalendar.getTime();
    }

    public void setDateEnd(Date dateEnd, boolean isDate) {
        Calendar newCalendar = Calendar.getInstance();
        Calendar oldCalendar = Calendar.getInstance();

        newCalendar.setTime(dateEnd);
        oldCalendar.setTime(this.dateEnd);

        if (isDate){
            Log.println(Log.INFO, "setDateEnd", "isDate = true");
            newCalendar.set(newCalendar.get(Calendar.YEAR),
                    newCalendar.get(Calendar.MONTH),
                    newCalendar.get(Calendar.DAY_OF_MONTH),
                    oldCalendar.get(Calendar.HOUR_OF_DAY),
                    oldCalendar.get(Calendar.MINUTE),
                    oldCalendar.get(Calendar.SECOND));
        } else {
            Log.println(Log.INFO, "setDateEnd", "isDate = false");
            newCalendar.set(oldCalendar.get(Calendar.YEAR),
                    oldCalendar.get(Calendar.MONTH),
                    oldCalendar.get(Calendar.DAY_OF_MONTH),
                    newCalendar.get(Calendar.HOUR_OF_DAY),
                    newCalendar.get(Calendar.MINUTE),
                    newCalendar.get(Calendar.SECOND));
        }

        Log.println(Log.ERROR, "setDateStart", "date = " + newCalendar.getTime());
        this.dateEnd = newCalendar.getTime();
    }

    public void setDateEnd(String dateEnd, boolean isDate) {
        try {
            Date date = new SimpleDateFormat(Utils.DATETIME_FORMAT, Locale.getDefault()).parse(dateEnd);
            setDateEnd(date, isDate);
        } catch (ParseException e) {
            Log.println(Log.ERROR, "setDateEnd", "Error while parsing date string");
        }
    }

    public void setDateEnd(String dateEnd) {
        try {
            Date date = new SimpleDateFormat(Utils.DATETIME_FORMAT, Locale.getDefault()).parse(dateEnd);
            setDateEnd(date);
        } catch (ParseException e) {
            Log.println(Log.ERROR, "setDateEnd", "Error while parsing date string");
        }
    }

    public void displayContent() {
        Log.println(Log.INFO, "DataBlock", "blockTitle: " + blockTitle + "\n" +
                "blockType: " + blockType.name() + "\n" +
                "magnitude: " + magnitude.name() + "\n" +
                "unit: " + unit.name() + "\n" +
                "dateStart: " + dateStart.toString() + "\n" +
                "dateEnd: " + dateEnd.toString());
    }
}
