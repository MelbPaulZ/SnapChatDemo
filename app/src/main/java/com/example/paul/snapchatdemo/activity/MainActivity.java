package com.example.paul.snapchatdemo.activity;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.fragment.FragmentAddaddressbook;
import com.example.paul.snapchatdemo.fragment.FragmentAddfriends;
import com.example.paul.snapchatdemo.fragment.FragmentAddusername;
import com.example.paul.snapchatdemo.fragment.FragmentChat;
import com.example.paul.snapchatdemo.fragment.FragmentMain;

public class MainActivity extends AppCompatActivity {
    private FragmentMain fragmentMain;
    private FragmentAddfriends fragmentAddfriends;
    private FragmentAddaddressbook fragmentAddaddressbook;
    private FragmentAddusername fragmentAddusername;
    private FragmentChat fragmentChat;

    public static boolean isAppCreated = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentMain).commit();

    }

    public void initFragments(){
        fragmentMain = new FragmentMain();
        fragmentAddfriends = new FragmentAddfriends();
        fragmentAddaddressbook = new FragmentAddaddressbook();
        fragmentAddusername = new FragmentAddusername();

    }

    public FragmentMain getFragmentMain(){
        return fragmentMain;
    }

    public void contactToAddFriends(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).hide(fragmentMain).commit();
        if (fragmentAddfriends.isAdded()){
            if (fragmentAddfriends.isHidden()) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).show(fragmentAddfriends).commit();
            }
        }else{
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).add(R.id.main_frame, fragmentAddfriends).commit();
        }
    }

    public void addFriendsToContact(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).hide(fragmentAddfriends).commit();
        if (fragmentMain.isAdded()){
            if (fragmentMain.isHidden()){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).show(fragmentMain).commit();
            }
        }else{
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).add(R.id.main_frame, fragmentMain).commit();
        }
    }

    public void addFriendsToAddAddress(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddfriends).commit();
        if (fragmentAddaddressbook.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddaddressbook).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentAddaddressbook).commit();
        }
    }

    public void addFriendsToAddUsername(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddfriends).commit();
        if (fragmentAddusername.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddusername).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentAddusername).commit();
        }
    }

}
