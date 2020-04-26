package com.representation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import layouteditor.LayoutEditor;
import layouts.LayoutsList;
import measurements.Measurements;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, Measurements.class);
        startActivity(i);
    }
}
