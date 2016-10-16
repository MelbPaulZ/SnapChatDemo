package com.example.paul.snapchatdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.Friendship;
import com.example.paul.snapchatdemo.utils.HttpUtil;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anita on 2016/9/26.
 */
public class FragmentResultFriend extends Fragment implements View.OnClickListener{
    private View root;
    private Button addFriend;
    private TextView friendName;
    private String id;
    private String username;
    private String friendId;
    private String friendUsername;
    private Button backToUserscreen;
    private final String TAG = "ResultFriend";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_result_friend, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMemories();
    }


    public void initMemories(){
        addFriend = (Button) root.findViewById(R.id.add_button);
        friendName = (TextView) root.findViewById(R.id.friend_name);
        backToUserscreen =(Button)root.findViewById(R.id.resultFriendBtBack);
        String friendUsername= ((MainActivity)getActivity()).getFriendUsername();
        friendName.setText(friendUsername);
        addFriend.setOnClickListener(this);
        backToUserscreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                id=((MainActivity)getActivity()).getUserId();
                username=((MainActivity)getActivity()).getUsername();
                friendId=((MainActivity)getActivity()).getFriend_userid();
                friendUsername=((MainActivity)getActivity()).getFriendUsername();

                Toast.makeText(FragmentResultFriend.this.getActivity().getBaseContext(), "sending request...", Toast.LENGTH_SHORT).show();

                // get remote service
                UserApi userApi = HttpUtil.accessServer(UserApi.class);

                // this is for getting data back, asynchronous doing this task
                userApi.addusername(id,username,friendId,friendUsername, C.methods.METHOD_ADDUSERNAME).enqueue(new Callback<Friendship>() {
                    @Override
                    public void onResponse(Call<Friendship> call, Response<Friendship> response) {
                        Log.i(TAG, "onResponse: " + response.body().toString());
                        Toast.makeText(FragmentResultFriend.this.getActivity().getBaseContext(), "send successfully", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Friendship> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + "userApi failure");
                    }
                });

                break;
            case R.id.resultFriendBtBack:
                ((MainActivity)getActivity()).fromResultFriendToUserscreen();
                break;
            default:
                break;
        }
    }
}
