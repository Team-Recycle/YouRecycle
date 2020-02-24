package com.example.yourecycle.Helpers;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class YouRecyclePreference extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
    }
}
