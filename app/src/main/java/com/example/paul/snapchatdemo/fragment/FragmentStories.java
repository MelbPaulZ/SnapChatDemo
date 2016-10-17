package com.example.paul.snapchatdemo.fragment;

import android.app.SearchManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.adapters.StoryAdapter;
import com.example.paul.snapchatdemo.bean.DiscoveryUrl;
import com.example.paul.snapchatdemo.helpers.OnPageSlideListener;
import com.example.paul.snapchatdemo.manager.StoryManager;
import com.example.paul.snapchatdemo.manager.UrlManager;
import com.example.paul.snapchatdemo.widgets.ExpandedListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Paul on 7/09/2016.
 */
public class FragmentStories extends Fragment implements SearchView.OnQueryTextListener,View.OnClickListener, OnPageSlideListener {
    private View root;
    private StoryAdapter storyAdapter;
    private CustomTabsIntent customTabsIntent;

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
        initListeners();
    }

    public void initSubscriptionScrollView() {
        LinearLayout subscriptionContentLayout = (LinearLayout) root.findViewById(R.id.subscription_content_layout);

        for (final DiscoveryUrl discoveryUrl : UrlManager.getInstance().getUrls()) {
            ImageView imageView = new ImageView(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(800, 800));
            imageView.setLayoutParams(params);
            subscriptionContentLayout.addView(imageView);
            Picasso.with(getContext()).load(discoveryUrl.getUrl()).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoUrl(UrlManager.getInstance().getUrls().indexOf(discoveryUrl));
                }
            });
        }
    }



    public void initRecommendTopics(){
        ImageView imageView1 = (ImageView) root.findViewById(R.id.stories_image1);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(0).getUrl()).into(imageView1);
        imageView1.setOnClickListener(this);

        ImageView imageView2 = (ImageView) root.findViewById(R.id.stories_image2);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(1).getUrl()).into(imageView2);
        imageView2.setOnClickListener(this);

        ImageView imageView3 = (ImageView) root.findViewById(R.id.stories_image3);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(2).getUrl()).into(imageView3);
        imageView3.setOnClickListener(this);

        ImageView imageView4 = (ImageView) root.findViewById(R.id.stories_image4);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(3).getUrl()).into(imageView4);
        imageView4.setOnClickListener(this);

        ImageView imageView5 = (ImageView) root.findViewById(R.id.stories_image5);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(4).getUrl()).into(imageView5);
        imageView5.setOnClickListener(this);

        ImageView imageView6 = (ImageView) root.findViewById(R.id.stories_image6);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(5).getUrl()).into(imageView6);
        imageView6.setOnClickListener(this);

        ImageView imageView7 = (ImageView) root.findViewById(R.id.stories_image7);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(6).getUrl()).into(imageView7);
        imageView7.setOnClickListener(this);

        ImageView imageView8 = (ImageView) root.findViewById(R.id.stories_image8);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(7).getUrl()).into(imageView8);
        imageView8.setOnClickListener(this);
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

    private void initListeners(){
        TextView left = (TextView) root.findViewById(R.id.story_left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movePrevious();
            }
        });

        TextView right = (TextView) root.findViewById(R.id.story_right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveNext();
            }
        });
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

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        Bitmap icon = BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_arrow_back);
        builder.setCloseButtonIcon(icon);
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        customTabsIntent = builder.build();

        switch (viewId){
            case R.id.stories_image1:{
                gotoUrl(0);
                break;
            }case R.id.stories_image2:{
                gotoUrl(1);
                break;
            }case R.id.stories_image3:{
                gotoUrl(2);
                break;
            }case R.id.stories_image4:{
                gotoUrl(3);
                break;
            }case R.id.stories_image5:{
                gotoUrl(4);
                break;
            }case R.id.stories_image6:{
                gotoUrl(5);
                break;
            }case R.id.stories_image7:{
                gotoUrl(6);
                break;
            }case R.id.stories_image8:{
                gotoUrl(7);
                break;
            }
        }
    }

    private void gotoUrl(int index){
        String url = UrlManager.getInstance().getUrls().get(index).getTextUrl();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }

    @Override
    public void moveNext() {
        ViewPager viewPager = ((MainActivity)getActivity()).getFragmentMain().getViewPager();
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    @Override
    public void movePrevious() {
        ViewPager viewPager = ((MainActivity)getActivity()).getFragmentMain().getViewPager();
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
    }
}
