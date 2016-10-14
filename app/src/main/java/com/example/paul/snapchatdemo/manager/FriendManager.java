package com.example.paul.snapchatdemo.manager;

import com.example.paul.snapchatdemo.bean.Friend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Paul on 22/09/2016.
 */
public class FriendManager {
    private static FriendManager ourInstance = new FriendManager();
    private List<Friend> friendList = new ArrayList<>();
    private List<Friend> selectedFriendList = new ArrayList<>();

    public static FriendManager getInstance() {
        return ourInstance;
    }

    private FriendManager() {
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }

    public List<String> getFriendNameListStr(){
        List<String> ls = new ArrayList<>();
        for (Friend friend: friendList){
            ls.add(friend.getName());
        }
        return ls;
    }

    public List<Friend> getSelectedFriendList() {
        return selectedFriendList;
    }

    public void setSelectedFriendList(List<Friend> selectedFriendList) {
        this.selectedFriendList = selectedFriendList;
    }

    public boolean isFriendSelected(Friend friend){
        return selectedFriendList.contains(friend);
    }

    public String friendShipTime(long becomeFriendTime){
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        if (now - becomeFriendTime < 1000 * 3600){
            return "become friend just now";
        }else if (now - becomeFriendTime <= 1000 * 3600 * 24){
            return "new friend";
        }else
            return "old friend";
    }

}
