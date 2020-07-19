package com.fightalarm.fightalarm.providers;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.activity.HomeActivity;

/**
 * Created by Presly on 21/09/18.
 */

public class AlarmNotificationService extends IntentService {
    private NotificationManager alarmNotificationManager;
    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 1;

    public AlarmNotificationService() {
        super("AlarmNotificationService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        OtherLocalStore otherLocalStore = new OtherLocalStore(getApplicationContext());
        //Send notification
        sendNotification("Wake Up! Fight has started!!", otherLocalStore.getDescription());
    }

    //handle notification
    private void sendNotification(String title, String description) {
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        //get pending intent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        //Create notification
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle(title).setSmallIcon(R.mipmap.logo)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                .setContentText(description).setAutoCancel(true);
        alamNotificationBuilder.setContentIntent(contentIntent);

        //notify notification manager about new notification
        alarmNotificationManager.notify(NOTIFICATION_ID, alamNotificationBuilder.build());
    }


}
