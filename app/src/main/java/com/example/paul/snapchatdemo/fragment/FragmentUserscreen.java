package com.example.paul.snapchatdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.FriendPhone;
import com.example.paul.snapchatdemo.utils.HttpUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anita on 2016/10/3.
 */
public class FragmentUserscreen extends Fragment implements View.OnClickListener,GestureDetector.OnGestureListener{
    private View root;
    private Button buttonAddedme;
    private Button buttonAddfriends;
    private Button buttonMyfriends;
    private TextView tvUsername;
    private GestureDetector detector;
    private final String TAG = "UserScreen";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_userscreen, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUserscreen();
        detector = new GestureDetector(getContext(),this);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }


    public void initUserscreen(){
        tvUsername = (TextView) root.findViewById(R.id.tv_username);
        String username= ((MainActivity)getActivity()).getUsername();
        tvUsername.setText(username);
        buttonAddedme = (Button) root.findViewById(R.id.button_addedme);
        buttonAddedme.setOnClickListener(this);
        buttonAddfriends = (Button) root.findViewById(R.id.button_addfriends);
        buttonAddfriends.setOnClickListener(this);
        buttonMyfriends= (Button) root.findViewById(R.id.button_myfriends);
        buttonMyfriends.setOnClickListener(this);
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
            ((MainActivity)getActivity()).userscreenToContact();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_addedme:
                String userid= ((MainActivity)getActivity()).getUserId();
                // get remote service
                UserApi userApi = HttpUtil.accessServer(UserApi.class);

                // this is for getting data back, asynchronous doing this task
                userApi.addedme(userid, C.methods.METHOD_ADDEDME).enqueue(new Callback<ArrayList<FriendPhone>>() {
                    @Override
                    public void onResponse(Call<ArrayList<FriendPhone>> call, Response<ArrayList<FriendPhone>> response) {
                        Log.i(TAG, "onResponse: " + response.body().toString());
                        ((MainActivity)getActivity()).setFriendPhoneList(response.body());
                        ((MainActivity)getActivity()).fromUserscreenToAddedme();

                    }

                    @Override
                    public void onFailure(Call<ArrayList<FriendPhone>> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + "userApi failure");
                        Toast.makeText(FragmentUserscreen.this.getActivity().getBaseContext(),
                                "no added friends", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.button_addfriends:
                ((MainActivity)getActivity()).fromUserscreenToAddfriends();
                break;
            case R.id.button_myfriends:
                ((MainActivity)getActivity()).fromUserscreenToMyfriends();
                break;
            default:
                break;
        }
    }

}
