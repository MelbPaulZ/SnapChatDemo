package com.example.paul.snapchatdemo.bean;

/**
 * Created by Paul on 10/10/16.
 */
public class Story {
    private String id;
    private String storyText;
    private String image;

    public Story(){}

    public Story(String storyText, String url){

    }

    public String getStoryText() {
        return storyText;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
