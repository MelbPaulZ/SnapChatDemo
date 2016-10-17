package com.example.paul.snapchatdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.ChatApi;
import com.example.paul.snapchatdemo.api.PushMessageApi;
import com.example.paul.snapchatdemo.bean.PushMessage;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.chat.ChatMessageAdapter;
import com.example.paul.snapchatdemo.chat.ChatMessageModel;
import com.example.paul.snapchatdemo.chat.ImageGaleryAdapter;
import com.example.paul.snapchatdemo.utils.HttpUtil;
import com.example.paul.snapchatdemo.utils.UserUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentChat extends android.support.v4.app.Fragment {
    /**
     * Initialize fragment
     */
    private View root;
    LayoutInflater inflater;
    Bundle savedInstanceState;

    public static List<ChatMessageModel>  chatMessageList;
    public static ChatMessageAdapter chatMessageAdapter;

    ImageButton addImageButton;
    ImageButton sendImageButton;

    GridView grdImages;
    ImageGaleryAdapter adapterImage;
    EditText messageText;
    LinearLayout imageLayout;
    LinearLayout inputLayout;
    ProgressBar loadingPanel;

    private StorageReference mStorageRef;

    public String receiverUserId;

    private ImageButton backToContactButton;

    private TextView friendNameTextView;

    private String name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        root= inflater.inflate(R.layout.fragment_chat, container, false);
        this.savedInstanceState = savedInstanceState;
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }



    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        friendNameTextView = (TextView)root.findViewById(R.id.friendNameTextView);
        if (name!=null) {
            friendNameTextView.setText(name);
        }

        backToContactButton = (ImageButton)root.findViewById(R.id.backToContactButton);
        backToContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToContactList();
            }
        });

        loadingPanel = (ProgressBar) root.findViewById(R.id.loadingPanel);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        imageLayout= (LinearLayout) root.findViewById(R.id.imageGalleryLayout);

        inputLayout= (LinearLayout) root.findViewById(R.id.inputLayout);

        // set inputLayout height
