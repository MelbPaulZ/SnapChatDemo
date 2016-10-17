package com.example.paul.snapchatdemo.api;

import com.example.paul.snapchatdemo.bean.PushMessage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface PushMessageApi {
    @PUT("push_message_queue.php?")
    Call<PushMessage> pushMessage(@Query("user_id") String userId,
                                  @Query("sender_user_id") String senderUserId,
                                  @Query("chat_message") String chatMessage,
                                  @Query("chat_message_type") String chatMessageType,
                                  @Query("chat_message_timer") String chatMessageTimer);

    @PUT("delete_message_by_sender_id.php?")
    Call<PushMessage> deleteMessage(@Query("user_id") String userId,
                                  @Query("sender_user_id") String senderUserId);

    @PUT("get_message_by_sender_id.php?")
    Call<ArrayList<PushMessage>> getMessageBySenderId(@Query("user_id") String userId,
                                  @Query("sender_user_id") String senderUserId);

    @PUT("get_message_by_user_id.php?")
    Call<ArrayList<PushMessage>> getMessagebyUserId(@Query("user_id") String userId);
}
