package com.example.yourecycle.Helpers;

import android.app.Application;

import com.example.yourecycle.Models.User;
import com.google.firebase.FirebaseApp;

public class YouRecyclePreference extends Application {

    private User user;

    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
