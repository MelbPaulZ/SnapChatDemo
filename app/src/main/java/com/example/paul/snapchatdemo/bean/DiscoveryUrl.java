package com.example.paul.snapchatdemo.bean;

/**
 * Created by Paul on 27/09/2016.
 */
public class DiscoveryUrl {
    private int topic;
    private String url;
    private String textUrl;

    public DiscoveryUrl(){

    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTextUrl() {
        return textUrl;
    }

    public void setTextUrl(String textUrl) {
        this.textUrl = textUrl;
    }
}
