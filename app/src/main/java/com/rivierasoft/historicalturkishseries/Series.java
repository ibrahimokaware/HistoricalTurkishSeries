package com.rivierasoft.historicalturkishseries;

import java.util.ArrayList;

public class Series {
    private int id;
    private String name;
    private String picture;
    private String about;
    private ArrayList<String> photo_gallery;
    private String seasons;
    private int no_of_seasons;
    private ArrayList<Integer> e_of_each_s;
    private String display_status;
    private boolean display;

    public Series(int id, String name, String picture, String about, ArrayList<String> photo_gallery, String seasons, int no_of_seasons, ArrayList<Integer> e_of_each_s, String display_status, boolean display) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.about = about;
        this.photo_gallery = photo_gallery;
        this.seasons = seasons;
        this.no_of_seasons = no_of_seasons;
        this.e_of_each_s = e_of_each_s;
        this.display_status = display_status;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public ArrayList<String> getPhoto_gallery() {
        return photo_gallery;
    }

    public void setPhoto_gallery(ArrayList<String> photo_gallery) {
        this.photo_gallery = photo_gallery;
    }

    public String getSeasons() {
        return seasons;
    }

    public void setSeasons(String seasons) {
        this.seasons = seasons;
    }

    public int getNo_of_seasons() {
        return no_of_seasons;
    }

    public void setNo_of_seasons(int no_of_seasons) {
        this.no_of_seasons = no_of_seasons;
    }

    public ArrayList<Integer> getE_of_each_s() {
        return e_of_each_s;
    }

    public void setE_of_each_s(ArrayList<Integer> e_of_each_s) {
        this.e_of_each_s = e_of_each_s;
    }

    public String getDisplay_status() {
        return display_status;
    }

    public void setDisplay_status(String display_status) {
        this.display_status = display_status;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
}
