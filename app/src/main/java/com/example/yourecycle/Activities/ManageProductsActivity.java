package com.example.yourecycle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourecycle.Adapters.UserProductsAdapter;
import com.example.yourecycle.Models.Item;
import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {


    public static final int UPLOAD_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        findViewById(R.id.add_junk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ManageProductsActivity.this, UploadItemActivity.class), UPLOAD_REQUEST_CODE);
            }
        });

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadUserData();

    }

    private void loadUserData() {

        findViewById(R.id.no_feed_info).setVisibility(View.GONE);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);


        FirebaseFirestore.getInstance().collection("SharedProducts")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    findViewById(R.id.progressBar).setVisibility(View.GONE);

                    List<Item> itemList = task.getResult().toObjects(Item.class);

                    if (itemList.isEmpty()){
                        findViewById(R.id.no_feed_info).setVisibility(View.VISIBLE);
                    }
                    else {
                        UserProductsAdapter adapter = new UserProductsAdapter(itemList);
                        RecyclerView recyclerView = findViewById(R.id.products_list_RV);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_REQUEST_CODE && resultCode == RESULT_OK){
            loadUserData();
        }
    }
}
