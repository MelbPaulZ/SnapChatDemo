package com.example.paul.snapchatdemo.bean;

/**
 * Created by Paul on 10/10/16.
 */
public class Story {
    public final static int STORY_SECRET = 1;
    public final static int STORY_NOT_SECRET = 0;
    private String id;
    private String storyText;
    private String image;
    private int isSecret;

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

    public int getIsSecret() {
        return isSecret;
    }

    public void setIsSecret(int isSecret) {
        this.isSecret = isSecret;
    }
}
