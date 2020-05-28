package com.example.yourecipe.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import com.example.yourecipe.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.RecycleViewHolder> {

    private List<String> productList = new ArrayList<>();

    public void setItems(Collection products) {
        productList.addAll(products);
        notifyDataSetChanged();
    }

    public void clearItems() {
        productList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkbox_item_view, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder {

        private CheckBox productCheckBoxView;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            productCheckBoxView = itemView.findViewById(R.id.checkBoxProduct);
        }

        public void bind(String product) {
            productCheckBoxView.setText(product);
        }

    }

}
