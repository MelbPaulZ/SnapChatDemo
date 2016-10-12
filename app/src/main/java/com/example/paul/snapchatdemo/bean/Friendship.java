package com.example.paul.snapchatdemo.bean;

/**
 * Created by Anita on 2016/10/1.
 */
public class Friendship {
    private String id;
    private String username;
    private String friendId;
    private String friendUsername;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }



    public Friendship(String id, String username, String friendId, String friendUsername) {
        this.id = id;
        this.username = username;
        this.friendId = friendId;
        this.friendUsername = friendUsername;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", friendId='" + friendId + '\'' +
                ", friendUsername='" + friendUsername + '\'' +
                '}';
    }





}
