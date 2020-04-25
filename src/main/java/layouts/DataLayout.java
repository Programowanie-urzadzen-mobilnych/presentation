package layouts;

import java.util.ArrayList;

import layouteditor.DataBlock;

public class DataLayout {
    private String layoutTitle;
    private ArrayList<DataBlock> dataBlocks;
    private boolean selected;

    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks, boolean selected) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = dataBlocks;
        this.selected = selected;
    }
    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = dataBlocks;
        this.selected = false;
    }
    public DataLayout(String layoutTitle) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = new ArrayList<>();
        this.selected = false;
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
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
