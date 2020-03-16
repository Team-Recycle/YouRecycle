package com.example.yourecycle.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.Helpers.YouRecyclePreference;
import com.example.yourecycle.Models.Item;
import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadItemActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 20;
    private static final int CAMERA_PICTURE = 80;
    private static final int CAMERA_PERMISSIONS = 120;
    private static final int GALLERY_PICK_PERMISSIONS = 140;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        user = ((YouRecyclePreference) getApplicationContext()).getUser();

        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(this).create();

        final String lastLocation = ((YouRecyclePreference) getApplicationContext()).getDefaultLocation();
        if (lastLocation != null)
            ((TextView) findViewById(R.id.address)).setText(lastLocation);

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.add_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation animation = new TranslateAnimation(0,0,300,0);
                animation.setDuration(300);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        findViewById(R.id.image_pickers).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                findViewById(R.id.image_pickers).startAnimation(animation);
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation animation = new TranslateAnimation(0,0,0,700);
                animation.setDuration(300);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        findViewById(R.id.image_pickers).setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                findViewById(R.id.image_pickers).startAnimation(animation);
            }
        });

        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if (ContextCompat.checkSelfPermission(UploadItemActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(UploadItemActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(UploadItemActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS);
                    }
                    else {
                        openCamera();
                    }
                }
                else{
                    openCamera();
                }
                
                findViewById(R.id.cancel).callOnClick();
            }
        });


        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if (ContextCompat.checkSelfPermission(UploadItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(UploadItemActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PICK_PERMISSIONS);
                    }
                    else {
                        openGalleryPicker();
                    }
                }
                else{
                    openGalleryPicker();
                }
                findViewById(R.id.cancel).callOnClick();
            }
        });

        findViewById(R.id.save_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String address = ((EditText) findViewById(R.id.address)).getText().toString().trim();
                final String name = ((EditText) findViewById(R.id.product_name)).getText().toString().trim();
                final String desc = ((EditText) findViewById(R.id.description)).getText().toString().trim();

                if (address.isEmpty()){
                    Helper.shortToast(getApplicationContext(), "your address is required!.");
                }
                else if (name.isEmpty()){
                    Helper.shortToast(getApplicationContext(), "please enter the name of product you're sharing");
                }
                else if (imageUri == null){
                    Helper.shortToast(getApplicationContext(), "please add an image!.");
                }
                else {

                    final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    progressDialog.setTitle("Adding...");
                    progressDialog.setMessage("please wait while we add your item...");
                    progressDialog.show();

                    final StorageReference reference = FirebaseStorage.getInstance()
                            .getReference("ProductsDisplayImage")
                            .child(user_id+Helper.randomString(10)+".jpg");


                    reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){

                                final Item item = new Item();
                                item.setName(name);
                                item.setDesc(desc);
                                item.setOwnerId(user_id);
                                item.setAddress(address);
                                item.setOwnerPhoneNumber(((YouRecyclePreference) getApplicationContext()).getUser().getPhone());
                                item.setOwnerName(((YouRecyclePreference) getApplicationContext()).getUser().getName());
                                item.setTimeStamp(new SimpleDateFormat("dd/MM/yyy").format(new Date()));

                                reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()){
                                            Helper.log(task.getResult().toString());
                                            item.setImageUri(task.getResult().toString());

                                            FirebaseFirestore.getInstance().collection("SharedProducts")
                                                    .add(item).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull final Task<DocumentReference> task) {

                                                    if (task.isSuccessful()){
                                                        Helper.shortToast(getApplicationContext(), "your item was added successfully.");
                                                        String email = ((YouRecyclePreference) getApplicationContext()).getUser().getEmail();
                                                        ((YouRecyclePreference) getApplicationContext()).setDefaultLocation(email, address);

                                                    final Map<String, Object> properties = user.getProperties();
                                                    Object obj = user.getProperties().get("uploadedItems");
                                                    List<String> arr;
                                                    if (obj != null){
                                                        arr = (List<String>) obj;
                                                        arr.add(task.getResult().getId());
                                                    }
                                                    else {
                                                        arr = new ArrayList<>();
                                                        arr.add(task.getResult().getId());
                                                    }
                                                    properties.put("uploadedItems", arr);

                                                    FirebaseFirestore.getInstance().collection("Users")
                                                                .document(user_id).update("properties", properties)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                                        if (task1.isSuccessful()){
                                                                            setResult(RESULT_OK);
                                                                            user.setProperties(properties);
                                                                            ((YouRecyclePreference) getApplicationContext()).setUser(user);
                                                                            finish();
                                                                        }
                                                                        else{
                                                                            alertDialog.setMessage(task1.getException().getMessage());
                                                                            alertDialog.show();
                                                                            task.getResult().delete();
                                                                            reference.delete();
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                    else{
                                                        alertDialog.setMessage(task.getException().getMessage());
                                                        alertDialog.show();
                                                    }
                                                }
                                            });


                                        }else {
                                            progressDialog.hide();

                                            alertDialog.setMessage(task.getException().getMessage());
                                            alertDialog.show();
                                        }
                                    }
                                });

                            }
                            else{
                                progressDialog.hide();

                                alertDialog.setMessage(task.getException().getMessage());
                                alertDialog.show();
                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSIONS){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
            else{
                Helper.longToast(getApplicationContext(), "Camera permissions denied!");
            }
        }

        if (requestCode == GALLERY_PICK_PERMISSIONS){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGalleryPicker();
            }
            else{
                Helper.longToast(getApplicationContext(), "Gallery permissions denied!");
            }
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            CropImage.activity(uri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(UploadItemActivity.this);
        }

        if (requestCode == CAMERA_PICTURE && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            Uri tempUri = null;
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            if (uri == null) {
                tempUri = getImageUri(getApplicationContext(), (Bitmap) data.getExtras().get("data"));
            }

            if (data.getData() != null || tempUri != null) {
                CropImage.activity((uri == null) ? tempUri: uri)
                        .setAspectRatio(1, 1)
                        .setMinCropWindowSize(500, 500)
                        .start(UploadItemActivity.this);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                findViewById(R.id.image).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.image)).setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Helper.log("Error: Crop Error.\nMessage: " + result.getError().getMessage());
                Helper.longToast(getApplicationContext(), result.getError().getMessage());
            }
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void openGalleryPicker() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PICTURE);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.image_pickers).getVisibility() == View.VISIBLE){
            findViewById(R.id.cancel).callOnClick();
        }
        else {
            super.onBackPressed();
        }
    }
}
