package com.rivierasoft.historicalturkishseries;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.AdapterViewHolder> {

    private ArrayList<MainRecyclerView> mainRecyclerViewArrayList;
    private Context context;
    private OnItemClickListener listener;

    public MainRecyclerViewAdapter(ArrayList<MainRecyclerView> mainRecyclerViewArrayList, Context context, OnItemClickListener listener) {
        this.mainRecyclerViewArrayList = mainRecyclerViewArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_main, parent, false);
        AdapterViewHolder adapterViewHolder = new AdapterViewHolder(view);
        return adapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        MainRecyclerView mainRecyclerView = mainRecyclerViewArrayList.get(position);
        holder.textView.setText(mainRecyclerView.getTitle());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(mainRecyclerView.getAdapter());
    }

    @Override
    public int getItemCount() {
        return mainRecyclerViewArrayList.size();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        RecyclerView recyclerView;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            recyclerView = itemView.findViewById(R.id.recyclerView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   listener.OnClick(getAdapterPosition());
                }
            });
        }
    }
}
