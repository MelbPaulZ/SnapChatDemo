package com.example.paul.snapchatdemo.api;

import com.example.paul.snapchatdemo.bean.FriendPhone;
import com.example.paul.snapchatdemo.bean.Friendship;
import com.example.paul.snapchatdemo.bean.PhotoStory;
import com.example.paul.snapchatdemo.bean.User;

import java.util.ArrayList;

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
    Call<User> register(@Query("token") String token , @Query("username") String username, @Query("password") String password);

    // this is for user login
    @GET("login.php?")
    Call<User> login(@Query("username") String username, @Query("password") String password, @Query("method") String method);

    //this is for add friends by username
    @PUT("addusername.php?")
    Call<Friendship> addusername(@Query("id") String id, @Query("username") String username,
                                 @Query("friendId") String friendId, @Query("friendUsername") String friendUsername,
                                 @Query("method") String method);

    //this is for create story
    @PUT("createstory.php?")
    Call<ArrayList<PhotoStory>> createstory(@Query("id") String id, @Query("image") String image,
                                 @Query("method") String method);

    //this is for agree add friend request
    @PUT("addedmeagree.php?")
    Call<Friendship> addedmeagree(@Query("id") String id, @Query("friendUsername") String friendUsername, @Query("method") String method);

    //this is for search friends by username
    @GET("searchusername.php?")
    Call<User> searchusername(@Query("username") String username, @Query("method") String method);

    //this is for search friends by telephone number
    @GET("searchtelephone.php?")
    Call<User> searchtelephone(@Query("telephone") String telephone, @Query("method") String method);

    //this is for search friends which want to add me
    @GET("addedme.php?")
    Call<ArrayList<FriendPhone>> addedme(@Query("userid") String userid, @Query("method") String method);
}
