package layouteditor;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

public class CustomTextWatcher implements TextWatcher {
    private int itemPosition;
    private Context mContext;

    public CustomTextWatcher(int itemPosition, Context context) {
        this.itemPosition = itemPosition;
        this.mContext = context;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }
    @Override
    public void afterTextChanged(Editable s) {
        if (mContext instanceof LayoutEditor) {
            ((LayoutEditor)mContext).changeBlockTitle(itemPosition, s.toString());
        }
    }
}
