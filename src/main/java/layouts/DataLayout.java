package layouts;

import java.util.ArrayList;

import layouteditor.DataBlock;

public class DataLayout {
    private String layoutTitle;
    private ArrayList<DataBlock> dataBlocks;

    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = dataBlocks;
    }

    public String getLayoutTitle() {
        return layoutTitle;
    }

    public void setLayoutTitle(String layoutTitle) {
        this.layoutTitle = layoutTitle;
    }

    public ArrayList<DataBlock> getDataBlocks() {
        return dataBlocks;
    }

    public void setDataBlocks(ArrayList<DataBlock> dataBlocks) {
        this.dataBlocks = dataBlocks;
    }
}
