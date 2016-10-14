package com.example.paul.snapchatdemo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.paul.snapchatdemo.activity.LoginActivity;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.chat.Token;
import com.example.paul.snapchatdemo.manager.FriendManager;
import com.example.paul.snapchatdemo.manager.UrlManager;
import com.example.paul.snapchatdemo.utils.HttpUtil;
import com.example.paul.snapchatdemo.utils.UserUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paul on 10/10/16.
 */
public class LoginPresenter {
    private Context context;
    private final String TAG = "LoginPresenter";
    private LoginActivity activity;

    public LoginPresenter(Context context){
        this.context = context;
    }

    public void setActivity(LoginActivity activity){
        this.activity = activity;
    }

    public void login(String userName, String passWord){

        // get remote service
        UserApi userApi = HttpUtil.accessServer(UserApi.class);

        // this is for getting data back, asynchronous doing this task
        userApi.login(userName, passWord, C.methods.METHOD_LOGIN).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User loginUser = response.body();
                loginUser.toString();
                storyUserInfo(loginUser.getUserName(), loginUser.getId(), loginUser.getToken());
                FriendManager.getInstance().setFriendList(loginUser.getFriends());
                UrlManager.getInstance().setUrls(loginUser.getDiscoveryUrls());
                activity.loginSuccessful();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Login failure, please retry", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signup(String username, String password){
        UserApi userApi = HttpUtil.accessServer(UserApi.class);
        userApi.register(Token.generateToken(), username, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(context, "Registration successful, please login", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Registration failed, please retry", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storyUserInfo(String name, String id, String token){
        UserUtil.setUsername(name);
        UserUtil.setId(id);
        UserUtil.setToken(token);
    }
}
