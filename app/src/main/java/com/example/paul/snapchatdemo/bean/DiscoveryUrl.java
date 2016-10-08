package com.example.paul.snapchatdemo.bean;

/**
 * Created by Paul on 27/09/2016.
 */
public class DiscoveryUrl {

    public final int TOPIC_FINANCE = 1;
    public final int TOPIC_BEAUTY = 2;
    public final int TOPIC_SPORT = 3;
    public final int TOPIC_ENTERTAINMENT = 4;

    private int topic;
    private String url;

    public DiscoveryUrl(){

    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
