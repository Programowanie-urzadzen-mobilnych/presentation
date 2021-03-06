package layouteditor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;

import com.representation.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomDatePickerDialog implements DatePickerDialog.OnDateSetListener {
    private Context layoutEditorContext;
    private int itemPosition;
    private DialogType dialogType;

    public enum DialogType { END, START }

    public CustomDatePickerDialog(Context layoutEditorContext, int itemPosition, DialogType dialogType) {
        this.layoutEditorContext = layoutEditorContext;
        this.itemPosition = itemPosition;
        this.dialogType = dialogType;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (layoutEditorContext instanceof LayoutEditor) {
            try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.DATETIME_FORMAT, Locale.getDefault());
                    Date date = dateFormat.parse(dayOfMonth + "." + month + "." + year + " " + 0 + ":" + 0 + ":" + 0);
                    if(dialogType == DialogType.START) {
                        ((LayoutEditor) layoutEditorContext).setStartDateText(date, itemPosition);
                    } else if (dialogType == DialogType.END) {
                        ((LayoutEditor) layoutEditorContext).setEndDateText(date, itemPosition);
                    }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
