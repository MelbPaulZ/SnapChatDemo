package com.example.paul.snapchatdemo.chat;

/**
 * Created by verramukty on 9/26/2016.
 */
public class ChatMessageModel {

    public static int MSG_TYPE_MINE_TEXT_SENT = 1;
    public static int MSG_TYPE_MINE_TEXT_PENDING = 2;
    public static int MSG_TYPE_MINE_IMG_SENT = 3;
    public static int MSG_TYPE_MINE_IMG_PENDING = 4;
    public static int MSG_TYPE_OTHER_TEXT = 5;
    public static int MSG_TYPE_OTHER_IMG_VIEW = 6;
    public static int MSG_TYPE_OTHER_IMG_REPLAY = 7;

    private String text;
    private String imageUrl;
    private int messageType;

    public ChatMessageModel(String text, String imageUrl, int messageType) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.messageType = messageType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
