package com.fightalarm.fightalarm.providers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.activity.HomeActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

public class Notification extends FirebaseMessagingService {

    Context context;
    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;
    private PendingIntent pendingIntent;
    private Alarm alarm;

    public Notification(Context context) {
        this.context = context;
    }

    public void subscribeToTopic(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    public void unsubscribeFromTopic(String topic){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }

}
