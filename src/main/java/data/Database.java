package data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import layouteditor.DataBlock;
import layouts.DataLayout;

public class Database {
    // this class  will be totally changed. Right now it is a simulation of Database.
    // Public static fields from this class are representation for database data.
    // In the future there will be methods to collect data from database or to push it there.
    public static ArrayList<DataLayout> layouts;
    public static ArrayList<ExampleRecord> exampleData;
    public static void initalizeDatabase() {
        layouts = new ArrayList<>();
        ArrayList<DataBlock> dataBlocks = new ArrayList<>();
        dataBlocks.add(new DataBlock("Blok pierwszy", DataBlock.BlockTypeEnum.VALUE));
        dataBlocks.add(new DataBlock("Blok drugi", DataBlock.BlockTypeEnum.CHART));
        dataBlocks.add(new DataBlock("Blok trzeci", DataBlock.BlockTypeEnum.TABLE));

        layouts.add(new DataLayout("Tytuł pierwszy"));
        layouts.add(new DataLayout("Tytuł drugi", dataBlocks, true, false));
        layouts.add(new DataLayout("Przykładowy tytuł układu", dataBlocks, false, true, true));

        dataBlocks.add(new DataBlock("Blok czwarty", DataBlock.BlockTypeEnum.CHART));
        dataBlocks.add(new DataBlock("Blok piąty", DataBlock.BlockTypeEnum.VALUE));
        layouts.add(new DataLayout("Tytuł czwarty", dataBlocks, false, false, true));

        exampleData = new ArrayList<>();
        exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), DataBlock.Magnitude.BATTERY_CURRENT, DataBlock.Unit.AMPERE, 10.2));
        exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), DataBlock.Magnitude.TEMPERATURE, DataBlock.Unit.CELSIUS, 22.5));
        exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), DataBlock.Magnitude.BATTERY_VOLTAGE, DataBlock.Unit.MILLI_VOLT, 100.22));
        exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), DataBlock.Magnitude.PRESSURE, DataBlock.Unit.HECTO_PASCAL, 1020.3));
        exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), DataBlock.Magnitude.PRESSURE, DataBlock.Unit.HECTO_PASCAL, 1020));
        exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), DataBlock.Magnitude.PRESSURE, DataBlock.Unit.HECTO_PASCAL, 1024.3));
        exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), DataBlock.Magnitude.PRESSURE, DataBlock.Unit.HECTO_PASCAL, 1019.7));
        exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), DataBlock.Magnitude.PRESSURE, DataBlock.Unit.HECTO_PASCAL, 1021.8));
        exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), DataBlock.Magnitude.PRESSURE, DataBlock.Unit.HECTO_PASCAL, 1020));
    }
    public Database(ArrayList<DataLayout> layouts) {
        Database.layouts = layouts;
    }
    public static void deselectAllLayouts(){
        // Deselect all layouts
        for (int i = 0; i < layouts.size(); i++) {
            layouts.get(i).setSelected(false);
        }
    }

    public static void undefaultAllLayouts() {
        // Undefault all layouts
        for (int i = 0; i < layouts.size(); i++) {
            layouts.get(i).setDefaultChoice(false);
        }
    }

    public static void selectDefaultLayout(){
        deselectAllLayouts();
        for (int i = 0; i < layouts.size(); i++) {
            if(layouts.get(i).isDefaultChoice()){
                layouts.get(i).setSelected(true);
                return;
            }
        }
    }

    public static ArrayList<ExampleRecord> getDataBetween(Date data1, Date data2) {
        return exampleData;
    }
}
