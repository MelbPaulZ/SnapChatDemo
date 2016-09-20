package com.example.paul.snapchatdemo.api;

import com.example.paul.snapchatdemo.bean.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Paul on 20/09/2016.
 */
public interface UserApi {
    // this should for user register
    @PUT("register3.php?")
    Call<User> register(@Query("id") String id, @Query("token") String token ,
                   @Query("username") String username, @Query("method") String method);

    // this is for user login
    @GET("login.php?")
    Call<User> login(@Query("username") String username, @Query("method") String method);
}
