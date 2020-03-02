package com.example.yourecycle.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.yourecycle.Fragments.HomeFragment;
import com.example.yourecycle.Fragments.JunkTransactionFragment;
import com.example.yourecycle.Fragments.LearningFragment;
import com.example.yourecycle.Fragments.OnFragmentInteractionListener;
import com.example.yourecycle.Fragments.RequestDriverFragment;
import com.example.yourecycle.Fragments.TutorialFragment;
import com.example.yourecycle.R;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.widget.TextView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment, learningFragment, tutorialFragment, transactFragment, requestFragment;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigationDrawer();
        initializeViews();
    }

    private void initializeViews() {
        learningFragment = new LearningFragment();
        tutorialFragment = new TutorialFragment();
        transactFragment = new JunkTransactionFragment();
        requestFragment = new RequestDriverFragment();

        homeFragment = new HomeFragment(new OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(Map<String, Object> event) {
                switch ((int) event.get("name")){
                    case R.id.nav_request: showFragment(requestFragment); break;
                    case R.id.nav_transact: showFragment(transactFragment); break;
                    case R.id.nav_course: showFragment(tutorialFragment); break;
                    case R.id.nav_learning: showFragment(learningFragment); break;
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment)
                .commit();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment)
                .addToBackStack(null).commit();
    }

    private void setupNavigationDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home: showFragment(homeFragment); break;
                    case R.id.nav_request: showFragment(requestFragment); break;
                    case R.id.nav_transact: showFragment(transactFragment); break;
                    case R.id.nav_course: showFragment(tutorialFragment); break;
                    case R.id.nav_learning: showFragment(learningFragment); break;
                    case R.id.nav_change_password: startForgotPasswordActivity(); break;
                    case R.id.nav_logout: logoutUser(); break;
                }
                return false;
            }
        });
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
        finish();
    }

    private void startForgotPasswordActivity() {
        startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
