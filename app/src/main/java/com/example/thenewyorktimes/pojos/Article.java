package com.example.thenewyorktimes.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {
    public Article() {
    }

    public Article(String byline, String title, String description, String url, String urlToImage, String publishedAt) {
        this.byline = byline;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    private String byline;

    private String title;

    private String description;

    private String url;

    private String urlToImage;

    private String publishedAt;


    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}