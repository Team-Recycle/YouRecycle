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

    private FirebaseAuth mAuth;
    private User user;

    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        onStart();
        super.onCreate();
    }

    protected void onStart() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseFirestore.getInstance().collection("Users")
                    .document(mAuth.getCurrentUser().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        User user = task.getResult().toObject(User.class);
                        ((YouRecyclePreference) getApplicationContext()).setUser(user);
                        Toast.makeText(getApplicationContext(), "login successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(YouRecyclePreference.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
