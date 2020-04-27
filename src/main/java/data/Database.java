package data;

import java.util.ArrayList;

import layouteditor.DataBlock;
import layouts.DataLayout;

public class Database {
    // this class  will be totally changed. Right now it is a simulation of Database.
    // Public static fields from this class are representation for database data.
    // In the future there will be methods to collect data from database or to push it there.
    public static ArrayList<DataLayout> layouts;
    public static void initalizeDatabase() {
        layouts = new ArrayList<>();
        ArrayList<DataBlock> dataBlocks = new ArrayList<>();
        dataBlocks.add(new DataBlock("Blok pierwszy", DataBlock.BlockTypeEnum.VALUE));
        dataBlocks.add(new DataBlock("Blok drugi", DataBlock.BlockTypeEnum.CHART));
        dataBlocks.add(new DataBlock("Blok trzeci", DataBlock.BlockTypeEnum.TABLE));

        layouts.add(new DataLayout("Tytuł pierwszy"));
        layouts.add(new DataLayout("Tytuł drugi", dataBlocks, false, false));
        layouts.add(new DataLayout("Tytuł trzeci", dataBlocks, true, true, true));

        dataBlocks.add(new DataBlock("Blok czwarty", DataBlock.BlockTypeEnum.CHART));
        dataBlocks.add(new DataBlock("Blok piąty", DataBlock.BlockTypeEnum.VALUE));
        layouts.add(new DataLayout("Tytuł czwarty", dataBlocks, false, false, true));
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
}
