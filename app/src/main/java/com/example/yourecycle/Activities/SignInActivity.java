package com.example.yourecycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText email, password;
    private View no_account, goto_signup, signin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeViews();
    }

    private void initializeViews() {

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        goto_signup = findViewById(R.id.sign_up);
        no_account = findViewById(R.id.no_account);
        signin = findViewById(R.id.signin);

        goto_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_signup.callOnClick();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempSignin();
            }
        });
    }

    private void attempSignin() {
        String _email = email.getText().toString().trim();
        String _password = password.getText().toString().trim();

        if (TextUtils.isEmpty(_email)){
            Toast.makeText(getApplicationContext(), "email field is required!", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(_password)){
            Toast.makeText(getApplicationContext(), "password field is required!", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "login successful.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
