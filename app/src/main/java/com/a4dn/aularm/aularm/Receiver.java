package com.a4dn.aularm.aularm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by rob on 11/26/17.
 */

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag");

        // Lock acquisition
        wl.acquire();

        Log.v("ADebugTag", "It work!");

        // Start alarm
        Intent play_alarm_intent = new Intent(context, PlayService.class);
        context.startService(play_alarm_intent);

        // Lock release
        wl.release();

    }
}
