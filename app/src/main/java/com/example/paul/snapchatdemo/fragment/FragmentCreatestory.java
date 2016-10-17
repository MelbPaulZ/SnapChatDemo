package com.example.paul.snapchatdemo.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.PhotoStory;
import com.example.paul.snapchatdemo.firebase.FirebaseStorageService;
import com.example.paul.snapchatdemo.utils.HttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anita on 2016/10/16.
 */
public class FragmentCreatestory extends Fragment implements View.OnClickListener{
    private View root;
    private final String TAG = "CreateStory";

    private EditText storyText;
    private RadioGroup mChoiceMode;
    private Button createStoryNow;
    private Button createstoryBack;
    private String isSecret;
    private String story_text;
    private String uploadImg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_createstory, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMyStory();
    }


    public void initMyStory(){
        createStoryNow = (Button) root.findViewById(R.id.create_my_story);
        createStoryNow.setOnClickListener(this);
        storyText = (EditText) root.findViewById(R.id.story_text);
        createstoryBack = (Button) root.findViewById(R.id.createstoryBt);
        createstoryBack.setOnClickListener(this);

        mChoiceMode = (RadioGroup) root.findViewById(R.id.choice_mode);
        mChoiceMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.secret_rb){
                    isSecret="1";
                }else{
                    isSecret="0";
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_my_story:
                story_text = storyText.getText().toString();
                //String path=((MainActivity)getActivity()).getAbsolutePath();
                uploadImg=((MainActivity)getActivity()).getImageUrl();
                System.out.println("uploadImg:"+uploadImg);
                //uploadImg=uploadImage(path);
                if(uploadImg==null){
                    uploadImg="http://seeklogo.com/images/S/snapchat-ghost-logo-B618EE0704-seeklogo.com.png";
                }

                String id= ((MainActivity)getActivity()).getUserId();
                Toast.makeText(FragmentCreatestory.this.getActivity().getBaseContext(), "creating now...", Toast.LENGTH_SHORT).show();

                // get remote service
                UserApi userApi = HttpUtil.accessServer(UserApi.class);

                // this is for getting data back, asynchronous doing this task
                userApi.createstory(id, uploadImg, isSecret,story_text, C.methods.METHOD_CREATESTORY).enqueue(new Callback<ArrayList<PhotoStory>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PhotoStory>> call, Response<ArrayList<PhotoStory>> response) {
                        Log.i(TAG, "onResponse: " + response.body().toString());
                        Toast.makeText(FragmentCreatestory.this.getActivity().getBaseContext(),
                                "create story successfully", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ArrayList<PhotoStory>> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + "userApi failure");
                        Toast.makeText(FragmentCreatestory.this.getActivity().getBaseContext(),
                                "create story failed", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.createstoryBt:
                ((MainActivity)getActivity()).fromCreateStoryToMemory();
                break;
            default:
                break;
        }
    }
    public String uploadImage(String path) {
        String pathPrefix="file://";
        String localImageFile=pathPrefix+path;
        //String saveImgPath= "/storage/emulated/0/DCIM/100ANDRO/1.JPG";
        //boolean flag=compressBiamp(bitmap,saveImgPath,30);
        //String ImageFile=pathPrefix+saveImgPath;
        String urlDownload = FirebaseStorageService.uploadImage(localImageFile);
/*        while(urlDownload==null){
            urlDownload=FirebaseStorageService.uploadImage(localImageFile);
            if(urlDownload!=null){
                break;
            }
        }*/
        System.out.println("urlDownloadMemories:"+urlDownload);
        return urlDownload;
    }
}
