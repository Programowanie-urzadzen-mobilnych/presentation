package layouteditor;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import com.representation.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomTimePickerDialog implements TimePickerDialog.OnTimeSetListener {
    private Context layoutEditorContext;
    private int itemPosition;
    private DialogType dialogType;

    public enum DialogType { END, START }

    public CustomTimePickerDialog(Context layoutEditorContext, int itemPosition, DialogType dialogType) {
        this.layoutEditorContext = layoutEditorContext;
        this.itemPosition = itemPosition;
        this.dialogType = dialogType;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (layoutEditorContext instanceof LayoutEditor) {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat(Utils.TIME_FORMAT, Locale.getDefault());
                Date date = timeFormat.parse(hourOfDay + ":" + minute + ":" + 0);
                if(dialogType == DialogType.START)
                    ((LayoutEditor) layoutEditorContext).setStartDateTimeText(date, itemPosition);
                else if (dialogType == DialogType.END)
                    ((LayoutEditor) layoutEditorContext).setEndDateTimeText(date, itemPosition);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
