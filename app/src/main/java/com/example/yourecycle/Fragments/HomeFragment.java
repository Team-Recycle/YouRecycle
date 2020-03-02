package com.example.yourecycle.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yourecycle.Helpers.YouRecyclePreference;
import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {


    private OnFragmentInteractionListener listener;
    private View view;
    private User user;

    public HomeFragment(OnFragmentInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews();
        return view;
    }

    private View findViewById(int id){
        return getView().findViewById(id);
    }

    private void initializeViews() {
        user = ((YouRecyclePreference) getActivity().getApplicationContext()).getUser();

        ((TextView) findViewById(R.id.username)).setText(user.getName());

        View tutorial = findViewById(R.id.tutorial);
        View learning = findViewById(R.id.learning);
        View request = findViewById(R.id.request_a_driver);
        View transact = findViewById(R.id.junk_transaction);


        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    Map<String, Object> event = new HashMap<>();
                    event.put("name", R.id.nav_course);
                    listener.onFragmentInteraction(event);
                }
            }
        });
        learning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    Map<String, Object> event = new HashMap<>();
                    event.put("name", R.id.nav_learning);
                    listener.onFragmentInteraction(event);
                }
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    Map<String, Object> event = new HashMap<>();
                    event.put("name", R.id.nav_request);
                    listener.onFragmentInteraction(event);
                }
            }
        });
        transact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    Map<String, Object> event = new HashMap<>();
                    event.put("name", R.id.nav_transact);
                    listener.onFragmentInteraction(event);
                }
            }
        });

    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}
