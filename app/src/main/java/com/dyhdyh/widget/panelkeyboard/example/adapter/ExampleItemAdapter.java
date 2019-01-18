package com.dyhdyh.widget.panelkeyboard.example.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dyhdyh.widget.panelkeyboard.example.R;


/**
 * @author dengyuhan
 * created 2019/1/18 16:25
 */
public class ExampleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_example,viewGroup,false)){};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

}
