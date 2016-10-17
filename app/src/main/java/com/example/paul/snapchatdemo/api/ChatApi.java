package com.example.paul.snapchatdemo.api;

import com.example.paul.snapchatdemo.bean.User;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ChatApi {

    @PUT("send_message_demo.php?")
    Call<User> sendMessage(@Query("sender_user_id") String senderUserId,
                        @Query("receiver_user_id") String receiverUserId,
                        @Query("chat_message") String chatMessage,
                        @Query("chat_message_type") String chatMessageType,
                        @Query("chat_message_timer") String chatMessageTimer);
}
