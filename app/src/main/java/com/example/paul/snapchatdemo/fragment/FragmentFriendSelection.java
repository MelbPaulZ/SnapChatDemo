package com.example.paul.snapchatdemo.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.adapters.ContactAdapter;
import com.example.paul.snapchatdemo.api.ChatApi;
import com.example.paul.snapchatdemo.bean.Friend;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.chat.ChatMessageModel;
import com.example.paul.snapchatdemo.manager.FriendManager;
import com.example.paul.snapchatdemo.utils.HttpUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paul on 9/10/16.
 */
public class FragmentFriendSelection extends Fragment implements View.OnClickListener{
    private View root;
    private TextView backBtn, nextBtn;
    private ContactAdapter contactAdapter;

    private StorageReference mStorageRef;
    public String imageFilePath;
    public String imageTimer;
    ProgressBar loadingPanel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_friend_selection, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListeners();
        initContent();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        loadingPanel = (ProgressBar)root.findViewById(R.id.loadingPanel);
    }

    public void initListeners(){
        backBtn = (TextView) root.findViewById(R.id.friend_selection_back_btn);
        backBtn.setOnClickListener(this);
        nextBtn = (TextView) root.findViewById(R.id.friend_selection_done_btn);
        nextBtn.setOnClickListener(this);
    }

    public void initContent(){
        ListView contactList = (ListView) root.findViewById(R.id.friend_selection_listview);
        contactAdapter = new ContactAdapter(getContext(), R.layout.friend_selection_single_line, FriendManager.getInstance().getFriendList());
        contactList.setAdapter(contactAdapter);
    }



    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.friend_selection_back_btn:
                // set the back listener here
                ((MainActivity)getActivity()).fromFriendSelectionToMemory();
                break;
            case R.id.friend_selection_done_btn:
                // set the done listener here
                List<Friend> friendList = FriendManager.getInstance().getSelectedFriendList(); // that's the friend list has been selected

                // upload to firebase and send to list of friends
                sendImage(friendList);

                break;
            default:
                Toast.makeText(getContext(), "don't know what is clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void sendImage(final List<Friend> friendList) {
        String pathPrefix = "file://";

        // upload image to firebase
        final String localImageFileName = pathPrefix+imageFilePath;
        String fileName = UUID.randomUUID().toString();

        final StorageReference serverPhotoRef = mStorageRef.child("photos").child(fileName);
        Uri fileUri = Uri.parse(localImageFileName);
        serverPhotoRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadURL = taskSnapshot.getMetadata().getDownloadUrl();
                        // send the image url to friends
                        String senderUserId = ((MainActivity)getActivity()).getUserId();
                        String messageType = ChatMessageModel.MSG_DATA_IMG;
                        final int[] sendImageQueueSize = {friendList.size()};
                        final boolean[] isContinueSendingImage = {true};

                        for (Friend friend : friendList) {
                            if (isContinueSendingImage[0]) {
                                String receiverUserId = friend.getId();

                                // send message to server
                                ChatApi chatApi = HttpUtil.accessServer(ChatApi.class);
                                chatApi.sendMessage(senderUserId, receiverUserId, downloadURL.toString(), messageType, imageTimer).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        sendImageQueueSize[0] = sendImageQueueSize[0] - 1;

                                        if (sendImageQueueSize[0]==0) {
                                            loadingPanel.setVisibility(View.GONE);
                                            ((MainActivity)getActivity()).fromFriendSelectionToContact();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(getContext(), "Send image is failed, check your network connection.", Toast.LENGTH_SHORT).show();

                                        // break the looop
                                        isContinueSendingImage[0] = false;
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // upload image failed
                        loadingPanel.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Send image is failed, check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        loadingPanel.bringToFront();
                        loadingPanel.setVisibility(View.VISIBLE);
                    }
                });

    }
}
