package com.a4dn.aularm.aularm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

        player = MediaPlayer.create(this, R.raw.analog);
        player.start();

        return START_NOT_STICKY;
    }
}
