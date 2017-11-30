package com.a4dn.aularm.aularm;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PlayService extends Service {

    MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("In the service,", "start command");

        // New Player that infinitely loops
        player = MediaPlayer.create(this, R.raw.analog);
        player.setLooping(true);
        player.start();

        // No more services are created once killed
        return START_NOT_STICKY;
    }
}
