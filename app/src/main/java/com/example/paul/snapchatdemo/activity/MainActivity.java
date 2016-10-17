package com.example.paul.snapchatdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.bean.Friend;
import com.example.paul.snapchatdemo.bean.FriendPhone;
import com.example.paul.snapchatdemo.bean.PhotoStory;
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
import com.example.paul.snapchatdemo.fragment.FragmentLockphoto;
import com.example.paul.snapchatdemo.fragment.FragmentMain;
import com.example.paul.snapchatdemo.fragment.FragmentMemories;
import com.example.paul.snapchatdemo.fragment.FragmentResultAddedme;
import com.example.paul.snapchatdemo.fragment.FragmentResultFriend;
import com.example.paul.snapchatdemo.fragment.FragmentSecretalbum;
import com.example.paul.snapchatdemo.fragment.FragmentShowImageTimer;
import com.example.paul.snapchatdemo.fragment.FragmentUserscreen;
import com.example.paul.snapchatdemo.presenter.MainActivityPresenter;
import com.example.paul.snapchatdemo.utils.UserUtil;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FragmentMain fragmentMain;
    private FragmentAddfriends fragmentAddfriends;
    private FragmentAddaddressbook fragmentAddaddressbook;
    private FragmentAddusername fragmentAddusername;
    private FragmentLockphoto fragmentLockPhoto;
    private FragmentSecretalbum fragmentSecretalbum;
    private FragmentChat fragmentChat;
    private FragmentShowImageTimer fragmentShowImageTimer;

    private MainActivityPresenter presenter;

    public static boolean isAppCreated = false;
    private BroadcastReceiver chatMessageBroadcastReceiver;
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

    public ArrayList<PhotoStory> getPhotoStory() {
        return photoStory;
    }

    public void setPhotoStory(ArrayList<PhotoStory> photoStory) {
        this.photoStory = (ArrayList<PhotoStory>)photoStory.clone();
    }

    private ArrayList<PhotoStory> photoStory;
    private String imageUrl;

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    private String absolutePath;

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

        // original
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentMain).commit();

        // verra: to test chat screen
//        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentShowImageTimer).commit();
//        getSupportFragmentManager().beginTransaction().hide(fragmentShowImageTimer).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentChat).commit();

        // verra: to test image editor
