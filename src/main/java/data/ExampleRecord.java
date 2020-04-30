package data;

import java.util.Date;

import layouteditor.DataBlock;

public class ExampleRecord {
    private Date timestamp;
    private DataBlock.Magnitude magnitude;
    private DataBlock.Unit unit;
    private double value;

    public ExampleRecord(){
        this.timestamp = new Date();
        this.magnitude = DataBlock.Magnitude.TEMPERATURE;
        this.unit = DataBlock.Unit.CELSIUS;
        this.value = 0;
    }
    public ExampleRecord(Date date, DataBlock.Magnitude magnitude, DataBlock.Unit unit, double value) {
        this.timestamp = date;
        this.magnitude = magnitude;
        this.unit = unit;
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public DataBlock.Magnitude getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(DataBlock.Magnitude magnitude) {
        this.magnitude = magnitude;
    }

    public DataBlock.Unit getUnit() {
        return unit;
    }

    public void setUnit(DataBlock.Unit unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
