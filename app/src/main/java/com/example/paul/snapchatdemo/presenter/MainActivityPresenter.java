package com.example.paul.snapchatdemo.presenter;

import android.content.Context;
import android.util.Log;

import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.StoryApi;
import com.example.paul.snapchatdemo.bean.Story;
import com.example.paul.snapchatdemo.manager.StoryManager;
import com.example.paul.snapchatdemo.utils.HttpUtil;
import com.example.paul.snapchatdemo.utils.UserUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paul on 10/10/16.
 */
public class MainActivityPresenter {
    private Context context;
    private MainActivity activity;
    private StoryApi storyApi;
    private final String TAG = "MainActivityPresenter";


    public MainActivityPresenter(Context context){
        this.context = context;
        storyApi = HttpUtil.accessServer(StoryApi.class);
    }

    public void setActivity(MainActivity activity){
        this.activity = activity;
    }

    public void getFriendStroy(){
        storyApi.getFriendStories(UserUtil.getId()).enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                List<Story> storyList = response.body();
                if(storyList!=null) {
                    StoryManager.getInstance().setStoryList(storyList);
                }
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }


}
