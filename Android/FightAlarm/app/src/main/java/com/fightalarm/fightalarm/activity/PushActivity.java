package com.fightalarm.fightalarm.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.providers.Alarm;
import com.fightalarm.fightalarm.providers.AlarmReceiver;
import com.fightalarm.fightalarm.providers.OtherLocalStore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import me.leolin.shortcutbadger.ShortcutBadger;

public class PushActivity extends FirebaseMessagingService {

    private static final int ALARM_REQUEST_CODE = 133;
    private PendingIntent pendingIntent;
    private Alarm alarm;
    private OtherLocalStore otherLocalStore;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        super.onMessageReceived(remoteMessage);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        otherLocalStore = new OtherLocalStore(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String topic = remoteMessage.getData().get("topic");
        String title = remoteMessage.getData().get("title");
        String description = remoteMessage.getData().get("description");
        if (!topic.equalsIgnoreCase("users") && !topic.equalsIgnoreCase("admin") && !topic.equalsIgnoreCase("all")) {

            if (title != null && !title.toLowerCase().contains("update")) {
                otherLocalStore.setDescription(description);
                Intent alarmIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), ALARM_REQUEST_CODE, alarmIntent, 0);
                alarm = new Alarm(pendingIntent, getApplicationContext());
                alarm.triggerAlarmManager(2);
                addBadge();
            } else {
                NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentText(description)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                notification.setSound(sound);
            }
        }
    }

    public void addBadge() {
        OtherLocalStore otherLocalStore = new OtherLocalStore(getApplicationContext());
        int badgenumber = otherLocalStore.getBadge();
        badgenumber += 1;
        boolean applied = ShortcutBadger.applyCount(PushActivity.this, badgenumber);
    }

}
