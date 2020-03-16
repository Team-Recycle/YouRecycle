package com.example.yourecycle.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yourecycle.Activities.ProductDetailsActivity;
import com.example.yourecycle.Fragments.OnFragmentInteractionListener;
import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.Helpers.YouRecyclePreference;
import com.example.yourecycle.Models.Item;
import com.example.yourecycle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserProductsAdapter extends RecyclerView.Adapter<UserProductsAdapter.ViewHolder> {

    private final List<Item> dataSet;
    private final OnFragmentInteractionListener listener;
    private List<Item> selectedItems;
    private boolean selectedActivated;

    public UserProductsAdapter(List<Item> data, OnFragmentInteractionListener listener){
        if (data == null)
            this.dataSet = new ArrayList<>();
        else
            this.dataSet = data;

        this.listener = listener;

        selectedItems = new ArrayList<>();
        selectedActivated = false;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Item item = getDataSet().get(position);
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (selectedActivated) {
                    if (selectedItems.contains(item)){
                        selectedItems.remove(item);
                    }else {
                        selectedItems.add(item);
                    }
                    notifyItemChanged(position);
                }
                else{
                    selectedItems = new ArrayList<>();
                    selectedItems.add(item);
                    selectedActivated = true;
                    notifyDataSetChanged();
                    if (listener != null){
                        HashMap<String, Object> event = new HashMap<>();
                        event.put("name", "initial-long-click");
                        listener.onFragmentInteraction(event);
                    }
                }

                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedActivated){
                    if (selectedItems.contains(item)){
                        selectedItems.remove(item);
                    }else {
                        selectedItems.add(item);
                    }
                    notifyItemChanged(position);
                }
            }
        });

        if (selectedActivated){
            holder.checkBox.setVisibility(View.VISIBLE);
            if (selectedItems.contains(item)){
                holder.checkBox.setChecked(true);
            }else{
                holder.checkBox.setChecked(false);
            }
        }
        else{
            holder.checkBox.setChecked(false);
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    public void deleteSelected(){
        for (Item item: selectedItems){
            //TODO: remove all selected from list
            getDataSet().remove(item);
            FirebaseFirestore.getInstance().collection("SharedProducts")
                    .document(item.getID()).delete();
        }
        notifyDataSetChanged();
    }

    public void selectAll(){
        selectedItems = new ArrayList<>(dataSet);
        notifyDataSetChanged();
    }

    public void deSelectAll(){
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void cancelSelection(){
        selectedActivated = false;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public List<Item> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public int getItemCount() {
        return getDataSet().size();
    }

    public boolean isSelectionEnabled() {
        return selectedActivated;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CheckBox checkBox;
        public ImageView imageView;
        public TextView itemName, itemDescription, uploadTimeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.marked);
            imageView = itemView.findViewById(R.id.image);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDesc);
            uploadTimeStamp = itemView.findViewById(R.id.timeStamp);
        }
    }

}
