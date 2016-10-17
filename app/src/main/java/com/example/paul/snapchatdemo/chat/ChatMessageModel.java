package com.example.paul.snapchatdemo.chat;

/**
 * Created by verramukty on 9/26/2016.
 */
public class ChatMessageModel {

    public static String MSG_DATA_TEXT = "1";
    public static String MSG_DATA_IMG = "2";

    public static int MSG_TYPE_MINE_TEXT_SENT = 1;
    public static int MSG_TYPE_MINE_TEXT_PENDING = 2;
    public static int MSG_TYPE_MINE_IMG_SENT = 3;
    public static int MSG_TYPE_MINE_IMG_PENDING = 4;

    public static int MSG_TYPE_OTHER_TEXT = 5;
    public static int MSG_TYPE_OTHER_IMG = 6;
    public static int MSG_TYPE_OTHER_IMG_TIMER_VIEW = 7;
    public static int MSG_TYPE_OTHER_IMG_TIMER_REPLAY = 8;

    private String text;
    private String imageUrl;
    private int messageType;
    private int messageTimer;
    private int viewQuota;
    private int messageIdx;

    public ChatMessageModel(String text, String imageUrl, int messageType, int messageTimer, int messageIdx) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.messageType = messageType;
        this.messageTimer = messageTimer;
        this.viewQuota = 2;
        this.messageIdx = messageIdx;
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

    public int getMessageTimer() { return messageTimer; }

    public void setMessageTimer(int messageTimer) {
        this.messageTimer = messageTimer;
    }

    public void reduceViewQuota() {
        viewQuota = viewQuota - 1;
    }

    public int getViewQuota() {
        return viewQuota;
    }

    public int getMessageIdx() {
        return messageIdx;
    }

    public void setMessageIdx(int messageIdx) {
        this.messageIdx = messageIdx;
    }
}
