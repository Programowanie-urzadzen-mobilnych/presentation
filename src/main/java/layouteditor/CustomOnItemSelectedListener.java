package layouteditor;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.representation.Utils;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private int itemPosition;
    private Context layoutEditorContext;
    private DataBlockAdapter dataBlockAdapter;

    public CustomOnItemSelectedListener(int itemPosition, Context layoutEditorContext, DataBlockAdapter adapter){
        this.itemPosition = itemPosition;
        this.layoutEditorContext = layoutEditorContext;
        this.dataBlockAdapter = adapter;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int spinnerPosition, long id) {
        if (layoutEditorContext instanceof LayoutEditor) {
            switch(spinnerPosition){
                case 0:
                    ((LayoutEditor)layoutEditorContext).setBlockType(itemPosition, Utils.BlockTypeEnum.VALUE);
                    dataBlockAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    ((LayoutEditor)layoutEditorContext).setBlockType(itemPosition, Utils.BlockTypeEnum.TABLE);
                    dataBlockAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    ((LayoutEditor)layoutEditorContext).setBlockType(itemPosition, Utils.BlockTypeEnum.CHART);
                    dataBlockAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) { }
}
