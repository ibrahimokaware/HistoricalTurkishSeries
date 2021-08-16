package com.rivierasoft.historicalturkishseries;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainRecyclerView {
    private String title;
    private ArrayList<Episode> episodes;
    private RecyclerView.Adapter adapter;

    public MainRecyclerView(String title, ArrayList<Episode> episodes, RecyclerView.Adapter adapter) {
        this.title = title;
        this.episodes = episodes;
        this.adapter = adapter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<Episode> episodes) {
        this.episodes = episodes;
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }
}
