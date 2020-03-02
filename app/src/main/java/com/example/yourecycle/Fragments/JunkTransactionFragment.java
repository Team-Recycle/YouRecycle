package com.example.yourecycle.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;

public class JunkTransactionFragment extends Fragment {

    private View view;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_junk_transaction, container, false);
        return view;
    }

    private View findViewById(int id){
        return getView().findViewById(id);
    }
}
