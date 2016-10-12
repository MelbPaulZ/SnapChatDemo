package com.example.paul.snapchatdemo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paul.snapchatdemo.R;

import java.util.ArrayList;

/**
 * Created by Paul on 1/09/2016.
 */
public class FragmentMain extends Fragment {

    // test upload

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private FragmentDiscover fragmentDiscover;
    private FragmentContactList fragmentContactList;
    private FragmentStories fragmentStories;
    private FragmentMemories fragmentMemories;
    private FragmentChat fragmentChat;
    private FragmentCamera fragmentCamera;
    private ViewPager viewPager;
    private FragmentFriendSelection fragmentFriendSelection; // this need to put it in elsewhere, only for testing

    private View root;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main_views, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAll();
    }

    private void initAll(){
        // new fragments that can be reached by slide here
        fragmentDiscover = new FragmentDiscover();
        fragmentCamera = new FragmentCamera();
        fragmentContactList = new FragmentContactList();
        fragmentStories = new FragmentStories();
        fragmentMemories = new FragmentMemories();
        fragmentChat = new FragmentChat();
        fragmentFriendSelection = new FragmentFriendSelection();


        //put the fragment in the arrayList, so can use view pager to slide
        fragments.add(fragmentCamera);
        fragments.add(fragmentFriendSelection);
        fragments.add(fragmentChat);
        fragments.add(fragmentContactList);
        fragments.add(fragmentStories);
        fragments.add(fragmentDiscover);
        fragments.add(fragmentMemories);

        viewPager= (ViewPager) root.findViewById(R.id.main_view_pager);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(this.getFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    public ViewPager getViewPager(){
        return this.viewPager;
    }

    public FragmentCamera getFragmentCamera(){
        return this.fragmentCamera;

    }
}
