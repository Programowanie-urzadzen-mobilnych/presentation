package measurements;

import com.representation.Utils;

import java.text.DecimalFormat;

public class RecalculateValue {
    public static DecimalFormat df = new DecimalFormat("#.00");
    public static String recalculate(Utils.Unit unit, double value) {
        switch (unit.magnitude()) {
            case TEMPERATURE:
                return recalculateTemperature(unit, value);
            case HUMIDITY:
                return df.format(value);
            case PRESSURE:
                return recalculatePressure(unit, value);
            case VOLTAGE:
                return recalculateVoltage(unit, value);
            case CURRENT:
                return recalculateCurrent(unit, value);
            default:
                return df.format(value);
        }
    }

    private static String recalculateCurrent(Utils.Unit unit, double value) {
        switch(unit){
            case MILLI_AMPERE:
                return df.format(value * 1000);
            case KILO_AMPERE:
                return df.format(value / 1000);
            default:
                return df.format(value);
        }
    }
    private static String recalculateVoltage(Utils.Unit unit, double value) {
        switch(unit){
            case MILLI_VOLT:
                return df.format(value * 1000);
            case KILO_VOLT:
                return df.format(value / 1000);
            default:
                return df.format(value);
        }
    }
    private static String recalculatePressure(Utils.Unit unit, double value) {
        switch(unit){
            case PASCAL:
                return df.format(value * 100);
            case KILO_PASCAL:
                return df.format(value / 10);
            case MEGA_PASCAL:
                return df.format(value / 10000);
            case ATMOSPHERE:
                return df.format(value * 0.000987);
            case BAR:
                return df.format(value / 1000);
            default:
                return df.format(value);
        }
    }
    private static String recalculateTemperature(Utils.Unit unit, double value) {
        switch(unit){
            case KELWIN:
                return df.format(value + 273.15);
            case FAHRENHEIT:
                return df.format((value * 1.8) + 32);
            default:
                return df.format(value);
        }
    }
}
