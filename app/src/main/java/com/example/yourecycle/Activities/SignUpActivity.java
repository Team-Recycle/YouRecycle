package com.example.yourecycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    public static final String NORMAL = "--normal--";
    private EditText name, email, phone, password, confirmPassword;
    private View back, signup, goto_login, terms;
    private FirebaseAuth mAuth;
    private CheckBox checkBox;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


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
        checkBox = findViewById(R.id.checkbox);
        signup = findViewById(R.id.signup);
        goto_login = findViewById(R.id.signin);
        back = findViewById(R.id.back_arrow);


        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(this).create();
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
                attemptSignUp();
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
                startActivity(new Intent(SignUpActivity.this, TermsAndConditionsActivity.class));
            }
        });
    }


    private void setupPhoneAuth() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Helper.shortToast(getApplicationContext(), "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Helper.shortToast(getApplicationContext(), "onVerificationFailed:" + e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }
        };


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone.getText().toString().trim(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Helper.shortToast(getApplicationContext(), "sign in successful");
                }
                else{
                    Helper.shortToast(getApplicationContext(), task.getException().getMessage());
                }
            }
        });
    }


    private void attemptSignUp(){

        final String _name = name.getText().toString().trim();
        final String _email = email.getText().toString().trim();
        final String _phone = phone.getText().toString().trim();
        String _password = password.getText().toString().trim();
        String _confirm = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(_name)){
            Toast.makeText(getApplicationContext(), "name field is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(_phone)){
            Toast.makeText(getApplicationContext(), "phone field is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(_email)){
            Toast.makeText(getApplicationContext(), "email field is required", Toast.LENGTH_SHORT).show();
        }
        else if (!Helper.isValidEmailId(_email)){
            Toast.makeText(getApplicationContext(), "email provided is not valid", Toast.LENGTH_SHORT).show();
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
                    "you have to agree to our terms and conditions", Toast.LENGTH_SHORT).show();
        }
        else if (!Helper.isNetworkAvailable(getApplicationContext())){
            alertDialog.setTitle("Network Error");
            alertDialog.setMessage("You do not have any internet connection!. please connect to the internet and try again.");
            alertDialog.show();
        }
        else {
            progressDialog.setTitle("Attempting Signup");
            progressDialog.setMessage("Please wait while we try to register your account.");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(_email, _password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
//                                setupPhoneAuth();

                                User user = new User();
                                user.setName(_name);
                                user.setEmail(_email);
                                user.setPhone(_phone);
                                user.setType(NORMAL);

                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();

                                        if (task.isSuccessful()){
                                           Toast.makeText(getApplicationContext(), "signup successful. you can login in now.", Toast.LENGTH_SHORT).show();
                                           startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                           finish();
                                       }
                                       else{
                                            alertDialog.setTitle(null);
                                            alertDialog.setMessage(task.getException().getMessage());
                                            alertDialog.show();
                                       }
                                    }
                                });


                            }
                            else{
                                progressDialog.dismiss();

                                alertDialog.setTitle(null);
                                alertDialog.setMessage(task.getException().getMessage());
                                alertDialog.show();
                            }
                        }
                    });
        }
    }

}

