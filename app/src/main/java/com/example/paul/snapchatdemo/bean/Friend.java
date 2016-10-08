package com.example.paul.snapchatdemo.bean;

/**
 * Created by Paul on 7/09/2016.
 */
public class Friend implements People {
    private String name;
    private String status;
    private String gender;
    // need to implement more, need discuss

    public Friend(String name) {
        this.name = name;
        status = "";
        gender = "male";
    }

    public Friend(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public Friend(String name, String status, String gender) {
        this.name = name;
        this.status = status;
        this.gender = gender;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}