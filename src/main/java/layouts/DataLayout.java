package layouts;

import android.util.Log;

import java.util.ArrayList;

import layouteditor.DataBlock;

public class DataLayout {
    private String layoutTitle;
    private ArrayList<DataBlock> dataBlocks;
    private boolean selected;
    private boolean defaultChoice;
    private boolean quickMenuElement;

    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks, boolean selected, boolean defaultChoice, boolean quickMenuElement) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = dataBlocks;
        this.selected = selected;
        this.defaultChoice = defaultChoice;
        this.quickMenuElement = quickMenuElement;
    }
    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks, boolean selected, boolean defaultChoice) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = dataBlocks;
        this.selected = selected;
        this.defaultChoice = defaultChoice;
        this.quickMenuElement = false;
    }
    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks, boolean selected) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = dataBlocks;
        this.selected = selected;
        this.defaultChoice = false;
        this.quickMenuElement = false;
    }
    public DataLayout(String layoutTitle, ArrayList<DataBlock> dataBlocks) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = dataBlocks;
        this.selected = false;
        this.defaultChoice = false;
        this.quickMenuElement = false;
    }
    public DataLayout(String layoutTitle) {
        this.layoutTitle = layoutTitle;
        this.dataBlocks = new ArrayList<>();
        this.selected = false;
        this.defaultChoice = false;
        this.quickMenuElement = false;
    }
    public DataLayout() {
        this.layoutTitle = null;
        this.dataBlocks = new ArrayList<>();
        this.selected = false;
        this.defaultChoice = false;
        this.quickMenuElement = false;
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
    public boolean isDefaultChoice() {
        return defaultChoice;
    }
    public void setDefaultChoice(boolean defaultChoice) {
        this.defaultChoice = defaultChoice;
    }
    public boolean isQuickMenuElement() {
        return quickMenuElement;
    }
    public void setQuickMenuElement(boolean quickMenuElement) {
        this.quickMenuElement = quickMenuElement;
    }

    public void displayContent() {
        Log.println(Log.ERROR, "DataLayout", "content");
        Log.println(Log.INFO, "DataLayout", "layoutTitle: " + layoutTitle + "\n" +
                "selected: " + selected + "\n" +
                "defaultChoice: " + defaultChoice + "\n" +
                "quickMenuElement: " + quickMenuElement);
        for (DataBlock dataBlock: dataBlocks) {
            dataBlock.displayContent();
        }
    }
}
