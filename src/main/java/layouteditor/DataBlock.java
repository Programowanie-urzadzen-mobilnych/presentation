package layouteditor;

public class DataBlock {
    private String blockTitle;
    private BlockTypeEnum blockType;
    public enum BlockTypeEnum { VALUE, TABLE, CHART }

    public DataBlock() {
        this.blockTitle = "";
        this.blockType = BlockTypeEnum.VALUE;
    }

    public DataBlock(String blockTitle, BlockTypeEnum blockType) {
        this.blockTitle = blockTitle;
        this.blockType = blockType;
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
}
