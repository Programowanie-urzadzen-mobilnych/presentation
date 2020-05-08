package layouteditor;

import android.util.Log;

import com.representation.Utils;

import java.util.Calendar;
import java.util.Date;

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
        this.dateStart = Calendar.getInstance().getTime();
        this.dateEnd = Calendar.getInstance().getTime();
    }

    public DataBlock(String blockTitle, Utils.BlockTypeEnum blockType) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
        this.magnitude = Utils.Magnitude.TEMPERATURE;
        this.unit = Utils.Unit.KELWIN;
        this.dateStart = Calendar.getInstance().getTime();
        this.dateEnd = Calendar.getInstance().getTime();
    }

    public DataBlock(String blockTitle, Utils.BlockTypeEnum blockType, Utils.Magnitude magnitude, Utils.Unit unit) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
        this.magnitude = magnitude;
        this.unit = unit;
        this.dateStart = Calendar.getInstance().getTime();
        this.dateEnd = Calendar.getInstance().getTime();
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
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void displayContent() {
        Log.println(Log.ERROR, "DataBlock", "content");
        Log.println(Log.INFO, "DataBlock", "blockTitle: " + blockTitle + "\n" +
                "blockType: " + blockType.name() + "\n" +
                "magnitude: " + magnitude.name() + "\n" +
                "unit: " + unit.name() + "\n" +
                "dateStart: " + dateStart.toString() + "\n" +
                "dateEnd: " + dateEnd.toString());
    }
}
