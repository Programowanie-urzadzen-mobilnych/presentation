package layouteditor;

import android.view.View;
import android.widget.AdapterView;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private int itemPosition;

    public CustomOnItemSelectedListener(int itemPosition){
        this.itemPosition = itemPosition;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int spinnerPosition, long id) {
        switch(spinnerPosition){
            case 0:
                LayoutEditor.dataBlocks.get(itemPosition).setBlockType(DataBlock.BlockTypeEnum.VALUE);
                break;
            case 1:
                LayoutEditor.dataBlocks.get(itemPosition).setBlockType(DataBlock.BlockTypeEnum.TABLE);
                break;
            case 2:
                LayoutEditor.dataBlocks.get(itemPosition).setBlockType(DataBlock.BlockTypeEnum.CHART);
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parentView) { }
}
