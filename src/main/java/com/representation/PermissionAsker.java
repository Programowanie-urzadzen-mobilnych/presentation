package com.representation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.representation.Utils.READ_EXTERNAL_STORAGE_STATUS;
import static com.representation.Utils.WRITE_EXTERNAL_STORAGE_STATUS;

public class PermissionAsker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_asker);

/*        final Button addPermissionsButton = findViewById(R.id.add_permissions_button);
        addPermissionsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    requestWrite();
                }
            });

        final Button closeAppButton = findViewById(R.id.close_app_button);
        closeAppButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });*/

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            // Permission is not granted, request the permission
            requestWrite();
        }
        finish();
    }

    private void requestWrite() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                WRITE_EXTERNAL_STORAGE_STATUS);
    }
}
