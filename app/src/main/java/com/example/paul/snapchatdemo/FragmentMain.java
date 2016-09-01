package com.example.paul.snapchatdemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Paul on 1/09/2016.
 */
public class FragmentMain extends Fragment {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    FragmentDiscover fragmentDiscover;
    FragmentContactList fragmentContactList;

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
        fragmentContactList = new FragmentContactList();
        simulateChatListData(fragmentContactList);

        //put the fragment in the arrayList, so can use view pager to slide
        fragments.add(fragmentContactList);
        fragments.add(fragmentDiscover);


        ViewPager viewPager = (ViewPager) root.findViewById(R.id.main_view_pager);

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

    // this is for the contact simulate data, if you want to simulate some data, can put another function
    public void simulateChatListData(FragmentContactList fragmentContactList){
        ArrayList<String> names = new ArrayList<>();
        for(int i =0;i<5;i++){
            names.add("tom");
            names.add("paul");
            names.add("peter");
            names.add("asdd");
        }
        fragmentContactList.setNames(names);
    }

}
