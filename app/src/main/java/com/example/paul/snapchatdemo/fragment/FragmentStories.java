package com.example.paul.snapchatdemo.fragment;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.adapters.StoryAdapter;
import com.example.paul.snapchatdemo.bean.DiscoveryUrl;
import com.example.paul.snapchatdemo.manager.StoryManager;
import com.example.paul.snapchatdemo.manager.UrlManager;
import com.example.paul.snapchatdemo.widgets.ExpandedListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Paul on 7/09/2016.
 */
public class FragmentStories extends Fragment implements SearchView.OnQueryTextListener {
    private View root;
    private StoryAdapter storyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_stories, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFriendStoriesListView();
        initRecommendTopics();
        initSubscriptionScrollView();
        initSearch();
    }

    public void initSubscriptionScrollView() {
        LinearLayout subscriptionContentLayout = (LinearLayout) root.findViewById(R.id.subscription_content_layout);
        ArrayList<String> urls = new ArrayList<>();
        urls.add(UrlManager.getInstance().getUrls().get(0).getUrl());
        urls.add(UrlManager.getInstance().getUrls().get(1).getUrl());
        urls.add("http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg");

        for (DiscoveryUrl discoveryUrl : UrlManager.getInstance().getUrls()) {
            ImageView imageView = new ImageView(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(800, 800));
            imageView.setLayoutParams(params);
            subscriptionContentLayout.addView(imageView);
            Picasso.with(getContext()).load(discoveryUrl.getUrl()).into(imageView);
        }
    }


    public void initRecommendTopics(){
        ImageView imageView1 = (ImageView) root.findViewById(R.id.stories_image1);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(0).getUrl()).into(imageView1);
        ImageView imageView2 = (ImageView) root.findViewById(R.id.stories_image2);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(1).getUrl()).into(imageView2);
        ImageView imageView3 = (ImageView) root.findViewById(R.id.stories_image3);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(2).getUrl()).into(imageView3);
        ImageView imageView4 = (ImageView) root.findViewById(R.id.stories_image4);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(3).getUrl()).into(imageView4);
        ImageView imageView5 = (ImageView) root.findViewById(R.id.stories_image5);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(4).getUrl()).into(imageView5);
        ImageView imageView6 = (ImageView) root.findViewById(R.id.stories_image6);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(5).getUrl()).into(imageView6);
        ImageView imageView7 = (ImageView) root.findViewById(R.id.stories_image7);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(6).getUrl()).into(imageView7);
        ImageView imageView8 = (ImageView) root.findViewById(R.id.stories_image8);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(7).getUrl()).into(imageView8);
    }

    public void initFriendStoriesListView() {
        storyAdapter = new StoryAdapter(getContext(), R.layout.friend_stories_one_line, StoryManager.getInstance().getStoryList());
        ExpandedListView friendStoriesListView = (ExpandedListView) root.findViewById(R.id.friend_stories);
        friendStoriesListView.setAdapter(storyAdapter);
    }

    private void initSearch(){
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getContext().SEARCH_SERVICE);
        SearchView searchView = (SearchView) root.findViewById(R.id.story_search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        storyAdapter.getFilter().filter(newText);
        return false;
    }
}
