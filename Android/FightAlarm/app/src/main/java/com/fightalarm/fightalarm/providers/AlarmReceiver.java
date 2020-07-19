package com.fightalarm.fightalarm.providers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;


/**
 * Created by Presly on 21/09/18.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        OtherLocalStore otherLocalStore = new OtherLocalStore(context);

        String info = "Fight Started: " + otherLocalStore.getDescription();

        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        //Stop sound service to play sound for alarm
        context.startService(new Intent(context, AlarmSoundService.class));

        //This will send a notification message and show notification in notification tray
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmNotificationService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));

    }


}
