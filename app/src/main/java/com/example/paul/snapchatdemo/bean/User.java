package com.example.paul.snapchatdemo.bean;

/**
 * Created by Paul on 20/09/2016.
 */

/** this class is for retrieving data from server, properties must be the same as server database.
 */
public class User {
    private String id;
    private String token;
    private String username;

    public User(String id, String token, String userName) {
        this.id = id;
        this.token = token;
        this.username = userName;
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
        return "Register{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", userName='" + username + '\'' +
                '}';
    }
}
