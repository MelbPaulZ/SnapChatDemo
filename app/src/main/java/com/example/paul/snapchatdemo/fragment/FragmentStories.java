package com.example.paul.snapchatdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.paul.snapchatdemo.widgets.ExpandedListView;
import com.squareup.picasso.Picasso;

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

        initFriendStoriesListView();
        initRecommendTopics();
        initSubscriptionScrollView();
    }

    public void initSubscriptionScrollView() {
        LinearLayout subscriptionContentLayout = (LinearLayout) root.findViewById(R.id.subscription_content_layout);
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg");
        urls.add("http://rter.info/image.php?iso=AUD&name=1-8cc9325417c87e4405fe57b8f371582c");
        urls.add("http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg");

        for (int i = 0; i < urls.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(800, 800));
            imageView.setLayoutParams(params);
            subscriptionContentLayout.addView(imageView);
            Picasso.with(getContext()).load(urls.get(i)).into(imageView);
        }
    }


    public void initRecommendTopics(){
        ImageView imageView1 = (ImageView) root.findViewById(R.id.stories_image1);
        Picasso.with(getContext()).load("http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg").into(imageView1);
        ImageView imageView2 = (ImageView) root.findViewById(R.id.stories_image2);
        Picasso.with(getContext()).load("http://rter.info/image.php?iso=AUD&name=1-8cc9325417c87e4405fe57b8f371582c").into(imageView2);
        ImageView imageView3 = (ImageView) root.findViewById(R.id.stories_image3);
        ImageView imageView4 = (ImageView) root.findViewById(R.id.stories_image4);
        ImageView imageView5 = (ImageView) root.findViewById(R.id.stories_image5);
        ImageView imageView6 = (ImageView) root.findViewById(R.id.stories_image6);
        ImageView imageView7 = (ImageView) root.findViewById(R.id.stories_image7);
        ImageView imageView8 = (ImageView) root.findViewById(R.id.stories_image8);
    }

    public void initFriendStoriesListView() {
        ArrayList<String> friendStories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            friendStories.add(String.format("this is friend stories %d", i));
        }
        ArrayAdapter<String> friendStoriesAdapter = new ArrayAdapter<String>(getContext(), R.layout.friend_stories_one_line, R.id.friend_stories_text, friendStories);
        ExpandedListView friendStoriesListView = (ExpandedListView) root.findViewById(R.id.friend_stories);

        friendStoriesListView.setAdapter(friendStoriesAdapter);
    }
}
