package com.example.paul.snapchatdemo.utils;

/**
 * Created by Paul on 10/10/16.
 */
public class UserUtil {
    private static String username;

    // should have been kept this userId on local cache db, so we could push message to queue on server when app is not active
    private static String id = "4";

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
