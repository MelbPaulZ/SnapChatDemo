package com.example.paul.snapchatdemo.bean;

public class PushMessage {
    private String senderUserId;
    private String totalMessage;
    private String chatMessage;
    private String chatMessageType;
    private String chatMessageTimer;

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getTotalMessage() {
        return totalMessage;
    }

    public void setTotalMessage(String totalMessage) {
        this.totalMessage = totalMessage;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getChatMessageType() {
        return chatMessageType;
    }

    public void setChatMessageType(String chatMessageType) {
        this.chatMessageType = chatMessageType;
    }

    public String getChatMessageTimer() {
        return chatMessageTimer;
    }

    public void setChatMessageTimer(String chatMessageTimer) {
        this.chatMessageTimer = chatMessageTimer;
    }

//    @Override
//    public String toString() {
//        return "PushMessage{" +
//                "senderUserId='" + senderUserId + '\'' +
//                "totalMessage='" + totalMessage + '\'' +
//                "chatMessageType='" + chatMessage + '\'' +
//                "senderUserId='" + chatMessageType + '\'' +
//                "chatMessageTimer='" + chatMessageTimer + '\'' +
//                '}';
//    }
}
