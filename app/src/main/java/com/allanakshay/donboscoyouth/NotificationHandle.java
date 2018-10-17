package com.allanakshay.donboscoyouth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by Allan Akshay on 07-07-2017.
 */

public class NotificationHandle extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent = new Intent();
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationHandle.this, 0, intent, 0);
        Notification builder = new  Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Don Bosco Youth")
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).getNotification();

        NotificationManager manager = (NotificationManager)     getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder);

    }
}
