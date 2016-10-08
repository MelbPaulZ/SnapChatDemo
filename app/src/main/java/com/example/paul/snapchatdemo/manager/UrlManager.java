package com.example.paul.snapchatdemo.manager;

import com.example.paul.snapchatdemo.bean.DiscoveryUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 27/09/2016.
 */
public class UrlManager {
    private static UrlManager ourInstance = new UrlManager();
    private static List<DiscoveryUrl> urls = new ArrayList<>();
    private UrlManager(){

    }
    public static UrlManager getInstance(){
        return ourInstance;
    }
    public static List<DiscoveryUrl> getUrls() {
        return urls;
    }

    public static void setUrls(List<DiscoveryUrl> urls) {
        UrlManager.urls = urls;
    }

}
