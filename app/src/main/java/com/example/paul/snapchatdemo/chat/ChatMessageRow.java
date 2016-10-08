package com.example.paul.snapchatdemo.chat;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.squareup.picasso.Picasso;

/**
 * Created by verramukty on 9/26/2016.
 */
public class ChatMessageRow {
    public static final int LAYOUT = R.layout.item_chat_message;

    protected final Context context;
    protected final ImageView imageView;
    protected final TextView textView;

    public ChatMessageRow(Context context, View convertView) {
        this.context = context;
        this.imageView = (ImageView) convertView.findViewById(R.id.chatImageView);
        this.textView = (TextView) convertView.findViewById(R.id.chatTextView);
    }

    public void bind(ChatMessageModel modelChatMessage) {
        String imageUrl = modelChatMessage.getImageUrl();
        imageView.setVisibility(View.VISIBLE);
        Picasso.with(context).load(imageUrl).into(imageView);

        String text = modelChatMessage.getText();
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);

    }
}
