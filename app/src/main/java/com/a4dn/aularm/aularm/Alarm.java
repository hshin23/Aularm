package com.a4dn.aularm.aularm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.test.suitebuilder.annotation.Suppress;
import android.view.View;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.Transaction;



/**
 * Created by rmereddy on 11/25/17.
 */

public class Alarm extends AppCompatActivity {
    TimePicker tp;
    long currentTime;
    int alarmTime;
    int alarmHourmil;
    DigitalClock dc;
    AlarmManager m;
    PendingIntent p;

    @SuppressLint("NewApi")
    public void makeAlarm(Context ct) {
        // m = new AlarmManager();
        currentTime = tp.getHour();
        alarmTime = 20;
        alarmHourmil = 19;
        Intent i = new Intent(this, Alarm.class);
        PendingIntent p = PendingIntent.getActivity(ct,100,i,0);
        m.set(AlarmManager.RTC_WAKEUP,(20*360000),p);
    }
    public void cancel(Context c) {
        m.cancel(p);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        Button btnGenerate = (Button) findViewById(R.id.gen_alarm_butt);
        //btnGenerate.setOnClickListener(new View.OnClickListener() {
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                /*
                switch(view.getId())
                {
                    case R.id.gen_alarm_butt:
                       // Toast.makeText(this, "Button1 clicked.", Toast.LENGTH_SHORT).show();
                }
                */

               // Intent intent = new Intent()
            }
        });
    }

}








