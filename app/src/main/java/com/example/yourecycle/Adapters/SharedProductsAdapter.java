package com.example.yourecycle.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yourecycle.Activities.ProductDetailsActivity;
import com.example.yourecycle.Fragments.OnFragmentInteractionListener;
import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.Models.Item;
import com.example.yourecycle.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SharedProductsAdapter extends RecyclerView.Adapter<SharedProductsAdapter.ViewHolder> {

    public static final int REQUEST_CODE = 200;
    private List<Item> dataSet;
    private final OnFragmentInteractionListener listener;
    private final ArrayList<Item> MAIN_DATA;

    public SharedProductsAdapter(List<Item> data, OnFragmentInteractionListener listener){
        if (data == null) {
            this.dataSet = new ArrayList<>();
        }else {
            dataSet = data;
        }

        MAIN_DATA = new ArrayList<>(dataSet);

        this.listener = listener;
    }

    public List<Item> getDataSet() {
        return dataSet;
    }

    public void addItem(Item item){
        getDataSet().add(item);
        notifyItemInserted(getDataSet().size()-1);
    }

    public void removeItem(Item item){
        int index = getDataSet().indexOf(item);
        getDataSet().remove(index);
        notifyItemRemoved(index);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.junk_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = getDataSet().get(position);

        Glide.with(holder.itemView)
                .load(item.getImageUri())
                .into(holder.imageView);

        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        holder.messageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Helper.shortToast(v.getContext().getApplicationContext(), "sending notification");
//            }
//        });

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    HashMap<String, Object> event = new HashMap<>();
                    event.put("name", "call-owner");
                    event.put("number", item.getOwnerPhoneNumber());
                    listener.onFragmentInteraction(event);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext().getApplicationContext(), ProductDetailsActivity.class);
                intent.putExtra("image", item.getImageUri());
                intent.putExtra("itemName", item.getName());
                intent.putExtra("desc", item.getDesc());
                intent.putExtra("address", item.getAddress());
                intent.putExtra("owner", item.getOwnerId());
                intent.putExtra("phone", item.getOwnerPhoneNumber());
                intent.putExtra("name", item.getOwnerName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getDataSet().size();
    }

    public void searchProduct(String newText) {

        List<Item> tempData = new ArrayList<>(MAIN_DATA);

        List<Item> searchMatches = new ArrayList<>();

        for (Item item: tempData){
            if (item.getName().toLowerCase().contains(newText.trim().toLowerCase())){
                searchMatches.add(item);
            }
            else if (item.getDesc().toLowerCase().contains(newText.trim().toLowerCase())){
                searchMatches.add(item);
            }
        }

        dataSet =  searchMatches;

        notifyDataSetChanged();

    }

    public void closeSearch() {
        dataSet = new ArrayList<>(MAIN_DATA);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public View messageBtn, callBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            messageBtn = itemView.findViewById(R.id.message);
            callBtn = itemView.findViewById(R.id.call);
        }
    }

}
