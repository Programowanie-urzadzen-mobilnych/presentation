package layouteditor;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private int itemPosition;
    private Context mContext;

    public CustomOnItemSelectedListener(int itemPosition, Context context){
        this.itemPosition = itemPosition;
        this.mContext = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int spinnerPosition, long id) {
        if (mContext instanceof LayoutEditor) {
            switch(spinnerPosition){
                case 0:
                    ((LayoutEditor)mContext).setBlockType(itemPosition, DataBlock.BlockTypeEnum.VALUE);
                    break;
                case 1:
                    ((LayoutEditor)mContext).setBlockType(itemPosition, DataBlock.BlockTypeEnum.TABLE);
                    break;
                case 2:
                    ((LayoutEditor)mContext).setBlockType(itemPosition, DataBlock.BlockTypeEnum.CHART);
                    break;
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parentView) { }
}
