package com.example.paul.snapchatdemo.manager;

import com.example.paul.snapchatdemo.bean.DiscoveryUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 27/09/2016.
 */
public class UrlManager {
    private static final int TOPIC_FINANCIAL = 1;
    private static final int TOPIC_BEAUTY = 2;
    private static final int TOPIC_EDUCATION = 3;
    private static final int TOPIC_SPORT = 4;

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
