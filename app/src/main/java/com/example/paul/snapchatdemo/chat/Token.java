package com.example.paul.snapchatdemo.chat;

import com.example.paul.snapchatdemo.api.TokenApi;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.utils.HttpUtil;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Token {
    public static String generateToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        return token;
    }

    public static void registerToken(String token, String userId) {
        TokenApi tokenApi = HttpUtil.accessServer(TokenApi.class);
        tokenApi.registerToken(token, userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // do nothing
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // do nothing
            }
        });
    }
}
