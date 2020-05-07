package com.representation;

import android.app.Application;

import data.Database;

public class ThisApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: replace Database class calls with real database calls
        // This code is executed only once, when app is starting.
        Database.initalizeDatabase();
        Database.selectDefaultLayout();
    }
}
