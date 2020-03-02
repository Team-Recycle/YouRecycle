package com.example.yourecycle.Helpers;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.yourecycle.Activities.MainActivity;
import com.example.yourecycle.Activities.SignInActivity;
import com.example.yourecycle.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
