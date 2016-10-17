package com.example.paul.snapchatdemo.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.PushMessageApi;
import com.example.paul.snapchatdemo.bean.PushMessage;
import com.example.paul.snapchatdemo.utils.HttpUtil;
import com.example.paul.snapchatdemo.utils.UserUtil;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public static final String RECEIVE_FIREBASE_CHAT_MESSAGE = "ReceiveFirebaseChatMessage";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("sender_user_id"),
                remoteMessage.getData().get("sender"),
                remoteMessage.getData().get("chat_message"),
                remoteMessage.getData().get("chat_message_type"),
                remoteMessage.getData().get("chat_message_timer"));
    }

    //  show a notification for received message
    private void showNotification(String senderUserId, String sender, String message, String messageType, String messageTimer){
        if (!MainActivity.isAppCreated) {
            // user is not opening the app

            // to be read on MainActivity.onPostCreate
            // which is called when user tap the notification message on their phone
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("sender_user_id", senderUserId);
            intent.putExtra("chat_message", message);
            intent.putExtra("chat_message_type",messageType);
            intent.putExtra("chat_message_timer", messageTimer);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            // create notification message
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Snapchat")
                    .setContentText(sender)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build());

            //Call api to put message into queue on server
            pushMessageToQueue(UserUtil.getId(), senderUserId, message, messageType, messageTimer);
        }
        else {
            // user is opening the app
            Intent intentRece = new Intent(RECEIVE_FIREBASE_CHAT_MESSAGE);
            intentRece.putExtra("sender_user_id", senderUserId);
            intentRece.putExtra("chat_message", message);
            intentRece.putExtra("chat_message_type",messageType);
            intentRece.putExtra("chat_message_timer", messageTimer);

            // broadcast the message to receiver on MainActivity because we cannot control UI from here
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentRece);
        }
    }

    public static void pushMessageToQueue(String userId, String senderUserId, String chatMessage,
                                          String chatMessageType, String chatMessageTimer) {

        PushMessageApi pushMessageApi = HttpUtil.accessServer(PushMessageApi.class);
        pushMessageApi.pushMessage(userId, senderUserId ,chatMessage, chatMessageType, chatMessageTimer).enqueue(new Callback<PushMessage>() {
            @Override
            public void onResponse(Call<PushMessage> call, Response<PushMessage> response) {
                // do nothing
            }

            @Override
            public void onFailure(Call<PushMessage> call, Throwable t) {
                // do nothing
            }
        });
    }


}

