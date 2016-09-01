package com.example.paul.snapchatdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    FragmentDiscover fragmentDiscover;
    FragmentContactList fragmentContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAll();
    }

    private void initAll(){
        // new all your fragment here
        fragmentDiscover = new FragmentDiscover();
        fragmentContactList = new FragmentContactList();
        simulateChatListData(fragmentContactList);

        //put the fragment in the arrayList, so can use view pager to slide
        fragments.add(fragmentContactList);
        fragments.add(fragmentDiscover);


        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