//        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentImageEditor).commit();
//        getSupportFragmentManager().beginTransaction().show(fragmentImageEditor).commit();


        chatMessageBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                receiveChatMessage(intent);
            }
        };

        // set timeout for firebase upload to 10 seconds
        FirebaseStorage.getInstance().setMaxUploadRetryTimeMillis(10000);

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
        fragmentLockPhoto = new FragmentLockphoto();
        fragmentSecretalbum = new FragmentSecretalbum();
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

    public void fromImageEditorToCreateStory(){
        getSupportFragmentManager().beginTransaction().hide(fragmentImageEditor).commit();
        if (fragmentCreatestory.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentCreatestory).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentCreatestory).commit();
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



    public void fromEditorToCamera(){
        getSupportFragmentManager().beginTransaction().hide(fragmentImageEditor).show(fragmentCamera).commit();
        checkPermission();
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
        }else {
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentAddaddressbook).commit();
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
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentResultFriend).commit();
        }
    }

    public void fromImageEditorToSelectFriends(String imageFilePath, String timer){
        // file path to be sent to friends
        fragmentFriendSelection.imageFilePath = imageFilePath;
        fragmentFriendSelection.imageTimer = timer;

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
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentAddedme).commit();
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

    public void fromMemoryToLockphoto(){
        getSupportFragmentManager().beginTransaction().hide(fragmentMain).commit();
        if(fragmentLockPhoto.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentLockPhoto).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentLockPhoto).commit();
        }
    }

    public void fromLockphotoToMemory(){
        getSupportFragmentManager().beginTransaction().hide(fragmentLockPhoto).commit();
        if(fragmentMain.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentMain).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentMain).commit();
        }
    }

    public void fromSecretalbumToLockphoto(){
        getSupportFragmentManager().beginTransaction().hide(fragmentSecretalbum).commit();
        if(fragmentLockPhoto.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentLockPhoto).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentLockPhoto).commit();
        }
    }

    public void fromLockPhotoToSecretAlbum(){
        getSupportFragmentManager().beginTransaction().hide(fragmentLockPhoto).commit();
        if(fragmentSecretalbum.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragmentSecretalbum).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame,fragmentSecretalbum).commit();
        }
    }

    public void fromFriendSelectionToMemory() {
        getSupportFragmentManager().beginTransaction().hide(fragmentFriendSelection).show(fragmentMain).commit();
    }

    public void fromChatScreenToShowImage(int messageTimer, String imageUrl, int messageIdx){
        fragmentShowImageTimer.messageTimer = messageTimer;
        fragmentShowImageTimer.imageUrl = imageUrl;
        fragmentShowImageTimer.messageIdx = messageIdx;

        getSupportFragmentManager().beginTransaction().hide(fragmentChat).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentShowImageTimer).commit();
    }

    public void fromShowImageToChatScreen(){
        // update status of image that has been displayed
        fragmentChat.updateDisplayedImageStatus(fragmentShowImageTimer.messageIdx);

        getSupportFragmentManager().beginTransaction().hide(fragmentShowImageTimer).commit();
        getSupportFragmentManager().beginTransaction().show(fragmentChat).commit();
    }


    public void contactToChatScreen(Friend friend){
        getSupportFragmentManager().beginTransaction().hide(fragmentMain).commit();
        if (!fragmentChat.isAdded()){
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentChat).commit();
        }else {
            getSupportFragmentManager().beginTransaction().show(fragmentChat).commit();
        }
        fragmentChat.setFriend(friend.getId(), friend.getName());
        fragmentChat.pullMessageFromQueue();

    }

    public void chatScreenToContact(){
        getSupportFragmentManager().beginTransaction().hide(fragmentChat).commit();

        if (fragmentMain.isAdded()){
            if (fragmentMain.isHidden()){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).show(fragmentMain).commit();
            }
        }else{
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).add(R.id.main_frame, fragmentMain).commit();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(chatMessageBroadcastReceiver,
                new IntentFilter(FirebaseMessagingService.RECEIVE_FIREBASE_CHAT_MESSAGE));
    }
    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatMessageBroadcastReceiver);
    }

    private void receiveChatMessage(Intent intent) {
        if(intent.getAction().equals(FirebaseMessagingService.RECEIVE_FIREBASE_CHAT_MESSAGE)){
            Bundle extras = intent.getExtras();
            if(extras != null){
                String senderUserId = (String)extras.get("sender_user_id");
                String chatMessage = (String)extras.get("chat_message");
                String chatMessageType = (String)extras.get("chat_message_type");
                String chatMessageTimer = (String)extras.get("chat_message_timer");
                int messageTimer = Integer.parseInt(chatMessageTimer);

                if (fragmentChat.isHidden()) {
                    // user is not opening chat screen, keep the message on the queue on server.
                    FirebaseMessagingService.pushMessageToQueue(UserUtil.getId(), senderUserId, chatMessage, chatMessageType, chatMessageTimer);
                }
                else {
                    // check whether user is currently chatting with the sender.
                    if (fragmentChat.isChattingWithUser(senderUserId)) {
                        putMessageToChatScreen(chatMessage, chatMessageType, messageTimer);

                    }
                    else {
                        // user opening the chat screen, but not chatting with the sender.
                        // keep the message on the queue on server
                        FirebaseMessagingService.pushMessageToQueue(UserUtil.getId(), senderUserId, chatMessage, chatMessageType, chatMessageTimer);
                    }
                }
            }
        }
    }

    private void putMessageToChatScreen(String chatMessage, String chatMessageType, int messageTimer) {
        // user is chatting with sender, show the message directly on chat screen
        if (chatMessageType.equals(ChatMessageModel.MSG_DATA_TEXT)) {
            // receive regular text
            fragmentChat.addMessageListItems(chatMessage,false, ChatMessageModel.MSG_TYPE_OTHER_TEXT, messageTimer);
        }
        else if (messageTimer!=0){
            // receive a snap (image with timer)
            fragmentChat.addMessageListItems(chatMessage,true, ChatMessageModel.MSG_TYPE_OTHER_IMG_TIMER_VIEW, messageTimer);
        }
        else {
            // receive static image without timer
            fragmentChat.addMessageListItems(chatMessage,true, ChatMessageModel.MSG_TYPE_OTHER_IMG, messageTimer);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String senderUserId = (String) extras.get("sender_user_id");
            String sender = (String) extras.get("sender");
            // redirect to chat screen
            initToChatScreen(senderUserId, sender);
        }
    }

    private void initToChatScreen(String senderUserId, String sender) {
        getSupportFragmentManager().beginTransaction().hide(fragmentMain).commit();
        if (!fragmentChat.isAdded()){
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentChat).commit();
        }
        getSupportFragmentManager().beginTransaction().show(fragmentChat).commit();

        fragmentChat.setFriend(senderUserId, sender);
        fragmentChat.pullMessageFromQueue();
    }

    public FragmentChat getFragmentChat() {
        return fragmentChat;
    }

    @Override
    public void onBackPressed()
    {
        // do nothing, all navigation are handled by app, this app doesnt support native back button navigation.

    }
}
