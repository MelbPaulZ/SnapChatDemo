package com.example.paul.snapchatdemo.bean;

/**
 * Created by Anita on 2016/10/12.
 */
public class PhotoStory {
    private String id;
    private String image;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "PhotoStory{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                '}';
    }


}
