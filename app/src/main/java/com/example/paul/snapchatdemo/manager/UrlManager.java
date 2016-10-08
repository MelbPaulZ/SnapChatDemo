package com.example.paul.snapchatdemo.manager;

import com.example.paul.snapchatdemo.bean.DiscoveryUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Paul on 27/09/2016.
 */
public class UrlManager {
    private static final int TOPIC_FINANCIAL = 1;
    private static final int TOPIC_BEAUTY = 2;
    private static final int TOPIC_EDUCATION = 3;
    private static final int TOPIC_SPORT = 4;

    private static UrlManager ourInstance = new UrlManager();
    private List<DiscoveryUrl> urls = new ArrayList<>();
    private List<Integer> clickLists = new ArrayList<>();
    private UrlManager(){

    }
    public static UrlManager getInstance(){
        return ourInstance;
    }
    public List<DiscoveryUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<DiscoveryUrl> urls) {
        this.urls = urls;
    }

    // this is the algorithm to
    public void updateUrls(int topic){
        List<DiscoveryUrl> newUrls = new ArrayList<>(urls);
        int size = newUrls.size();
        while(size > 1){
            DiscoveryUrl discoveryUrl = newUrls.get(size-1);
            if (discoveryUrl.getTopic() == topic){
                newUrls.remove(size-1);
                newUrls.add(0, discoveryUrl);
                break;
            }
            size--;
        }
        this.urls = newUrls;
    }
}
