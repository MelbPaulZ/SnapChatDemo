package com.example.paul.snapchatdemo.bean;

/**
 * Created by Paul on 10/10/16.
 */
public class Story {
    private Friend friend;
    private String text;
    private String url;

    public Story(){}

    public Story(String text, String url){

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
