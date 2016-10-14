package com.example.paul.snapchatdemo.bean;

/**
 * Created by Paul on 7/09/2016.
 */
public class Friend implements People {
    private String name;
    private String id;
    private String status;
    private String gender;
    private long addFriendTime;
    // need to implement more, need discuss


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAddFriendTime() {
        return addFriendTime;
    }

    public void setAddFriendTime(long addFriendTime) {
        this.addFriendTime = addFriendTime;
    }
}