package com.example.yourecycle.Fragments;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourecycle.Activities.ManageProductsActivity;
import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;

import static android.content.Context.SEARCH_SERVICE;

public class JunkTransactionFragment extends Fragment {

    private View view;
    private User user;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_junk_transaction, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        findViewById(R.id.manage_junk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageProductsActivity.class));
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.junk_transact_menu, menu);
        MenuItem mActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) mActionMenuItem.getActionView();
        searchView.setQueryHint("Search Products");
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private View findViewById(int id){
        return getView().findViewById(id);
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}
