package com.example.paul.snapchatdemo.api;

import com.example.paul.snapchatdemo.bean.Story;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Paul on 10/10/16.
 */
public interface StoryApi {
    @POST("story.php?")
    Call<List<Story>> getFriendStories(@Query("id") String userId);
}