//        ViewGroup.LayoutParams layoutParams = inputLayout.getLayoutParams();
//        layoutParams.height = getScreenHeight()/2;
//        inputLayout.setLayoutParams(layoutParams);


        grdImages= (GridView) root.findViewById(R.id.grdImages);

        addImageButton = (ImageButton) root.findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddImageFragment();
            }
        });

        sendImageButton = (ImageButton) root.findViewById(R.id.sendImageButton);
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage();
            }
        });

        chatMessageList = new ArrayList<ChatMessageModel>();
        chatMessageAdapter = new ChatMessageAdapter(root.getContext(), chatMessageList);
        ListView messageList = (ListView) root.findViewById(R.id.messageList);
        messageList.setAdapter(chatMessageAdapter);

        // setup message input
        messageText = (EditText) root.findViewById(R.id.messageInputText);
        messageText.setHorizontallyScrolling(false);
        messageText.setMaxLines(Integer.MAX_VALUE);

        messageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.setVisibility(View.GONE);
            }
        });

        messageText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imageLayout.setVisibility(View.GONE);
                }
            }
        });

        messageText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // add message to the message list
                    if (messageText.getText() != null) {
                        final String inputMessage = messageText.getText().toString();
                        if (!inputMessage.isEmpty()) {
                            String messageType = ChatMessageModel.MSG_DATA_TEXT;
                            final int messageTimer = 0; // this is text, so just use 0 for timer

                            // send message to server
                            ChatApi chatApi = HttpUtil.accessServer(ChatApi.class);
                            chatApi.sendMessage(UserUtil.getId(), receiverUserId, inputMessage, messageType,"0").enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    // send message is successful
                                    // add message to message list
                                    messageText.setText(null);
                                    addMessageListItems(inputMessage, false, ChatMessageModel.MSG_TYPE_MINE_TEXT_SENT, messageTimer);
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    // send message is failed
                                    messageText.setText(null);
                                    addMessageListItems(inputMessage, false, ChatMessageModel.MSG_TYPE_MINE_TEXT_PENDING, messageTimer);
                                }
                            });
                        }
                    }

                    handled = true;
                }
                return handled;
            }
        });
    }

    private void backToContactList() {

        // remove message from queue
        PushMessageApi pushMessageApi = HttpUtil.accessServer(PushMessageApi.class);
        pushMessageApi.deleteMessage(UserUtil.getId(), receiverUserId).enqueue(new Callback<PushMessage>() {
            @Override
            public void onResponse(Call<PushMessage> call, Response<PushMessage> response) {
                // do nothing
            }

            @Override
            public void onFailure(Call<PushMessage> call, Throwable t) {
                // do nothing
            }
        });

        // reset receiver id
        receiverUserId = "-1";

        // reset chat message list
        chatMessageList.clear();

        // redirect to contact screen
        ((MainActivity)(Activity) getContext()).chatScreenToContact();
    }

    public void addMessageListItems(String input, boolean isImageURL, int messageType, int messageTimer){
        ChatMessageModel chatMessage;
        int messageIdx = 0;

        if (chatMessageList.size()!=0) {
            messageIdx = chatMessageList.size();
        }

        if (isImageURL) {
            chatMessage = new ChatMessageModel("", input, messageType, messageTimer, messageIdx);
        }
        else {
            chatMessage = new ChatMessageModel(input, "none", messageType, messageTimer, messageIdx);
        }

        chatMessageList.add(chatMessage);
        chatMessageAdapter.notifyDataSetChanged();
    }

    public void showAddImageFragment() {

        messageText.clearFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageText.getWindowToken(), 0);

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;

        Cursor imagecursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);

        int count = imagecursor.getCount();
        String[] arrPath = new String[count];
        int ids[] = new int[count];
        boolean[] imageSelection = new boolean[count];

        for (int i = 0; i < count; i++) {
            imagecursor.moveToPosition(i);
            ids[i] = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            arrPath[i] = imagecursor.getString(dataColumnIndex);
        }

        adapterImage = new ImageGaleryAdapter(inflater, count, imageSelection, ids, getActivity(), arrPath);
        grdImages.setAdapter(adapterImage);
        imagecursor.close();

        imageLayout.setVisibility(View.VISIBLE);
    }

    public void sendImage() {

        String pathPrefix = "file://";
        Map<String,String> selectedImagePath = adapterImage.getSelectedImagePath();
        Set<String> keys = selectedImagePath.keySet();
        final List<String> imageURLDownload = new ArrayList();

        for (String imageURLPath:keys) {
            // upload image to server
            final String localImageFileName = pathPrefix+imageURLPath;
            String fileName = UUID.randomUUID().toString();

            final StorageReference serverPhotoRef = mStorageRef.child("photos").child(fileName);
            Uri fileUri = Uri.parse(localImageFileName);
            serverPhotoRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadURL = taskSnapshot.getMetadata().getDownloadUrl();
                            // send the image url to the receiver
                            String messageType = ChatMessageModel.MSG_DATA_IMG;
                            // TODO: image from gallery is not using timer, so set it to zero
                            // if camera button already added to chat screen, then we need to adjust this
                            String messageTimer = "0";

                            // send message to server
                            ChatApi chatApi = HttpUtil.accessServer(ChatApi.class);
                            chatApi.sendMessage(UserUtil.getId(), receiverUserId, downloadURL.toString(), messageType, messageTimer).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    // send message is successful
                                    // upload image to sender chat screen
                                    loadingPanel.setVisibility(View.GONE);
                                    addMessageListItems(localImageFileName, true, ChatMessageModel.MSG_TYPE_MINE_IMG_SENT, 0);
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    // send message to receiver is failed, ask user to retry
                                    loadingPanel.setVisibility(View.GONE);
                                    addMessageListItems(localImageFileName, true, ChatMessageModel.MSG_TYPE_MINE_IMG_PENDING, 0);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // upload image failed, ask user to retry
                            loadingPanel.setVisibility(View.GONE);
                            addMessageListItems(localImageFileName, true, ChatMessageModel.MSG_TYPE_MINE_IMG_PENDING, 0);
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

        imageLayout.setVisibility(View.GONE);
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) root.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    public void updateDisplayedImageStatus(int messageIdx) {
        // get the chatMessageModel
        ChatMessageModel chm = chatMessageList.get(messageIdx);

        // reduce the quota
        chm.reduceViewQuota();

        // update the message list
        chatMessageList.remove(messageIdx);
        chatMessageList.add(messageIdx, chm);
        chatMessageAdapter.notifyDataSetChanged();
    }

    private void putMessageToChatScreen(String chatMessage, String chatMessageType, int messageTimer) {
        // user is chatting with sender, show the message directly on chat screen
        if (chatMessageType.equals(ChatMessageModel.MSG_DATA_TEXT)) {
            // receive regular text
            addMessageListItems(chatMessage,false, ChatMessageModel.MSG_TYPE_OTHER_TEXT, messageTimer);
        }
        else if (messageTimer!=0){
            // receive a snap (image with timer)
            addMessageListItems(chatMessage,true, ChatMessageModel.MSG_TYPE_OTHER_IMG_TIMER_VIEW, messageTimer);
        }
        else {
            // receive static image without timer
            addMessageListItems(chatMessage,true, ChatMessageModel.MSG_TYPE_OTHER_IMG, messageTimer);
        }
    }

    public void pullMessageFromQueue() {
        final PushMessageApi pushMessageApi = HttpUtil.accessServerWithGson(PushMessageApi.class);
        pushMessageApi.getMessageBySenderId(UserUtil.getId(), receiverUserId).enqueue(new Callback<ArrayList<PushMessage>>() {
            @Override
            public void onResponse(Call<ArrayList<PushMessage>> call, Response<ArrayList<PushMessage>> response) {
                List<PushMessage> messageList = response.body();
                for (PushMessage msg : messageList) {
                    int timer = Integer.parseInt(msg.getChatMessageTimer());
                    putMessageToChatScreen(msg.getChatMessage(), msg.getChatMessageType(), timer);
                }

                // remove all message from queue

            }

            @Override
            public void onFailure(Call<ArrayList<PushMessage>> call, Throwable t) {
                // do nothing
                System.out.println("ERROR: Cannot get message queue");
                t.printStackTrace();

            }
        });
    }

    public void setFriend(String userId, String name){
        receiverUserId = userId;
        this.name = name;
        if (friendNameTextView!=null) {
            friendNameTextView.setText(name);
        }
    }

    public boolean isChattingWithUser(String userId) {
        if (receiverUserId!=null) {
            return receiverUserId.equals(userId);
        }
        else {
            return false;
        }
    }

    public void retrySendingMessage(final ChatMessageModel chm) {
        ChatApi chatApi = HttpUtil.accessServer(ChatApi.class);
        chatApi.sendMessage(UserUtil.getId(), receiverUserId, chm.getImageUrl(), ""+chm.getMessageType(), ""+chm.getMessageTimer()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // update this message into successful
                int oldMessageType = chm.getMessageType();
                if (oldMessageType==ChatMessageModel.MSG_TYPE_MINE_TEXT_PENDING) {
                    chm.setMessageType(ChatMessageModel.MSG_TYPE_MINE_TEXT_SENT);
                }
                else if (oldMessageType==ChatMessageModel.MSG_TYPE_MINE_IMG_PENDING) {
                    chm.setMessageType(ChatMessageModel.MSG_TYPE_MINE_IMG_SENT);
                }

                int messageIdx = chm.getMessageIdx();
                chatMessageList.remove(messageIdx);
                chatMessageList.add(messageIdx, chm);
                chatMessageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // show toast
                Toast.makeText(getContext(), "Retry sending message failed, check your network connection.", Toast.LENGTH_LONG).show();
            }
        });

    }
}
