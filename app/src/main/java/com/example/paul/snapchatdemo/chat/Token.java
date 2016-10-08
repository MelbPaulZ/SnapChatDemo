package com.example.paul.snapchatdemo.chat;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Token {
    public static String generateToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        return token;
    }

    public static void registerToken(String token, String userId) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                                    .add("token",token)
                                    .add("user_id",userId)
                                    .build();
        Request request = new Request.Builder().url("http://115.146.85.253/register_token.php").post(body).build();

        try {
            client.newCall(request).execute();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
