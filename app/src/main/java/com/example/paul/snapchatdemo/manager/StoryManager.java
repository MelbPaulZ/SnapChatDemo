package com.example.paul.snapchatdemo.manager;

import com.example.paul.snapchatdemo.bean.Friend;
import com.example.paul.snapchatdemo.bean.Story;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.utils.UserUtil;

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

    public String getFriendName(String id){
        if (UserUtil.getId().equals(id)){
            return UserUtil.getUsername();
        }else{
            for (Friend friend : FriendManager.getInstance().getFriendList()){
                if (friend.getId().equals(id)){
                    return friend.getName();
                }
            }
            return "not find this friend";
        }
    }

    public boolean isSecretBlocked(String userId, Story story){
        if (userId.equals(story.getId())){
            return false;
        }else{
            if (story.getIsSecret()==Story.STORY_SECRET){
                return true;
            }else{
                return false;
            }
        }

    }
}
