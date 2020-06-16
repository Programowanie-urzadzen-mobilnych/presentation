package data;

import com.representation.Utils;

import java.util.Date;

public class ExampleRecord {
    private Date timestamp;
    private Utils.Magnitude magnitude;
    private Utils.Unit unit;
    private double value;

    public ExampleRecord(){
        this.timestamp = new Date();
        this.magnitude = Utils.Magnitude.TEMPERATURE;
        this.unit = Utils.Unit.CELSIUS;
        this.value = 0;
    }
    public ExampleRecord(Date date, Utils.Magnitude magnitude, Utils.Unit unit, double value) {
        this.timestamp = date;
        this.magnitude = magnitude;
        this.unit = unit;
        this.value = value;
    }

    public String toString() {
        return value+" "+unit.toString()+" "+timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
