package com.example.yourecycle.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yourecycle.Activities.ProductDetailsActivity;
import com.example.yourecycle.Models.Item;
import com.example.yourecycle.R;

import java.util.ArrayList;
import java.util.List;

public class SharedProductsAdapter extends RecyclerView.Adapter<SharedProductsAdapter.ViewHolder> {

    private final List<Item> dataSet;

    public SharedProductsAdapter(List<Item> data){
        if (data == null)
            this.dataSet = new ArrayList<>();
        else
            this.dataSet = data;
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
        View view = inflater.inflate(R.layout.user_junk_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = getDataSet().get(position);
        holder.itemName.setText(item.getName());
        if (item.getDesc().isEmpty()){
            holder.itemDescription.setVisibility(View.GONE);
        }
        else {
            holder.itemDescription.setText(item.getDesc());
        }
        holder.uploadTimeStamp.setText(item.getTimeStamp());

        Glide.with(holder.itemView)
                .load(item.getImageUri())
                .into(holder.imageView);

        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext().getApplicationContext(), ProductDetailsActivity.class);
                intent.putExtra("image", "");
                intent.putExtra("name", "");
                intent.putExtra("desc", "");
                intent.putExtra("contact", "");
                intent.putExtra("address", "");
                intent.putExtra("owner", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().getApplicationContext()
                        .startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getDataSet().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView itemName, itemDescription, uploadTimeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDesc);
            uploadTimeStamp = itemView.findViewById(R.id.timeStamp);
        }
    }

}
