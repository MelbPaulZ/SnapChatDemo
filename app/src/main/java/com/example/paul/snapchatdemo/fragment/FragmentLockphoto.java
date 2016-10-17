package com.example.paul.snapchatdemo.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.PhotoStory;
import com.example.paul.snapchatdemo.utils.HttpUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anita on 2016/10/17.
 */
public class FragmentLockphoto extends Fragment implements View.OnClickListener{
    private View root;
    private Button buttonLockPhoto;
    private Button buttonSecretAlbum;
    private Button buttonBackToMemory;
    private EditText passwordSecret;
    private String downloadUrl;
    private StorageReference mStorageRef;
    private final String TAG = "LockPhoto";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_lockphoto, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        initLockPhoto();
    }


    public void initLockPhoto(){
        buttonLockPhoto = (Button) root.findViewById(R.id.button_lock_photo);
        buttonLockPhoto.setOnClickListener(this);
        buttonSecretAlbum=(Button)root.findViewById(R.id.button_lock_album);
        buttonSecretAlbum.setOnClickListener(this);
        buttonBackToMemory=(Button)root.findViewById(R.id.lockphotoBtBack);
        buttonBackToMemory.setOnClickListener(this);
        passwordSecret = (EditText)root.findViewById(R.id.password_secret);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_lock_photo:
                String path=((MainActivity)getActivity()).getAbsolutePath();
                lockPhoto(path);
                //((MainActivity)getActivity()).fromAddfriendsToAddaddressbook();
                //String uploadImg=((MainActivity)getActivity()).getImageUrl();
                //uploadToDatabase();
                break;
            case R.id.button_lock_album:
                String password = passwordSecret.getText().toString();
                String id= ((MainActivity)getActivity()).getUserId();
                Toast.makeText(FragmentLockphoto.this.getActivity().getBaseContext(), "Opening now...", Toast.LENGTH_SHORT).show();
                // get remote service
                UserApi userApi = HttpUtil.accessServer(UserApi.class);
                //String testdownloadUrl="test";

                // this is for getting data back, asynchronous doing this task
                userApi.lockalbum(id, password, C.methods.METHOD_LOCKALBUM).enqueue(new Callback<ArrayList<PhotoStory>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PhotoStory>> call, Response<ArrayList<PhotoStory>> response) {
                        Log.i(TAG, "onResponse: " + response.body().toString());
                        Toast.makeText(FragmentLockphoto.this.getActivity().getBaseContext(),
                                "open album successfully", Toast.LENGTH_SHORT).show();
                        //(((MainActivity)getActivity()).setPhotoStory(response.body().to);
                        ((MainActivity)getActivity()).setPhotoStory(response.body());
                        ((MainActivity)getActivity()).fromLockPhotoToSecretAlbum();


                    }

                    @Override
                    public void onFailure(Call<ArrayList<PhotoStory>> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + "userApi failure");
                        Toast.makeText(FragmentLockphoto.this.getActivity().getBaseContext(),
                                "You do not have secret photo or enter the wrong password", Toast.LENGTH_SHORT).show();
                    }
                });
                //((MainActivity)getActivity()).fromAddfriendsToAddusername();
                break;
            case R.id.lockphotoBtBack:
                ((MainActivity)getActivity()).fromLockphotoToMemory();
                break;
            default:
                break;
        }
    }

    public void lockPhoto(String path){
        String pathPrefix = "file://";
        String localImageFile=pathPrefix+path;
        String fileName = UUID.randomUUID().toString();
        final StorageReference serverPhotoRef = mStorageRef.child("photos").child(fileName);
        Uri fileUri = Uri.parse(localImageFile);
        serverPhotoRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                        downloadUrl=downloadUri.toString();
                        System.out.println("Connect to server downloadUrl:"+downloadUrl);
                        //uploadToDatabase();
                        if(downloadUrl==null){
                            downloadUrl="http://seeklogo.com/images/S/snapchat-ghost-logo-B618EE0704-seeklogo.com.png";
                        }

                        String id= ((MainActivity)getActivity()).getUserId();
                        Toast.makeText(FragmentLockphoto.this.getActivity().getBaseContext(), "locking now...", Toast.LENGTH_SHORT).show();

                        String password="123456";

                        // get remote service
                        UserApi userApi = HttpUtil.accessServer(UserApi.class);
                        //String testdownloadUrl="test";

                        // this is for getting data back, asynchronous doing this task
                        userApi.lockphoto(id, downloadUrl, password, C.methods.METHOD_LOCKPHOTO).enqueue(new Callback<ArrayList<PhotoStory>>() {
                            @Override
                            public void onResponse(Call<ArrayList<PhotoStory>> call, Response<ArrayList<PhotoStory>> response) {
                                //Log.i(TAG, "onResponse: " + response.body().toString());
                                Toast.makeText(FragmentLockphoto.this.getActivity().getBaseContext(),
                                        "lock photo successfully", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<ArrayList<PhotoStory>> call, Throwable t) {
                                Log.i(TAG, "onFailure: " + "userApi failure");
                                Toast.makeText(FragmentLockphoto.this.getActivity().getBaseContext(),
                                        "lock photo failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });

    }

/*    private void uploadToDatabase(){
        System.out.println(downloadUrl);
        if(downloadUrl==null){
            downloadUrl="http://seeklogo.com/images/S/snapchat-ghost-logo-B618EE0704-seeklogo.com.png";
        }

        String id= ((MainActivity)getActivity()).getUserId();
        Toast.makeText(FragmentLockphoto.this.getActivity().getBaseContext(), "locking now...", Toast.LENGTH_SHORT).show();

        String password="123456";

        // get remote service
        UserApi userApi = HttpUtil.accessServer(UserApi.class);
        //String testdownloadUrl="test";

        // this is for getting data back, asynchronous doing this task
        userApi.lockphoto(id, downloadUrl, password, C.methods.METHOD_LOCKPHOTO).enqueue(new Callback<ArrayList<PhotoStory>>() {
            @Override
            public void onResponse(Call<ArrayList<PhotoStory>> call, Response<ArrayList<PhotoStory>> response) {
                //Log.i(TAG, "onResponse: " + response.body().toString());
                Toast.makeText(FragmentLockphoto.this.getActivity().getBaseContext(),
                        "lock photo successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ArrayList<PhotoStory>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + "userApi failure");
                Toast.makeText(FragmentLockphoto.this.getActivity().getBaseContext(),
                        "lock photo failed", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

}
