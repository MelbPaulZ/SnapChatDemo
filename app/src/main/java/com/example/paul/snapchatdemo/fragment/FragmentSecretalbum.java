package com.example.paul.snapchatdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.adapters.FriendPhoneAdapter;
import com.example.paul.snapchatdemo.adapters.PhotoStoryAdapter;
import com.example.paul.snapchatdemo.bean.FriendPhone;
import com.example.paul.snapchatdemo.bean.PhotoStory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anita on 2016/10/17.
 */
public class FragmentSecretalbum extends Fragment implements View.OnClickListener{
    private View root;
    private final String TAG = "SecretAlbum";
    private List<PhotoStory> photoStoryList = new ArrayList<>();
    private Button lockalbumBt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_lockalbum, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSecretAlbum();

    }


    public void initSecretAlbum(){
        lockalbumBt =(Button)root.findViewById(R.id.lockalbumBt);
        lockalbumBt.setOnClickListener(this);
        ListView photoStoryAlbum = (ListView) root.findViewById(R.id.lockalbumLv);
        photoStoryList=(ArrayList<PhotoStory>)(((MainActivity)getActivity()).getPhotoStory().clone());

        PhotoStoryAdapter phtotstoryAdpater = new PhotoStoryAdapter(getContext(), R.layout.secretalbum_single_line_view, photoStoryList);
        photoStoryAlbum.setAdapter(phtotstoryAdpater);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lockalbumBt:
                ((MainActivity)getActivity()).fromSecretalbumToLockphoto();
                break;
            default:
                break;
        }
    }
}
