package com.a4dn.aularm.aularm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class Receiver extends BroadcastReceiver {

    final String TAG = "RECEIVER";

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag");

        // Lock acquisition for 10 mins
        wl.acquire(10*60*1000L /*10 minutes*/);

        // Start alarm
        Intent play_alarm_intent = new Intent(context, PlayService.class);
        context.startService(play_alarm_intent);

        /* OTHER CODE GOES HERE */

        // Lock release
        wl.release();

    }
}
