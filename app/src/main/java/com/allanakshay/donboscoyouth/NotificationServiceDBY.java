package com.allanakshay.donboscoyouth;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Allan Akshay on 24-08-2017.
 */

public class NotificationServiceDBY extends Service {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference referencegroup;
    DatabaseReference referencefinance;
    DatabaseReference referencemom;
    DatabaseReference referenceevent;
    private final String PREFERENECE = "DBY-preferenceFile-AccountID";
    private String id, newpos;
    private SharedPreferences preference;
    private String momnoti, eventnoti, groupnoti, financenoti;

    @Override
    public void onCreate() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceevent = firebaseDatabase.getReference().child("Upcoming_Events");
        referencemom = firebaseDatabase.getReference().child("MOM");
        referencefinance = firebaseDatabase.getReference().child("Finance_List");
        referencegroup = firebaseDatabase.getReference().child("Youth_List");
        preference = getSharedPreferences(PREFERENECE, MODE_PRIVATE);
        id = preference.getString("ID", "null");
        if (!id.equals("null")) {
            Query query = referencegroup.orderByChild("Username").equalTo(id);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            momnoti = snapshot.child("Notification_Viewed_MOM").getValue().toString();
                            eventnoti = snapshot.child("Notification_Viewed_Event").getValue().toString();
                            groupnoti = snapshot.child("Notification_Viewed_Group").getValue().toString();
                            financenoti = snapshot.child("Notification_Viewed_Finance").getValue().toString();
                            newpos = snapshot.getKey();
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            referencemom.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {

                        if (preference.contains("ID")) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (momnoti.equals("No")) {
                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationServiceDBY.this);
                                        mBuilder.setContentTitle("Don Bosco Youth");
                                        mBuilder.setContentText("A new MOM Entry has been added. Please do check it out!!");
                                        mBuilder.setSmallIcon(R.drawable.notification_icon);

                                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        mBuilder.setSound(alarmSound);

                                        // Creates an explicit intent for an Activity in your app
                                        Intent resultIntent = new Intent(NotificationServiceDBY.this, LoginPage.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationServiceDBY.this);
// Adds the back stack for the Intent (but not the Intent itself)
                                        stackBuilder.addParentStack(LoginPage.class);
// Adds the Intent that starts the Activity to the top of the stack
                                        stackBuilder.addNextIntent(resultIntent);
                                        PendingIntent resultPendingIntent =
                                                stackBuilder.getPendingIntent(
                                                        0,
                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                );
                                        mBuilder.setContentIntent(resultPendingIntent);
                                        NotificationManager mNotificationManager =
                                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                        // mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
                                        mNotificationManager.notify(1, mBuilder.build());
                                        referencegroup.child(newpos).child("Notification_Viewed_MOM").setValue("Yes");
                                    }

                                }
                            }, 2000);

                        }
                    } catch(Exception e) {}

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

            referenceevent.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {

                        if (preference.contains("ID")) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (eventnoti.equals("No")) {
                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationServiceDBY.this);
                                        mBuilder.setContentTitle("Don Bosco Youth");
                                        mBuilder.setContentText("A new Event has been added. Please do check it out!!");
                                        mBuilder.setSmallIcon(R.drawable.notification_icon);

                                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        mBuilder.setSound(alarmSound);

                                        // Creates an explicit intent for an Activity in your app
                                        Intent resultIntent = new Intent(NotificationServiceDBY.this, LoginPage.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationServiceDBY.this);
// Adds the back stack for the Intent (but not the Intent itself)
                                        stackBuilder.addParentStack(LoginPage.class);
// Adds the Intent that starts the Activity to the top of the stack
                                        stackBuilder.addNextIntent(resultIntent);
                                        PendingIntent resultPendingIntent =
                                                stackBuilder.getPendingIntent(
                                                        0,
                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                );
                                        mBuilder.setContentIntent(resultPendingIntent);
                                        NotificationManager mNotificationManager =
                                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                        // mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
                                        mNotificationManager.notify(2, mBuilder.build());
                                        referencegroup.child(newpos).child("Notification_Viewed_Event").setValue("Yes");
                                    }

                                }
                            }, 2000);

                        }
                    } catch(Exception e) {}

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

            referencegroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {

                        if (preference.contains("ID")) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (groupnoti.equals("No")) {
                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationServiceDBY.this);
                                        mBuilder.setContentTitle("Don Bosco Youth");
                                        mBuilder.setContentText("A new Member has been added. Please do check it out!!");
                                        mBuilder.setSmallIcon(R.drawable.notification_icon);

                                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        mBuilder.setSound(alarmSound);

                                        // Creates an explicit intent for an Activity in your app
                                        Intent resultIntent = new Intent(NotificationServiceDBY.this, LoginPage.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationServiceDBY.this);
// Adds the back stack for the Intent (but not the Intent itself)
                                        stackBuilder.addParentStack(LoginPage.class);
// Adds the Intent that starts the Activity to the top of the stack
                                        stackBuilder.addNextIntent(resultIntent);
                                        PendingIntent resultPendingIntent =
                                                stackBuilder.getPendingIntent(
                                                        0,
                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                );
                                        mBuilder.setContentIntent(resultPendingIntent);
                                        NotificationManager mNotificationManager =
                                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                        // mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
                                        mNotificationManager.notify(3, mBuilder.build());
                                        referencegroup.child(newpos).child("Notification_Viewed_Group").setValue("Yes");
                                    }

                                }
                            }, 2000);

                        }
                    } catch(Exception e) {}

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

            referencefinance.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {

                        if (preference.contains("ID")) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (financenoti.equals("No")) {
                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationServiceDBY.this);
                                        mBuilder.setContentTitle("Don Bosco Youth");
                                        mBuilder.setContentText("A new Finance Entry has been added. Please do check it out!!");
                                        mBuilder.setSmallIcon(R.drawable.notification_icon);

                                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        mBuilder.setSound(alarmSound);

                                        // Creates an explicit intent for an Activity in your app
                                        Intent resultIntent = new Intent(NotificationServiceDBY.this, LoginPage.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationServiceDBY.this);
// Adds the back stack for the Intent (but not the Intent itself)
                                        stackBuilder.addParentStack(LoginPage.class);
// Adds the Intent that starts the Activity to the top of the stack
                                        stackBuilder.addNextIntent(resultIntent);
                                        PendingIntent resultPendingIntent =
                                                stackBuilder.getPendingIntent(
                                                        0,
                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                );
                                        mBuilder.setContentIntent(resultPendingIntent);
                                        NotificationManager mNotificationManager =
                                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                        // mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
                                        mNotificationManager.notify(4, mBuilder.build());
                                        referencegroup.child(newpos).child("Notification_Viewed_Finance").setValue("Yes");
                                    }

                                }
                            }, 2000);

                        }
                    } catch(Exception e) {}

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Service stopped...", Toast.LENGTH_SHORT).show();
    }
}
