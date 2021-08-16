package com.rivierasoft.historicalturkishseries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.AdapterViewHolder> {

    private ArrayList<Series> seriesArrayList;
    private Context context;
    private OnItemClickListener listener;

    public SeriesAdapter(ArrayList<Series> seriesArrayList, Context context, OnItemClickListener listener) {
        this.seriesArrayList = seriesArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.series, parent, false);
        AdapterViewHolder adapterViewHolder = new AdapterViewHolder(view);
        return adapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Series series = seriesArrayList.get(position);

        Glide.with(context)
                .asBitmap()
                .load(series.getPicture())
                .into(holder.imageView);
        holder.nameTextView.setText(series.getName());
        holder.seasonsTextView.setText(series.getSeasons());
    }

    @Override
    public int getItemCount() {
        return seriesArrayList.size();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView, seasonsTextView;
        ConstraintLayout constraintLayout;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            seasonsTextView = itemView.findViewById(R.id.seasonsTextView);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);

            constraintLayout.setOnClickListener(v -> {
                listener.OnClick(getAdapterPosition());
            });
        }
    }
}
