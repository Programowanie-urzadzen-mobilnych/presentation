package layouteditor;

public class DataBlock {
    private String blockTitle;
    private BlockTypeEnum blockType;
    private Magnitude magnitude;
    private Unit unit;

    public enum Magnitude { UNDEFINED(0), TEMPERATURE(1), HUMIDITY(2), PRESSURE(3), BATTERY_VOLTAGE(4),
        SOLAR_PANEL_VOLTAGE(5), NODE_VOLTAGE(6), BATTERY_CURRENT(7), SOLAR_PANEL_CURRENT(8), NODE_CURRENT(9);
        private int mValue;
        Magnitude(int value) { this.mValue = value;} // Constructor
        public int id(){return mValue;}                  // Return enum index

        public static Magnitude fromId(int value) {
            for(Magnitude magnitude : values()) {
                if (magnitude.mValue == value) {
                    return magnitude;
                }
            }
            return UNDEFINED;
        }}
    public enum Unit { CELSIUS, KELWIN, FAHRENHEIT, PERCENT, PASCAL, HECTO_PASCAL, KILO_PASCAL,
        MEGA_PASCAL, BAR, ATMOSPHERE, VOLT, MILLI_VOLT, KILO_VOLT, AMPERE, MILLI_AMPERE, KILO_AMPERE }
    public enum BlockTypeEnum { VALUE, TABLE, CHART }

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
