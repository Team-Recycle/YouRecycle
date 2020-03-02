package com.example.yourecycle.Fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;

public class TutorialFragment extends Fragment {

    private View view;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        return view;
    }

    private View findViewById(int id){
        return getView().findViewById(id);
    }
}