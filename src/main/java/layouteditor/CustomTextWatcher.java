package layouteditor;

import android.text.Editable;
import android.text.TextWatcher;

public class CustomTextWatcher implements TextWatcher {
    private int itemPosition;

    public CustomTextWatcher (int itemPosition) {
        this.itemPosition = itemPosition;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }
    @Override
    public void afterTextChanged(Editable s) {
        LayoutEditor.layout.getDataBlocks().get(itemPosition).setBlockTitle(s.toString());
    }
}
