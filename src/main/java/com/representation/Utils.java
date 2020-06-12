package com.representation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class Utils {
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final int FOLDERPICKER_CODE = 9998;
    public static final int PICKFILE_RESULT_CODE = 9997;
    public static final int WRITE_EXTERNAL_STORAGE_STATUS = 0;
    public static final int READ_EXTERNAL_STORAGE_STATUS = 0;

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

        public static Magnitude fromString(String text) {
            switch(text){
                case "TEMPERATURE":
                    return TEMPERATURE;
                case "HUMIDITY":
                    return HUMIDITY;
                case "PRESSURE":
                    return PRESSURE;
                case "BATTERY_VOLTAGE":
                    return BATTERY_VOLTAGE;
                case "SOLAR_PANEL_VOLTAGE":
                    return SOLAR_PANEL_VOLTAGE;
                case "NODE_VOLTAGE":
                    return NODE_VOLTAGE;
                case "BATTERY_CURRENT":
                    return BATTERY_CURRENT;
                case "SOLAR_PANEL_CURRENT":
                    return SOLAR_PANEL_CURRENT;
                case "NODE_CURRENT":
                    return NODE_CURRENT;
                case "VOLTAGE":
                    return VOLTAGE;
                case "CURRENT":
                    return CURRENT;
            }
            return UNDEFINED;

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

        public static Unit fromString(String text){
            switch(text){
                case "CELSIUS":
                    return CELSIUS;
                case "KELWIN":
                    return KELWIN;
                case "FAHRENHEIT":
                    return FAHRENHEIT;
                case "PERCENT":
                    return PERCENT;
                case "HECTO_PASCAL":
                    return HECTO_PASCAL;
                case "PASCAL":
                    return PASCAL;
                case "KILO_PASCAL":
                    return KILO_PASCAL;
                case "MEGA_PASCAL":
                    return MEGA_PASCAL;
                case "ATMOSPHERE":
                    return ATMOSPHERE;
                case "BAR":
                    return BAR;
                case "VOLT":
                    return VOLT;
                case "MILLI_VOLT":
                    return MILLI_VOLT;
                case "KILO_VOLT":
                    return KILO_VOLT;
                case "AMPERE":
                    return AMPERE;
                case "MILLI_AMPERE":
                    return MILLI_AMPERE;
                case "KILO_AMPERE":
                    return KILO_AMPERE;
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
    public enum BlockTypeEnum { VALUE(0), TABLE(1), CHART(2), CHARTTWO(3), UNDEFINED(-1);
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

        public static BlockTypeEnum fromString(String text){
            switch(text){
                case "VALUE":
                    return VALUE;
                case "TABLE":
                    return TABLE;
                case "CHART":
                    return CHART;
                case "CHARTTWO":
                    return CHARTTWO;
            }
            return UNDEFINED;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        // Applies dark overlay to the background of given view
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void clearDim(@NonNull ViewGroup parent) {
        // removes dark overlay from background
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }
}
