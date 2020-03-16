package com.example.yourecycle.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourecycle.Activities.ManageProductsActivity;
import com.example.yourecycle.Adapters.SharedProductsAdapter;
import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.Models.Item;
import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.SEARCH_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.example.yourecycle.Activities.ProductDetailsActivity.PHONE_CALL_REQUEST_CODE;

public class JunkTransactionFragment extends Fragment {

    private View view;
    private User user;
    private SearchView searchView;
    private String lastNumber;
    private SharedProductsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_junk_transaction, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {

        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        findViewById(R.id.manage_junk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageProductsActivity.class));
            }
        });

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchView.isIconified()){
                    searchView.clearFocus();
                    searchView.setIconified(true);
                }
                else {
                    getActivity().onBackPressed();
                }

            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

        final CollectionReference sharedProducts = FirebaseFirestore.getInstance().collection("SharedProducts");

        sharedProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    findViewById(R.id.shimmer_view_container).setVisibility(View.GONE);
                    if (task.isSuccessful()) {

                        List<Item> data = task.getResult().toObjects(Item.class);

                        List<Item> itemsList = new ArrayList<>();

                        for (Item item: data){
                            if (!item.getOwnerId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                itemsList.add(item);
                            }
                        }

                        adapter = new SharedProductsAdapter(itemsList, new OnFragmentInteractionListener() {
                            @Override
                            public void onFragmentInteraction(Map<String, Object> event) {
                                if (event.get("name").toString().equals("call-owner")) {
                                    lastNumber = event.get("number").toString();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED) {
                                            callOwner(event.get("number").toString());
                                        } else {
                                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_REQUEST_CODE);
                                        }
                                    } else {
                                        callOwner(event.get("number").toString());
                                    }
                                }
                            }
                        });
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.products_list_RV);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        recyclerView.setAdapter(adapter);
                    } else {
                        findViewById(R.id.no_feed_info).setVisibility(View.VISIBLE);
                    }
                } else {
                    Helper.log(task.getException().getMessage());
                }
            }
        });

    }


    private void callOwner(String number) {
        String uri = "tel:" + number;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED) {
                return;
            }
        }
        startActivity(intent);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.junk_transact_menu, menu);
        MenuItem mActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) mActionMenuItem.getActionView();
        searchView.setQueryHint("Search Product Name");
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.closeSearch();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.searchProduct(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.searchProduct(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private View findViewById(int id){
        return getView().findViewById(id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        callOwner(lastNumber);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}
