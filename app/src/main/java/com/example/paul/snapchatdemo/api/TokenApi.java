package com.example.paul.snapchatdemo.api;

import com.example.paul.snapchatdemo.bean.User;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TokenApi {

    @PUT("register_token.php?")
    Call<User> registerToken(@Query("token") String token,
                           @Query("user_id") String userId);
}
