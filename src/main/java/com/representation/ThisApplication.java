package com.representation;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import data.Database;
import lib.folderpicker.FolderPicker;

import static com.representation.Utils.READ_EXTERNAL_STORAGE_STATUS;
import static com.representation.Utils.WRITE_EXTERNAL_STORAGE_STATUS;

public class ThisApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: replace Database class calls with real database calls
        // This code is executed only once, when app is starting.
        //ask for permissions
        askForPermissions();

        Database.initalizeDatabase();
        Database.selectDefaultLayout();
    }

    private void askForPermissions() {
        Intent intent = new Intent(getApplicationContext(), PermissionAsker.class);
        startActivity(intent);
    }
}
