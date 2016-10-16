package com.example.paul.snapchatdemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.api.ChatApi;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.chat.ChatMessageAdapter;
import com.example.paul.snapchatdemo.chat.ChatMessageModel;
import com.example.paul.snapchatdemo.chat.ImageGaleryAdapter;
import com.example.paul.snapchatdemo.chat.Token;
import com.example.paul.snapchatdemo.firebase.FirebaseStorageService;
import com.example.paul.snapchatdemo.utils.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentChat extends Fragment {
    /**
     * Initialize fragment
     */
    private View root;
    LayoutInflater inflater;
    Bundle savedInstanceState;

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


    static List<ChatMessageModel>  chatMessageList;
    static ChatMessageAdapter chatMessageAdapter;

    Button addImageButton;
    Button regTokenButton;
    Button sendImageButton;

    GridView grdImages;
    ImageGaleryAdapter adapterImage;
    EditText messageText;
    LinearLayout imageLayout;
    LinearLayout inputLayout;

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageLayout= (LinearLayout) root.findViewById(R.id.imageGalleryLayout);

        inputLayout= (LinearLayout) root.findViewById(R.id.inputLayout);

        // set inputLayout height
//        ViewGroup.LayoutParams layoutParams = inputLayout.getLayoutParams();
//        layoutParams.height = getScreenHeight()/2;
//        inputLayout.setLayoutParams(layoutParams);


        grdImages= (GridView) root.findViewById(R.id.grdImages);


        // setup button
        regTokenButton = (Button) root.findViewById(R.id.regTokenButton);
        regTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        registerToken();
                    }
                }).start();
            }
        });

        addImageButton = (Button) root.findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddImageFragment();
            }
        });

        sendImageButton = (Button) root.findViewById(R.id.sendImageButton);
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send image to google server
                List<String> imageDownloadURL = sendImage();

                // send the image url to the receiver

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




                    // this is for getting data back, asynchronous doing this task


                    // add message to the message list
                    if (messageText.getText() != null) {
                        String inputMessage = messageText.getText().toString();
                        if (!inputMessage.isEmpty()) {

                            // TODO: still using static data
                            String senderUserId = "2";
                            String receiverUserId = "4";
                            String messageType = "1";

                            // send message to server
                            ChatApi chatApi = HttpUtil.accessServer(ChatApi.class);
                            chatApi.sendMessage(senderUserId, receiverUserId, inputMessage, messageType).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    // send message is successful
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    // send message is failed, put message on the queue ?

                                    // show command to user to resend message

                                }
                            });

                            // add message to message list
                            messageText.setText(null);
                            addMessageListItems(inputMessage, false);
                        }
                    }

                    handled = true;
                }
                return handled;
            }
        });
    }

    public void addMessageListItems(String input, boolean isImageURL){
        ChatMessageModel chatMessage;
        if (isImageURL) {
            chatMessage = new ChatMessageModel("",input);
        }
        else {
            chatMessage = new ChatMessageModel(input,"none");
        }

        chatMessageList.add(chatMessage);
        chatMessageAdapter.notifyDataSetChanged();
    }

    public void registerToken() {
        String token = Token.generateToken();
        Token.registerToken(token, "4");
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

    public List<String> sendImage() {
        String pathPrefix = "file://";
        Map<String,String> selectedImagePath = adapterImage.getSelectedImagePath();
        Set<String> keys = selectedImagePath.keySet();
        final List<String> imageURLDownload = new ArrayList();

        for (String imageURLPath:keys) {
            // upload image to server
            String localImageFileName = pathPrefix+imageURLPath;
            String urlDownload = FirebaseStorageService.uploadImage(localImageFileName);
            imageURLDownload.add(urlDownload);
            System.out.println("urlDownload:"+urlDownload);

            // upload image to sender chat screen
            addMessageListItems(localImageFileName,true);
        }

        imageLayout.setVisibility(View.GONE);

        return imageURLDownload;
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) root.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

}