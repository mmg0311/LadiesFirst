package com.example.ladiesfirst;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class ladiesfirst extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
