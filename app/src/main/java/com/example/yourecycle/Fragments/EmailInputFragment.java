package com.example.yourecycle.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.yourecycle.R;

import java.util.HashMap;
import java.util.Map;


public class EmailInputFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // TODO: Rename and change types of parameters
    private View view, sendResetEmail;
    private EditText emailTextView;

    private OnFragmentInteractionListener mListener;

    public EmailInputFragment() {
        // Required empty public constructor
    }

    public EmailInputFragment(OnFragmentInteractionListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_email_input, container, false);

        initializeViews();

        return view;
    }

    private void initializeViews() {

        emailTextView = (EditText) findViewById(R.id.email);
        sendResetEmail = findViewById(R.id.sendrequest);

        sendResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> event = new HashMap<>();
                event.put("name", "sendResetEmailOnClick");
                String email = emailTextView.getText().toString();
                event.put("email", email);
                onViewInteraction(event);
            }
        });
    }

    private View findViewById(int id){
        return view.findViewById(id);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onViewInteraction(Map<String, Object> event) {
        if (mListener != null) {
            mListener.onFragmentInteraction(event);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

}
