package com.example.yourecycle.Helpers;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.yourecycle.Models.User;
import com.google.firebase.FirebaseApp;

public class YouRecyclePreference extends Application {

    public static final int MODE_PRIVATE = 0;
    public static final String DEFAULT_ADDRESS = "DEFAULT_LOCATION";
    public static final String ADDRESS = "--ADDRESS--";
    public static final String EMAIL = "--EMAIL--";
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

    public void setDefaultLocation(String email, String location){
        SharedPreferences preferences = getSharedPreferences(DEFAULT_ADDRESS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, email);
        editor.putString(ADDRESS, location);
        editor.apply();
    }

    public String getDefaultLocation(){
        SharedPreferences preferences = getSharedPreferences(DEFAULT_ADDRESS, MODE_PRIVATE);
        if (user.getEmail().equals(preferences.getString(EMAIL, ""))) {
            return preferences.getString(ADDRESS, null);
        }
        return null;
    }

}
