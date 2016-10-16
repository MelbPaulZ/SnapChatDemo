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
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
//    public static final String MESSAGE_TYPE_REGISTRATION = "MessageTypeRegistration";
//    public static final String MESSAGE_TYPE_CHAT = "MessageTypeChat";

    public static final String REGISTRATION_SUCCESS = "MessageTypeRegistration";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("sender"),
                remoteMessage.getData().get("chat_message"),
                remoteMessage.getData().get("chat_message_type"));
    }

    //  show a notification for received message
    private void showNotification(String sender, String message, String messageType){
        if (!MainActivity.isAppCreated) {
            // to be broad
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("message", message);
            intent.putExtra("message_type",messageType);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

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
        }

        Intent intentRece = new Intent(REGISTRATION_SUCCESS);
        intentRece.putExtra("message", message);
        intentRece.putExtra("message_type",messageType);

        // broadcast the message to any receiver
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentRece);

    }

}

