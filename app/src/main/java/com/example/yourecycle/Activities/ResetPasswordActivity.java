package com.example.yourecycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.os.Handler;

import com.example.yourecycle.Fragments.EmailInputFragment;
import com.example.yourecycle.Fragments.EmailSentFragment;
import com.example.yourecycle.Fragments.OnFragmentInteractionListener;
import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;


public class ResetPasswordActivity extends AppCompatActivity{

    private Fragment emailInputFragment, emailSentFragment;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(this).create();
        mHandler = new Handler();
        mAuth = FirebaseAuth.getInstance();

        this.emailInputFragment = new EmailInputFragment(getFragmentInteractionListener());
        this.emailSentFragment = new EmailSentFragment();

        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, emailInputFragment)
                .addToBackStack(null).commit();
    }


    public OnFragmentInteractionListener getFragmentInteractionListener() {
        return new OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(Map<String, Object> event) {
                switch (event.get("name").toString()){
                    case "sendResetEmailOnClick":
                        progressDialog.setTitle("Sending Email");
                        progressDialog.setMessage("Please wait while we try to send you a reset link.");
                        progressDialog.show();
                        String email = event.get("email").toString();
                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    //TODO: show verification email sent fragment
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, emailSentFragment)
                                            .addToBackStack(null).commit();
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 5000);

                                }
                                else{
                                    //TODO: show error
                                    alertDialog.setTitle("Error");
                                    alertDialog.setMessage(task.getException().getMessage());
                                }
                            }
                        });
                        break;
                }
            }
        } ;
    }

}
