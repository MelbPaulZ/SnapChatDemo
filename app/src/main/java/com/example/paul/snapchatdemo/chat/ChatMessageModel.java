package com.example.paul.snapchatdemo.chat;

/**
 * Created by verramukty on 9/26/2016.
 */
public class ChatMessageModel {

    private String text;
    private String imageUrl;

    public ChatMessageModel(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
