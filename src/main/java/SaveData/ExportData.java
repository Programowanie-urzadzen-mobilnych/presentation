package SaveData;

import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class ExportData {
    private String name;
    private ArrayList<Float> values = new ArrayList<>();
    private ArrayList<String> timeStamps = new ArrayList<>();

    public ExportData(String name) {
        this.name = name;
    }

    public void addValue(String s, String s1) {
        Float value;
        //Timestamp timestamp;

        try{
            value = Float.parseFloat(s.trim());

            //SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            //Date parsedDate = dateFormat.parse(s1.trim());
            //timestamp = new java.sql.Timestamp(parsedDate.getTime());

            values.add(value);
            timeStamps.add(s1);
        }catch (Exception e){
            //throw new DataCannotConvertException;
            Log.i("ExportData Error","nie udalo sie przerobic wartosci");
        }
    }

    public String getValuesName() {
        return name;
    }

    public int getDataLength() {
        return values.size();
    }

    public String getStringOf(int a) {
        return values.get(a).toString() + " | "+ timeStamps.get(a).toString();
    }
}
