package com.example.paul.snapchatdemo.bean;

/**
 * Created by Paul on 20/09/2016.
 */

import java.util.List;

/** this class is for retrieving data from server, properties must be the same as server database.
 */
public class User {
    private String id;
    private String token;
    private String username;
    private List<Friend> friends;
    private List<DiscoveryUrl> discoveryUrls;

    public User(String id, String token, String userName, List<Friend> friends) {
        this.id = id;
        this.token = token;
        this.username = userName;
        this.friends = friends;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", friends=" + friends.toString() +
                '}';
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public List<DiscoveryUrl> getDiscoveryUrls() {
        return discoveryUrls;
    }

    public void setDiscoveryUrls(List<DiscoveryUrl> discoveryUrls) {
        this.discoveryUrls = discoveryUrls;
    }
}
