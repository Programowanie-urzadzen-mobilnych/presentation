package layouteditor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.representation.R;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private int itemPosition;
    private Context layoutEditorContext;
    private DataBlockAdapter cos;

    public CustomOnItemSelectedListener(int itemPosition, Context layoutEditorContext, DataBlockAdapter context){
        this.itemPosition = itemPosition;
        this.layoutEditorContext = layoutEditorContext;
        this.cos = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int spinnerPosition, long id) {
        if (layoutEditorContext instanceof LayoutEditor) {
            switch(spinnerPosition){
                case 0:
                    ((LayoutEditor)layoutEditorContext).setBlockType(itemPosition, DataBlock.BlockTypeEnum.VALUE);
                    Log.println(Log.INFO, "check it out: ", "============================");
                    cos.notifyDataSetChanged();
                    break;
                case 1:
                    ((LayoutEditor)layoutEditorContext).setBlockType(itemPosition, DataBlock.BlockTypeEnum.TABLE);
                    Log.println(Log.INFO, "check it out: ", "============================");
                    cos.notifyDataSetChanged();
                    break;
                case 2:
                    ((LayoutEditor)layoutEditorContext).setBlockType(itemPosition, DataBlock.BlockTypeEnum.CHART);
                    Log.println(Log.INFO, "check it out: ", "============================");
                    cos.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) { }
}
