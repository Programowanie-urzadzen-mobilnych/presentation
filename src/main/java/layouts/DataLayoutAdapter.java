package layouts;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

class DataLayoutAdapter extends ArrayAdapter<DataLayout> {
    public DataLayoutAdapter(Context context, ArrayList<DataLayout> layouts) {
        super(context, 0, layouts);
    }
}
