package com.example.paul.snapchatdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.utils.HttpUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anita on 2016/9/26.
 */
public class FragmentAddusername extends Fragment implements View.OnClickListener{
    private View root;
    private Button buttonUser;
    private EditText editUsername;
    private String userName;
    private Button buttonBackToAddfriends;
    private final String TAG = "ADDusername";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_addusername, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUsername();
    }


    public void initUsername(){
        buttonUser = (Button) root.findViewById(R.id.button_user);
        editUsername = (EditText) root.findViewById(R.id.edit_username);
        buttonUser.setOnClickListener(this);
        buttonBackToAddfriends =(Button)root.findViewById(R.id.addusernameBtBack);
        buttonBackToAddfriends.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_user:
                userName = editUsername.getText().toString();
                Toast.makeText(FragmentAddusername.this.getActivity().getBaseContext(), "searching now...", Toast.LENGTH_SHORT).show();
                // get remote service
                UserApi userApi = HttpUtil.accessServer(UserApi.class);

                // this is for getting data back, asynchronous doing this task
                userApi.searchusername(userName, C.methods.METHOD_SEARCHUSERNAME).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.i(TAG, "onResponse: " + response.body().toString());
                        String friendusername=response.body().getUserName();
                        String frienduserid=response.body().getId();
                        ((MainActivity)getActivity()).setFriendUsername(friendusername);
                        ((MainActivity)getActivity()).setFriend_userid(frienduserid);
                        // if server response data successfully, start main activity
                        ((MainActivity)getActivity()).fromAddusernameToResultfriend();

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + "userApi failure");
                        Toast.makeText(FragmentAddusername.this.getActivity().getBaseContext(),
                                "cannot find user with this username", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.addusernameBtBack:
                ((MainActivity)getActivity()).fromAddusernameToAddfriends();
                break;
            default:
                break;
        }
    }
}
