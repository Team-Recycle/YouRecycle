package com.example.yourecycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, phone, password, confirmPassword;
    private View back, signup, goto_login;
    private CheckBox checkBox;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();
    }

    private void initializeViews() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm);

        dialog = new ProgressDialog(this);

        signup = findViewById(R.id.signup);
        goto_login = findViewById(R.id.signin);
        back = findViewById(R.id.back_arrow);
        checkBox = findViewById(R.id.checkbox);
        mAuth = FirebaseAuth.getInstance();

        setupViewListeners();
    }

    private void setupViewListeners() {
        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_login.callOnClick();
            }
        });

        findViewById(R.id.terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, TermsandConditionsActivity.class));
            }
        });
    }

    private void attemptSignup(){

        String _name = name.getText().toString().trim();
        String _email = email.getText().toString().trim();
        String _phone = phone.getText().toString().trim();
        String _password = password.getText().toString().trim();
        String _confirm = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(_name)){
            Toast.makeText(getApplicationContext(), "name field is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(_email)){
            Toast.makeText(getApplicationContext(), "email field is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(_phone)){
            Toast.makeText(getApplicationContext(), "phone field is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(_password)){
            Toast.makeText(getApplicationContext(), "password field is required", Toast.LENGTH_SHORT).show();
        }
        else if (_password.length() < 5){
            Toast.makeText(getApplicationContext(), "password is too short", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(_confirm)){
            Toast.makeText(getApplicationContext(), "please confirm your password", Toast.LENGTH_SHORT).show();
        }
        else if (!_password.equals(_confirm)){
            Toast.makeText(getApplicationContext(),
                    "your passwords do not match please check an try again!.", Toast.LENGTH_SHORT).show();
        }
        else if (!checkBox.isChecked()){
            Toast.makeText(getApplicationContext(),
                    "you have to agree to the terms and conditions!.", Toast.LENGTH_SHORT).show();
        }
        else {
            dialog.setTitle("Attempting Signup");
            dialog.setMessage("Please wait while we try to create your account.");
            dialog.setCancelable(false);
            dialog.show();
            mAuth.createUserWithEmailAndPassword(_email, _password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "signup successful. you can login in now.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}

