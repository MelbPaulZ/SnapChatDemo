package com.example.paul.snapchatdemo.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by verramukty on 9/26/2016.
 */
public class ChatMessageRow {
    public static final int LAYOUT = R.layout.item_chat_message;

    protected final Context context;
    protected final ImageView imageView;
    protected final TextView textView;
    RelativeLayout chatRootContent;
    protected final TextView chatDescText;
    protected final ImageView chatDescRetryImage;
    protected final ImageView chatDescSentImage;
    protected final TextView bubbleSpacer;

    private int imageWidth;
    private int imageHeight;
    private int bubbleWidth;
    private int bubbleSpacerWidth;

    public ChatMessageRow(Context context, View convertView, int imageWidth, int imageHeight, int bubbleWidth, int bubbleSpacerWidth) {
        this.context = context;
        this.imageView = (ImageView) convertView.findViewById(R.id.chatImageView);
        this.textView = (TextView) convertView.findViewById(R.id.chatTextView);
        this.chatRootContent = (RelativeLayout) convertView.findViewById(R.id.chatRootContent);
        this.chatDescText = (TextView) convertView.findViewById(R.id.chatDescText);
        this.chatDescRetryImage = (ImageView) convertView.findViewById(R.id.chatDescRetryImage);
        this.chatDescSentImage = (ImageView) convertView.findViewById(R.id.chatDescSentImage);
        this.bubbleSpacer = (TextView) convertView.findViewById(R.id.bubbleSpacer);

        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.bubbleWidth = bubbleWidth;
        this.bubbleSpacerWidth = bubbleSpacerWidth;
    }

    public void bind(final ChatMessageModel chatMessageModel) {
        chatRootContent.getLayoutParams().width = bubbleWidth;
        bubbleSpacer.getLayoutParams().width = bubbleSpacerWidth;

        int messageType = chatMessageModel.getMessageType();

        if (messageType==ChatMessageModel.MSG_TYPE_MINE_TEXT_SENT
                || messageType==ChatMessageModel.MSG_TYPE_MINE_TEXT_PENDING
                || messageType==ChatMessageModel.MSG_TYPE_MINE_IMG_SENT
                || messageType==ChatMessageModel.MSG_TYPE_MINE_IMG_PENDING)
        {
            bubbleSpacer.setVisibility(View.VISIBLE);

            // set text & image
            String imageUrl = chatMessageModel.getImageUrl();
            Picasso.with(context).load(imageUrl).resize(imageWidth, imageHeight).centerInside().into(imageView);
            String text = chatMessageModel.getText();
            textView.setText(text);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTypeface(null, Typeface.NORMAL);

            if (messageType==ChatMessageModel.MSG_TYPE_MINE_TEXT_SENT
                    || messageType==ChatMessageModel.MSG_TYPE_MINE_TEXT_PENDING) {
                textView.bringToFront();
            }
            else {
                imageView.bringToFront();
            }
        }
        else {
            bubbleSpacer.setVisibility(View.GONE);

            String text="";
            if (messageType==ChatMessageModel.MSG_TYPE_OTHER_TEXT) {
                Picasso.with(context).load("none").into(imageView);

                text = chatMessageModel.getText();
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setTypeface(null, Typeface.NORMAL);
            }
            else if (messageType==ChatMessageModel.MSG_TYPE_OTHER_IMG) {
                String imageUrl = chatMessageModel.getImageUrl();
                Picasso.with(context).load(imageUrl).resize(imageWidth, imageHeight).centerInside().into(imageView);
            }
            else {
                if (chatMessageModel.getViewQuota()==2) {
                    Picasso.with(context).load("none").into(imageView);

                    text = "Tap to view image";
                    textView.setTextColor(Color.parseColor("#1E90FF"));
                    textView.setTypeface(null, Typeface.BOLD);
                }
                else if (chatMessageModel.getViewQuota()==1){
                    Picasso.with(context).load("none").into(imageView);

                    text = "Tap to replay image";
                    textView.setTextColor(Color.parseColor("#51188e"));
                    textView.setTypeface(null, Typeface.BOLD);
                }
                else {
                    Picasso.with(context).load("none").into(imageView);

                    text = "Image has been replayed";
                    textView.setTextColor(Color.parseColor("#fc0000"));
                    textView.setTypeface(null, Typeface.BOLD);
                }
            }
            textView.setText(text);
            textView.bringToFront();

            // add click listener on the chat bubble for image type
            if (messageType==ChatMessageModel.MSG_TYPE_OTHER_IMG_TIMER_VIEW
                    || messageType==ChatMessageModel.MSG_TYPE_OTHER_IMG_TIMER_REPLAY) {
                chatRootContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showImage(chatMessageModel);
                    }
                });
            }
        }

        // set footer message
        if (messageType==ChatMessageModel.MSG_TYPE_MINE_TEXT_SENT
                || messageType==ChatMessageModel.MSG_TYPE_MINE_IMG_SENT)
        {
            // my message is sent
            chatDescText.setText(" ");
            chatDescSentImage.setVisibility(View.VISIBLE);
            chatDescRetryImage.setVisibility(View.GONE);
        }
        else if (messageType==ChatMessageModel.MSG_TYPE_MINE_TEXT_PENDING
                || messageType==ChatMessageModel.MSG_TYPE_MINE_IMG_PENDING){
            // my message is pending
            chatDescText.setText("tap to retry ");
            chatDescSentImage.setVisibility(View.GONE);
            chatDescRetryImage.setVisibility(View.VISIBLE);
        }
        else {
            chatDescText.setText(" ");
            chatDescSentImage.setVisibility(View.GONE);
            chatDescRetryImage.setVisibility(View.GONE);
        }
    }

    private void showImage(ChatMessageModel chm) {
        // only show image if its still have view quota
        if (chm.getViewQuota()!=0) {
            // redirect to FragmentShowImageTimer
            ((MainActivity)(Activity) context).fromChatScreenToShowImage(chm.getMessageTimer(), chm.getImageUrl(), chm.getMessageIdx());
        }
    }
}
