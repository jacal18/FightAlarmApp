package com.fightalarm.fightalarm.providers;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import com.fightalarm.fightalarm.helpers.ForegroundCheckTask;
import com.fightalarm.fightalarm.R;

/**
 * Created by Presly on 21/09/18.
 */


public class AlarmSoundService extends Service {
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Start media player
        try {
            boolean foregroud = new ForegroundCheckTask().execute(this.getApplicationContext()).get();
            if(!foregroud || !screenOn()){
                mediaPlayer = MediaPlayer.create(this, R.raw.bell);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);//set looping true to run it infinitely
            } else {
                mediaPlayer = MediaPlayer.create(this, R.raw.woop);
                mediaPlayer.start();
            }
        } catch (Exception e){

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //On destory stop and release the media player
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    public boolean screenOn(){
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        Boolean on = false;
        if (Build.VERSION.SDK_INT >= 20) {
            on = pm.isInteractive();
        } else {
            on = pm.isScreenOn();
        }
        return on;
    }
}
