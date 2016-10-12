package com.example.paul.snapchatdemo.utils;

/**
 * Created by Paul on 10/10/16.
 */
public class UserUtil {
    private static String username;
    private static String id;
    private static String token;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserUtil.username = username;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        UserUtil.id = id;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserUtil.token = token;
    }
}
