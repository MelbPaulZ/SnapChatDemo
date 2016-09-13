package com.example.paul.snapchatdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.paul.snapchatdemo.helpers.SubscriptionAdapter;
import com.example.paul.snapchatdemo.widgets.NonScrollListView;

import java.util.ArrayList;

/**
 * Created by Paul on 7/09/2016.
 */
public class FragmentStories extends Fragment {
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_stories, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSubscriptionListView();
        initFriendStoriesListView();
    }

    public void initSubscriptionListView(){
        ArrayList<String>  urls = new ArrayList<>();
        urls.add("http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg");
        urls.add("http://rter.info/image.php?iso=AUD&name=1-8cc9325417c87e4405fe57b8f371582c");
        SubscriptionAdapter urlArrayAdapter = new SubscriptionAdapter(getContext(), R.layout.subscription_oneline, urls);
        NonScrollListView subscriptionList = (NonScrollListView) root.findViewById(R.id.subscription_listview);
        subscriptionList.setAdapter(urlArrayAdapter);
    }

    public void initFriendStoriesListView(){
        ArrayList<String> friendStories = new ArrayList<>();
        friendStories.add("this is friend stories 1");
        friendStories.add("this is friend stories 2");
        ArrayAdapter<String> friendStoriesAdapter = new ArrayAdapter<String>(getContext(), R.layout.friend_stories_one_line, R.id.friend_stories_text, friendStories);
        NonScrollListView friendStoriesListView = (NonScrollListView) root.findViewById(R.id.friend_stories);
        friendStoriesListView.setAdapter(friendStoriesAdapter);
    }
}
