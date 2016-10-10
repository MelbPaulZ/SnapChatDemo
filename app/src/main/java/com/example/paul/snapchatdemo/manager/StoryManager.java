package com.example.paul.snapchatdemo.manager;

import com.example.paul.snapchatdemo.bean.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 10/10/16.
 */
public class StoryManager {
    private static StoryManager instance = new StoryManager();
    private List<Story> storyList = new ArrayList<>();
    private StoryManager(){

    }

    public static StoryManager getInstance(){
        return instance;
    }

    public List<Story> getStoryList() {
        return storyList;
    }

    public void setStoryList(List<Story> storyList) {
        this.storyList = storyList;
    }
}
