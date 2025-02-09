package com.example.yessir;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.web.WebView;

public class Tab {
    private WebView webView;
    private StringProperty title = new SimpleStringProperty("New Tab");
    private StringProperty url = new SimpleStringProperty("");
    
    public Tab() {
        this.webView = new WebView();
        webView.getEngine().titleProperty().addListener((obs, old, newTitle) -> {
            if (newTitle != null) {
                title.set(newTitle);
            }
        });
    }
    
    public WebView getWebView() { return webView; }
    public String getTitle() { return title.get(); }
    public String getUrl() { return url.get(); }
    public void setUrl(String url) { this.url.set(url); }
} 