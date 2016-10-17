package com.example.paul.snapchatdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.bean.FriendPhone;
import com.example.paul.snapchatdemo.chat.ChatMessageModel;
import com.example.paul.snapchatdemo.firebase.FirebaseMessagingService;
import com.example.paul.snapchatdemo.fragment.FragmentAddaddressbook;
import com.example.paul.snapchatdemo.fragment.FragmentAddedme;
import com.example.paul.snapchatdemo.fragment.FragmentAddfriends;
import com.example.paul.snapchatdemo.fragment.FragmentAddusername;
import com.example.paul.snapchatdemo.fragment.FragmentCamera;
import com.example.paul.snapchatdemo.fragment.FragmentChat;
import com.example.paul.snapchatdemo.fragment.FragmentCreatestory;
import com.example.paul.snapchatdemo.fragment.FragmentFriendSelection;
import com.example.paul.snapchatdemo.fragment.FragmentImageEditor;
import com.example.paul.snapchatdemo.fragment.FragmentMain;
import com.example.paul.snapchatdemo.fragment.FragmentMemories;
import com.example.paul.snapchatdemo.fragment.FragmentResultAddedme;
import com.example.paul.snapchatdemo.fragment.FragmentResultFriend;
import com.example.paul.snapchatdemo.fragment.FragmentShowImageTimer;
import com.example.paul.snapchatdemo.fragment.FragmentUserscreen;
import com.example.paul.snapchatdemo.presenter.MainActivityPresenter;
import com.example.paul.snapchatdemo.utils.UserUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private FragmentMain fragmentMain;
    private FragmentAddfriends fragmentAddfriends;
    private FragmentAddaddressbook fragmentAddaddressbook;
    private FragmentAddusername fragmentAddusername;
    private FragmentChat fragmentChat;
    private FragmentShowImageTimer fragmentShowImageTimer;

    private MainActivityPresenter presenter;

    public static boolean isAppCreated = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private FragmentResultFriend fragmentResultFriend;
    private FragmentAddedme fragmentAddedme;
    private FragmentResultAddedme fragmentResultAddedme;
    private FragmentUserscreen fragmentUserscreen;
    private FragmentCamera fragmentCamera;
    private FragmentImageEditor fragmentImageEditor;
    private FragmentCreatestory fragmentCreatestory;
    private FragmentFriendSelection fragmentFriendSelection;

    private String userId;
    private String username;
    private String friend_username;
    private String friend_userid;
    private ArrayList<FriendPhone> friendPhoneList;
    private String imageUrl;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public ArrayList<FriendPhone> getFriendPhoneList() {
        return friendPhoneList;
    }

    public void setFriendPhoneList(ArrayList<FriendPhone> friendPhone) {
        this.friendPhoneList = (ArrayList<FriendPhone>)friendPhone.clone();
        System.out.println(friendPhoneList.toString());
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getFriend_userid() {
        return friend_userid;
    }

    public void setFriend_userid(String friend_userid) {
        this.friend_userid = friend_userid;
    }

    public String getFriendUsername() {
        return friend_username;
    }

    public void setFriendUsername(String friend_username) {
        this.friend_username = friend_username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUsername(UserUtil.getUsername());
        setUserId(UserUtil.getId());
        if (presenter==null){
            presenter = new MainActivityPresenter(getBaseContext());
        }
        presenter.setActivity(this);
        presenter.getFriendStroy();
        initFragments();

        // redirect to chat screen
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentMain).commit();
//        getFragmentManager().beginTransaction().add(R.id.main_frame, fragmentChat).commit();
//        getFragmentManager().beginTransaction().show(fragmentChat).commit();


//        getFragmentManager().beginTransaction().add(R.id.main_frame, fragmentShowImageTimer).commit();
//        getFragmentManager().beginTransaction().show(fragmentShowImageTimer).commit();

//        getFragmentManager().beginTransaction().add(R.id.main_frame, fragmentImageEditor).commit();
//        getFragmentManager().beginTransaction().show(fragmentImageEditor).commit();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(FirebaseMessagingService.REGISTRATION_SUCCESS)){

                    Bundle extras = intent.getExtras();
                    if(extras != null){
                        String message = (String)extras.get("message");
                        String message_type = (String)extras.get("message_type");
                        if (message_type.equals("1")) {
                            fragmentChat.addMessageListItems(message,false, ChatMessageModel.MSG_TYPE_OTHER_TEXT);
                        }
                        else {
                            fragmentChat.addMessageListItems(message,false, ChatMessageModel.MSG_TYPE_OTHER_IMG_VIEW);
                        }
                    }
                }
                else {

                }
            }
        };

        // this is for chat screen
        isAppCreated = true;
    }

    public void initFragments(){
        fragmentMain = new FragmentMain();
        fragmentAddfriends = new FragmentAddfriends();
        fragmentAddaddressbook = new FragmentAddaddressbook();
        fragmentAddusername = new FragmentAddusername();

        fragmentResultFriend = new FragmentResultFriend();
        fragmentAddedme = new FragmentAddedme();
        fragmentResultAddedme = new FragmentResultAddedme();
        fragmentUserscreen = new FragmentUserscreen();
        fragmentCamera = new FragmentCamera();
        fragmentImageEditor = new FragmentImageEditor();
        fragmentChat = new FragmentChat();
        fragmentCreatestory  = new FragmentCreatestory();
        fragmentFriendSelection = new FragmentFriendSelection();
        fragmentShowImageTimer = new FragmentShowImageTimer();
    }

    public FragmentMain getFragmentMain(){
        return fragmentMain;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case FragmentCamera.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,
                            FragmentCamera.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } else {
                    Toast.makeText(getBaseContext(), "retry", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FragmentCamera.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                fragmentCamera.getResult(data);
            }else if (resultCode == Activity.RESULT_CANCELED){
                fromCameraToMain();
            }
        }else if (requestCode == FragmentMemories.PICK_PHOTO){
                fragmentMain.getFragmentMemories().getPhoto(data);
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    FragmentCamera.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    FragmentCamera.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public void fromCameraToEditor(String path){

        fragmentImageEditor.imageCanvasBackgroundPath = path;
        getSupportFragmentManager().beginTransaction().hide(fragmentMain).commitAllowingStateLoss();
        if (!fragmentImageEditor.isAdded()){
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentImageEditor).commitAllowingStateLoss();
        }else {
            getSupportFragmentManager().beginTransaction().show(fragmentImageEditor).commitAllowingStateLoss();
        }
    }


    public void fromMainToCamera(){
        getSupportFragmentManager().beginTransaction().hide(fragmentMain).show(fragmentCamera).commit();
        checkPermission();
    }

    public void fromCameraToMain(){
        getSupportFragmentManager().beginTransaction().hide(fragmentCamera).show(fragmentMain).commitAllowingStateLoss();
    }
    public void contactToUserscreen(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).hide(fragmentMain).commit();
        if (fragmentUserscreen.isAdded()){
            if (fragmentUserscreen.isHidden()) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).show(fragmentUserscreen).commit();
            }
        }else{
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up).add(R.id.main_frame, fragmentUserscreen).commit();
        }
    }

    public void userscreenToContact(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).hide(fragmentUserscreen).commit();
        if (fragmentMain.isAdded()){
            if (fragmentMain.isHidden()){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).show(fragmentMain).commit();
            }
        }else{
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).add(R.id.main_frame, fragmentMain).commit();
        }
    }

    public void addFriendsToAddAddress(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddfriends).commit();
        if (fragmentAddaddressbook.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddaddressbook).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentAddaddressbook).commit();
        }
    }

    public void addFriendsToAddUsername(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddfriends).commit();
        if (fragmentAddusername.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddusername).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentAddusername).commit();
        }
    }

    public void fromAddfriendsToAddusername(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddfriends).commit();
        if(fragmentAddusername.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddusername).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentAddusername).commit();
        }
    }

    public void fromAddfriendsToAddaddressbook(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddfriends).commit();
        if(fragmentAddaddressbook.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddaddressbook).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentAddaddressbook).commit();
        }
    }

    public void fromAddusernameToResultfriend(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddusername).commit();
        if(fragmentResultFriend.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentResultFriend).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentResultFriend).commit();
        }
    }

    public void fromAddaddressbookToResultfriend(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddaddressbook).commit();
        if(fragmentResultFriend.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentResultFriend).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentResultFriend).commit();
        }
    }

    public void fromImageEditorToSelectFriends(){
        getSupportFragmentManager().beginTransaction().hide(fragmentImageEditor).commit();
        if (!fragmentFriendSelection.isAdded()){
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentFriendSelection).commit();
        }else{
            getSupportFragmentManager().beginTransaction().show(fragmentFriendSelection).commit();
        }
    }

    public void fromFriendSelectionToContact(){
        fragmentMain.setPage(2); // the third page is contact page
        getSupportFragmentManager().beginTransaction().hide(fragmentFriendSelection).show(fragmentMain).commit();
    }

    public void fromUserscreenToAddedme(){
        getSupportFragmentManager().beginTransaction().hide(fragmentUserscreen).commit();
        if(fragmentAddedme.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddedme).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentAddedme).commit();
        }
    }

    public void fromAddedmeToUserscreen(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddedme).commit();
        if(fragmentUserscreen.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentUserscreen).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentUserscreen).commit();
        }
    }

    public void fromAddedmeToResultAddedme(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddedme).commit();
        if(fragmentResultAddedme.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentResultAddedme).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentResultAddedme).commit();
        }
    }

    public void fromUserscreenToAddfriends(){
        getSupportFragmentManager().beginTransaction().hide(fragmentUserscreen).commit();
        if(fragmentAddfriends.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddfriends).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentAddfriends).commit();
        }
    }

    public void fromAddfriendsToUserscreen(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddfriends).commit();
        if(fragmentUserscreen.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentUserscreen).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentUserscreen).commit();
        }
    }

    public void fromAddaddressbookToAddfriends(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddaddressbook).commit();
        if(fragmentAddfriends.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddfriends).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentAddfriends).commit();
        }
    }

    public void fromAddusernameToAddfriends(){
        getSupportFragmentManager().beginTransaction().hide(fragmentAddusername).commit();
        if(fragmentAddfriends.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentAddfriends).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentAddfriends).commit();
        }
    }
    public void fromResultAddedmeToUserscreen(){
        getSupportFragmentManager().beginTransaction().hide(fragmentResultAddedme).commit();
        if(fragmentUserscreen.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentUserscreen).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentUserscreen).commit();
        }
    }

    public void fromResultFriendToUserscreen(){
        getSupportFragmentManager().beginTransaction().hide(fragmentResultFriend).commit();
        if(fragmentUserscreen.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentUserscreen).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentUserscreen).commit();
        }
    }

    public void fromUserscreenToMyfriends(){
        getSupportFragmentManager().beginTransaction().hide(fragmentUserscreen).commit();
        if(fragmentUserscreen.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentMain).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentMain).commit();
        }
    }

    public void fromMemoryToCreateStory(){
        getSupportFragmentManager().beginTransaction().hide(fragmentMain).commit();
        if(fragmentCreatestory.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentCreatestory).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentCreatestory).commit();
        }
    }

    public void fromCreateStoryToMemory(){
        getSupportFragmentManager().beginTransaction().hide(fragmentCreatestory).commit();
        if(fragmentMain.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentMain).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentMain).commit();
        }
    }

    public void fromFriendSelectionToMemory() {
        getSupportFragmentManager().beginTransaction().hide(fragmentFriendSelection).show(fragmentMain).commit();
    }

    public void fromChatScreenToShowImage(){

    }

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(FirebaseMessagingService.REGISTRATION_SUCCESS));
    }
    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

}
