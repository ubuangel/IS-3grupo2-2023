package com.sexed.androidapp.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Blog {

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("image")
    private String image;

    @SerializedName("date")
    private String date;

    @SerializedName("rating")
    private Double rating;

    @SerializedName("description")
    private String description;

    public Blog(String title, String url, String image, String date, Double rating, String description) {
        this.title = title;
        this.url = url;
        this.image = image;
        this.date = date;
        this.rating = rating;
        this.description = description;
    }

    public Blog() {
        this.title = "";
        this.url = "";
        this.image = "";
        this.date = "";
        this.description = "";
        this.rating = 0.0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
