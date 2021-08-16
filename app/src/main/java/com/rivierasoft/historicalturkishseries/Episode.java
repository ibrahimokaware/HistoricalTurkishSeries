package com.rivierasoft.historicalturkishseries;

import java.util.Date;

public class Episode {
    private int id;
    private int series_id;
    private int season;
    private String document;
    private String title;
    private String photo;
    private String video;
    private String duration;
    private String time;
    private int views;

    public Episode(int id, int series_id, int season, String document, String title, String photo, String video, String duration, String time, int views) {
        this.id = id;
        this.series_id = series_id;
        this.season = season;
        this.document = document;
        this.title = title;
        this.photo = photo;
        this.video = video;
        this.duration = duration;
        this.time = time;
        this.views = views;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeries_id() {
        return series_id;
    }

    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
