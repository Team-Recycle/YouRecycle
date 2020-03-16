package com.example.yourecycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ProductDetailsActivity extends AppCompatActivity {

    public static final int PHONE_CALL_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        setTextViewText(R.id.name_item, getIntent().getExtras().getString("itemName"));
        setTextViewText(R.id.description_item, getIntent().getExtras().getString("desc"));
        setTextViewText(R.id.address_info, getIntent().getExtras().getString("address"));
        setTextViewText(R.id.phone, getIntent().getExtras().getString("phone"));
        setTextViewText(R.id.user, getIntent().getExtras().getString("name"));

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOwner();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED) {
                        callOwner();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_REQUEST_CODE);
                    }
                } else {
                    callOwner();
                }
            }
        });

        Glide.with(this)
                .load(Uri.parse(getIntent().getExtras().getString("image")))
                .into((ImageView) findViewById(R.id.image));

    }

    private void callOwner() {
        String uri = "tel:" + getIntent().getExtras().getString("phone").trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        Helper.shortToast(getApplicationContext(), "attempting to call owner");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED) {
                return;
            }
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PHONE_CALL_REQUEST_CODE){
            if (grantResults[0] == PERMISSION_GRANTED){
                callOwner();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void setTextViewText(int id, String text){
        ((TextView) findViewById(id)).setText(text);
    }

}
