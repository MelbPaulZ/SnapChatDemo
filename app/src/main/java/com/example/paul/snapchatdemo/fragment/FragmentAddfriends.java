package com.example.paul.snapchatdemo.fragment;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Anita on 2016/9/26.
 */
public class FragmentAddfriends extends Fragment implements View.OnClickListener,GestureDetector.OnGestureListener {
    private View root;
    private Button buttonAddress;
    private Button buttonNearby;
    private Button buttonUsername;
    private GestureDetector detector;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_addfriends, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMemories();
        detector = new GestureDetector(getContext(),this);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }


    public void initMemories(){
        buttonAddress = (Button) root.findViewById(R.id.button_addressbook);
        buttonAddress.setOnClickListener(this);
        buttonNearby = (Button) root.findViewById(R.id.button_nearby);
        buttonNearby.setOnClickListener(this);
        buttonUsername = (Button) root.findViewById(R.id.button_username);
        buttonUsername.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_addressbook:
                ((MainActivity)getActivity()).addFriendsToAddAddress();
                break;
            case R.id.button_nearby:
                break;
            case R.id.button_username:
                ((MainActivity)getActivity()).addFriendsToAddUsername();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
        if (distanceY>10){
            ((MainActivity)getActivity()).addFriendsToContact();
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
