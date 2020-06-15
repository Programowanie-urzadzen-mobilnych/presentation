package data;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.representation.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        dataBlocks.add(new DataBlock("Blok pierwszy", Utils.BlockTypeEnum.VALUE));
        dataBlocks.add(new DataBlock("Blok drugi", Utils.BlockTypeEnum.CHART));
        dataBlocks.add(new DataBlock("Blok trzeci", Utils.BlockTypeEnum.TABLE));

        layouts.add(new DataLayout("Tytuł pierwszy", new ArrayList<DataBlock>(), false, false, true));
        layouts.add(new DataLayout("Tytuł drugi", new ArrayList<>(dataBlocks), true, false));
        layouts.add(new DataLayout("Przykładowy tytuł układu", new ArrayList<>(dataBlocks), false, false, true));
        dataBlocks.remove(0);

        dataBlocks.add(new DataBlock("Blok czwarty", Utils.BlockTypeEnum.CHART));
        dataBlocks.add(new DataBlock("Blok piąty", Utils.BlockTypeEnum.VALUE));
        layouts.add(new DataLayout("Tytuł czwarty", new ArrayList<>(dataBlocks), false, true, true));

        DateFormat format = new SimpleDateFormat(Utils.DATETIME_FORMAT, Locale.getDefault());

        try {
            exampleData = new ArrayList<>();
            exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), Utils.Magnitude.BATTERY_CURRENT, Utils.Unit.AMPERE, 10.2));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 12:10:00"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 22.50));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 11:21:30"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 21.50));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 10:33:20"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 20.88));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 12:10:10"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 22.77));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 14:37:12"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 23.64));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 16:55:00"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 25.31));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 18:51:00"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 23.12));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 19:00:10"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 22.54));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 10:14:44"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 23.32));
            exampleData.add(new ExampleRecord(format.parse("30.04.2020 11:13:02"), Utils.Magnitude.TEMPERATURE, Utils.Unit.CELSIUS, 20.54));
            exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), Utils.Magnitude.BATTERY_VOLTAGE, Utils.Unit.MILLI_VOLT, 100.22));
            exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), Utils.Magnitude.PRESSURE, Utils.Unit.HECTO_PASCAL, 1020.3));
            exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), Utils.Magnitude.PRESSURE, Utils.Unit.HECTO_PASCAL, 1020));
            exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), Utils.Magnitude.PRESSURE, Utils.Unit.HECTO_PASCAL, 1024.3));
            exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), Utils.Magnitude.PRESSURE, Utils.Unit.HECTO_PASCAL, 1019.7));
            exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), Utils.Magnitude.PRESSURE, Utils.Unit.HECTO_PASCAL, 1021.8));
            exampleData.add(new ExampleRecord(Calendar.getInstance().getTime(), Utils.Magnitude.PRESSURE, Utils.Unit.HECTO_PASCAL, 1020));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        // deselectAllLayouts();
        for (int i = 0; i < layouts.size(); i++) {
            if(layouts.get(i).isDefaultChoice()){
                layouts.get(i).setSelected(true);
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<ExampleRecord> getDataBetween(Date data1, Date data2, final Utils.Magnitude magnitude) {
        //TODO: Filter database to return only data between time, and data of given Magnitude
        //ArrayList<ExampleRecord> result = (ArrayList<ExampleRecord>) exampleData.stream().filter(rec -> rec.getMagnitude() == magnitude).collect(Collectors.toList());
        ArrayList<ExampleRecord> result = new ArrayList<>();
        for (ExampleRecord rec: exampleData) {
            if(rec.getMagnitude()==magnitude)
                result.add(rec);
        }

        return result;
    }

    public static ExampleRecord getLatestValue(Utils.Magnitude magnitude) {
        //TODO: Return latest data of given magnitude

        return exampleData.get(0);
    }
}
