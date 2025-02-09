package com.example.yessir;

import javafx.scene.image.Image;

public class Bookmark {
    private String title;
    private String url;
    private Image favicon;

    public Bookmark(String title, String url, Image favicon) {
        this.title = title;
        this.url = url;
        this.favicon = favicon;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Image getFavicon() {
        return favicon;
    }
}