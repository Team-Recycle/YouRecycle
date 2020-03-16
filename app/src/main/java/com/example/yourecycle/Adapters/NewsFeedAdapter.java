package com.example.yourecycle.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yourecycle.Activities.NewsPageActivity;
import com.example.yourecycle.Fragments.OnFragmentInteractionListener;
import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.Models.Item;
import com.example.yourecycle.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {

    private final JSONArray dataSet;
    private final OnFragmentInteractionListener listener;

    public NewsFeedAdapter(JSONArray data, OnFragmentInteractionListener listener){
        if (data == null)
            this.dataSet = new JSONArray();
        else
            this.dataSet = data;

        this.listener = listener;

    }

    public JSONArray getDataSet() {
        return dataSet;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
//        final Map<String, String> item = getDataSet().get(position);

        try {
            final JSONObject jsonObject = getDataSet().getJSONObject(position);

            Glide.with(holder.itemView.getContext().getApplicationContext())
                    .load(Uri.parse(jsonObject.getString("urlToImage")))
                    .into(holder.imageView);

            holder.heading.setText(jsonObject.getString("title"));

            holder.content.setText(jsonObject.getString("description"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext().getApplicationContext(), NewsPageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("data", jsonObject.toString());
                    v.getContext().getApplicationContext().startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return getDataSet().length();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView heading, content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            heading = itemView.findViewById(R.id.heading);
            content = itemView.findViewById(R.id.content);
        }
    }

}
