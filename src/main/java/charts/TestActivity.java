package charts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.representation.R;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs_test);
        ArrayList<Float> values = new ArrayList<>();
        values.add(0.9F);
        values.add(1.1F);
        values.add(1.2F);
        values.add(1.3F);

        ArrayList<Float> values2 = new ArrayList<>();
        values2.add(0F);
        values2.add(1F);
        values2.add(2F);
        values2.add(3F);

        ChartObj obj = new ChartObj(values, "2020-01-29T09:00:00Z", "2020-01-31T09:00:00Z", "Napięcie [V]", true);
        ChartObj obj2 = new ChartObj(values, values2, "Napięcie [V]", "Napięcie [V]", true);

        FrameLayout linearLayoutSubParent = findViewById(R.id.frameLayout);
        ChartView chart = new ChartView(this, obj);
        linearLayoutSubParent.addView(chart);
    }
}
