package com.example.yourecycle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.yourecycle.Helpers.YouRecyclePreference;
import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = task.getResult().toObject(User.class);
                        ((YouRecyclePreference) getApplicationContext()).setUser(user);
                        Toast.makeText(getApplicationContext(), "login successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                    else{
                        gotoIntroSliderActivity();
                    }
                }
            });
        }
        else{
            gotoIntroSliderActivity();
        }
    }

    private void gotoIntroSliderActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                finish();
            }
        }, 2000);
    }



}
