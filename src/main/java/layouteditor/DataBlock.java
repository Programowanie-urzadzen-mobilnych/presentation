package layouteditor;

import android.util.Log;

import com.representation.R;

import java.util.Calendar;
import java.util.Date;

import javax.xml.transform.Templates;

public class DataBlock {
    private String blockTitle;
    private BlockTypeEnum blockType;
    private Magnitude magnitude;
    private Unit unit;
    private Date dateStart;
    private Date dateEnd;


    // Never change enum values unless you change strings array items order
    public enum Magnitude { TEMPERATURE(0), HUMIDITY(1), PRESSURE(2), BATTERY_VOLTAGE(3), SOLAR_PANEL_VOLTAGE(4),
        NODE_VOLTAGE(5), BATTERY_CURRENT(6), SOLAR_PANEL_CURRENT(7), NODE_CURRENT(8), VOLTAGE(9), CURRENT(10), UNDEFINED(-1);
        private int mValue;
        Magnitude(int value) { this.mValue = value; }
        public int id(){ return mValue; }

        public static Magnitude fromId(int value) {
            for(Magnitude magnitude : values()) {
                if (magnitude.mValue == value) {
                    return magnitude;
                }
            }
            return UNDEFINED;
        }}

    // Never change enum values unless you change strings array items order
    public enum Unit {
        CELSIUS(0, Magnitude.TEMPERATURE),
        KELWIN(1, Magnitude.TEMPERATURE),
        FAHRENHEIT(2, Magnitude.TEMPERATURE),
        PERCENT(0, Magnitude.HUMIDITY),
        PASCAL(0, Magnitude.PRESSURE),
        HECTO_PASCAL(1, Magnitude.PRESSURE),
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
        }}

    public DataBlock() {
        this.blockTitle = "";
        this.blockType = BlockTypeEnum.VALUE;
        this.magnitude = Magnitude.TEMPERATURE;
        this.unit = Unit.CELSIUS;
        this.dateStart = Calendar.getInstance().getTime();
        this.dateEnd = Calendar.getInstance().getTime();
    }

    public DataBlock(String blockTitle, BlockTypeEnum blockType) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
        this.magnitude = Magnitude.TEMPERATURE;
        this.unit = Unit.CELSIUS;
        this.dateStart = Calendar.getInstance().getTime();
        this.dateEnd = Calendar.getInstance().getTime();
    }

    public DataBlock(String blockTitle, BlockTypeEnum blockType, Magnitude magnitude, Unit unit) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
        this.magnitude = magnitude;
        this.unit = unit;
        this.dateStart = Calendar.getInstance().getTime();
        this.dateEnd = Calendar.getInstance().getTime();
    }

    public String getBlockTitle() {
        return blockTitle;
    }

    public void setBlockTitle(String blockTitle) {
        this.blockTitle = blockTitle;
    }

    public BlockTypeEnum getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockTypeEnum blockType) {
        this.blockType = blockType;
    }

    public Magnitude getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Magnitude magnitude) {
        this.magnitude = magnitude;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
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
