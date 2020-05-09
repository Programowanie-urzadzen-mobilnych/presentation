package com.representation;

import android.content.Context;
import android.util.Log;

public class Utils {
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final int FOLDERPICKER_CODE = 9998;

    // Never change enum values unless you change strings array items order
    public enum Magnitude {
        TEMPERATURE(0),
        HUMIDITY(1),
        PRESSURE(2),
        BATTERY_VOLTAGE(3),
        SOLAR_PANEL_VOLTAGE(4),
        NODE_VOLTAGE(5),
        BATTERY_CURRENT(6),
        SOLAR_PANEL_CURRENT(7),
        NODE_CURRENT(8),
        VOLTAGE(9),
        CURRENT(10), 
        UNDEFINED(-1);

        private int mValue;
        private String mStr;

        Magnitude(int value) {
            this.mValue = value;
        }
        public int id(){
            return mValue;
        }

        public static Magnitude fromId(int value) {
            for(Magnitude magnitude : values()) {
                if (magnitude.mValue == value) {
                    return magnitude;
                }
            }
            return UNDEFINED;
        }
        public static String getName(Context con, Magnitude m){
            try {
                String[] array = con.getResources().getStringArray(R.array.MAGNITUDES_SPINNER);
                return array[m.mValue];
            } catch (Exception e) { }
            return null;
        }
    }

    // Never change enum values unless you change strings array items order
    public enum Unit {
        CELSIUS(0, Magnitude.TEMPERATURE),
        KELWIN(1, Magnitude.TEMPERATURE),
        FAHRENHEIT(2, Magnitude.TEMPERATURE),
        PERCENT(0, Magnitude.HUMIDITY),
        HECTO_PASCAL(0, Magnitude.PRESSURE),
        PASCAL(1, Magnitude.PRESSURE),
        KILO_PASCAL(2, Magnitude.PRESSURE),
        MEGA_PASCAL(3, Magnitude.PRESSURE),
        ATMOSPHERE(4, Magnitude.PRESSURE),
        BAR(5, Magnitude.PRESSURE),
        VOLT(0, Magnitude.VOLTAGE),
        MILLI_VOLT(1, Magnitude.VOLTAGE),
        KILO_VOLT(2, Magnitude.VOLTAGE),
        AMPERE(0, Magnitude.CURRENT),
        MILLI_AMPERE(1, Magnitude.CURRENT),
        KILO_AMPERE(2, Magnitude.CURRENT),
        UNDEFINED(-1, Magnitude.UNDEFINED);

        private int mValue;
        private Magnitude mMagnitude;

        Unit(int value, Magnitude magnitude) {
            this.mValue = value;
            this.mMagnitude = magnitude;}
        public int id(){ return mValue; }
        public Magnitude magnitude(){ return mMagnitude; }

        public static Unit fromId(int value, Magnitude magnitude) {
            for(Unit unit : values()) {
                if (unit.mValue == value) {
                    Magnitude temp;
                    if(magnitude == Magnitude.BATTERY_VOLTAGE ||
                            magnitude == Magnitude.NODE_VOLTAGE ||
                            magnitude == Magnitude.SOLAR_PANEL_VOLTAGE){
                        temp = Magnitude.VOLTAGE;
                    } else if(magnitude == Magnitude.BATTERY_CURRENT ||
                            magnitude == Magnitude.NODE_CURRENT ||
                            magnitude == Magnitude.SOLAR_PANEL_CURRENT){
                        temp = Magnitude.CURRENT;
                    } else {
                        temp = magnitude;
                    }
                    if(unit.mMagnitude == temp){
                        return unit;
                    }
                }
            }
            return UNDEFINED;
        }
        public static String getName(Context con, Unit u){
            String[] array;
            switch (u.magnitude()) {
                case TEMPERATURE:
                    array = con.getResources().getStringArray(R.array.TEMPERATURE_SPINNER);
                    return array[u.mValue];
                case HUMIDITY:
                    array = con.getResources().getStringArray(R.array.HUMIDITY_SPINNER);
                    return array[u.mValue];
                case PRESSURE:
                    array = con.getResources().getStringArray(R.array.PRESSURE_SPINNER);
                    return array[u.mValue];
                case VOLTAGE:
                    array = con.getResources().getStringArray(R.array.VOLTAGE_SPINNER);
                    return array[u.mValue];
                case CURRENT:
                    array = con.getResources().getStringArray(R.array.CURRENT_SPINNER);
                    return array[u.mValue];
                default:
                    return null;
            }
        }
    }

    // Never change enum values unless you change strings array items order
    public enum BlockTypeEnum { VALUE(0), TABLE(1), CHART(2), UNDEFINED(-1);
        private int mValue;
        BlockTypeEnum(int value) { this.mValue = value; }
        public int id(){ return mValue; }

        public static BlockTypeEnum fromId(int value) {
            for(BlockTypeEnum blockTypeEnum : values()) {
                if (blockTypeEnum.mValue == value) {
                    return blockTypeEnum;
                }
            }
            return UNDEFINED;
        }
    }
}
