package layouteditor;

import com.representation.R;

public class DataBlock {
    private String blockTitle;
    private BlockTypeEnum blockType;
    private Magnitude magnitude;
    private Unit unit;

    // Never change enum values unless you change strings array items order
    public enum Magnitude { TEMPERATURE(0), HUMIDITY(1), PRESSURE(2), BATTERY_VOLTAGE(3), SOLAR_PANEL_VOLTAGE(4),
        NODE_VOLTAGE(5), BATTERY_CURRENT(6), SOLAR_PANEL_CURRENT(7), NODE_CURRENT(8), UNDEFINED(-1);
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
    public enum Unit { CELSIUS(0), KELWIN(1), FAHRENHEIT(2), PERCENT(0),
        PASCAL(0), HECTO_PASCAL(1), KILO_PASCAL(2), MEGA_PASCAL(3),
        BAR(4), ATMOSPHERE(5), VOLT(0), MILLI_VOLT(1), KILO_VOLT(2),
        AMPERE(0), MILLI_AMPERE(1), KILO_AMPERE(2), UNDEFINED(-1);
        private int mValue;
        Unit(int value) { this.mValue = value; }
        public int id(){ return mValue; }

        public static Unit fromId(int value) {
            for(Unit unit : values()) {
                if (unit.mValue == value) {
                    return unit;
                }
            }
            return UNDEFINED;
        } }

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
    }

    public DataBlock(String blockTitle, BlockTypeEnum blockType) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
        this.magnitude = Magnitude.TEMPERATURE;
        this.unit = Unit.CELSIUS;
    }

    public DataBlock(String blockTitle, BlockTypeEnum blockType, Magnitude magnitude, Unit unit) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
        this.magnitude = magnitude;
        this.unit = unit;
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
}
