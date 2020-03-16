package com.example.yourecycle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourecycle.Adapters.UserProductsAdapter;
import com.example.yourecycle.Fragments.OnFragmentInteractionListener;
import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.Helpers.YouRecyclePreference;
import com.example.yourecycle.Models.Item;
import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageProductsActivity extends AppCompatActivity {


    public static final int UPLOAD_REQUEST_CODE = 200;
    private UserProductsAdapter adapter;
    private User user;

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

        List<String> uploadItemsList = new ArrayList<>();

        user = ((YouRecyclePreference) getApplicationContext()).getUser();

        Object obj = user.getProperties().get("uploadedItems");

        if (obj != null)
            uploadItemsList = (List<String>) user.getProperties().get("uploadedItems");

        if (uploadItemsList == null)
            uploadItemsList = new ArrayList<>();

        if (uploadItemsList.isEmpty()) {
            findViewById(R.id.no_feed_info).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }

        final List<Item> itemList = new ArrayList<>();

        for (int i=0;i<uploadItemsList.size();i++){

            final String id = uploadItemsList.get(i);

            final List<String> finalUploadItemsList = uploadItemsList;
            FirebaseFirestore.getInstance().collection("SharedProducts")
                    .document(id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                Item item = task.getResult().toObject(Item.class);
                                if (item != null) {
                                    item.setID(id);
                                    itemList.add(item);
                                }

                                if (finalUploadItemsList.indexOf(id) == finalUploadItemsList.size() - 1) {
                                    findViewById(R.id.progressBar).setVisibility(View.GONE);

                                    adapter = new UserProductsAdapter(itemList, new OnFragmentInteractionListener() {
                                        @Override
                                        public void onFragmentInteraction(Map<String, Object> event) {
                                            if (event.get("name").toString().equals("initial-long-click")) {
                                                startSelection();
                                            }
                                        }
                                    });
                                    RecyclerView recyclerView = findViewById(R.id.products_list_RV);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    recyclerView.setAdapter(adapter);

                                }
                            }
                        }
                    });

        }



        findViewById(R.id.selectionsBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSelection();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteSelected();
                if (adapter.getSelectedItems().size() > 0){
                    Helper.shortToast(getApplicationContext(), "deletion successful");
                    List<String> items = (List<String>) user.getProperties().get("uploadedItems");
                    for (Item item: adapter.getSelectedItems()) {
                        int i = items.indexOf(item.getID());
                        items.remove(i);
                    }
                    user.getProperties().put("uploadedItems", items);
                    ((YouRecyclePreference) getApplicationContext()).setUser(user);
                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .update("properties", user.getProperties());
                }
                hideSelection();
                adapter.getSelectedItems().clear();
            }
        });

        findViewById(R.id.globalCheckBox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()){
                    adapter.selectAll();
                }
                else{
                    adapter.deSelectAll();
                }
            }
        });

    }

    private void startSelection() {
        findViewById(R.id.selection_toolbar).setVisibility(View.VISIBLE);
        findViewById(R.id.toolbar).setVisibility(View.GONE);
        ((CheckBox) findViewById(R.id.globalCheckBox)).setChecked(false);
        TranslateAnimation animation = new TranslateAnimation(0,0,300,0);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                findViewById(R.id.add_junk).setVisibility(View.GONE);
                findViewById(R.id.delete).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.delete).startAnimation(animation);
    }

    private void hideSelection(){
        adapter.cancelSelection();

        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        findViewById(R.id.selection_toolbar).setVisibility(View.GONE);
        ((CheckBox) findViewById(R.id.globalCheckBox)).setChecked(false);

        TranslateAnimation animation = new TranslateAnimation(0,0,0,300);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.add_junk).setVisibility(View.VISIBLE);
                findViewById(R.id.delete).setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.delete).startAnimation(animation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_REQUEST_CODE && resultCode == RESULT_OK){
            loadUserData();
        }
    }

    @Override
    public void onBackPressed() {
        if (adapter != null && adapter.isSelectionEnabled()){
            hideSelection();
        }
        else {
            super.onBackPressed();
        }
    }
}
