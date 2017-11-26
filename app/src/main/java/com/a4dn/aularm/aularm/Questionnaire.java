package com.a4dn.aularm.aularm;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseUser;

import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Questionnaire extends Activity {
    /*String hours,sleep_timeVal,wake_timeVal;
    FirebaseHelper firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // This is an initialization state
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quest);
        firebase = new FirebaseHelper();
        //
        //This navigates to the alarm
        Button butt = (Button) findViewById(R.id.gen_alarm_butt);
        butt.setOnClickListener(new View.OnClickListener() {
            //Gets the next clock page
            //
            @Override
            public void onClick(View goTo) {
                EditText anOne = findViewById(R.id.answer_1);
                EditText anTwo = findViewById(R.id.answer_2);
                EditText anThree = findViewById(R.id.answer_3);
                String checkOne = anOne.getText().toString();
                String checkTwo = anTwo.getText().toString();
                String checkThree = anThree.getText().toString();

                if (checkOne != "")
                    if ((Integer.parseInt(checkOne) < 24) && (Integer.parseInt(checkOne) >= 0))
                        hours = checkOne;
                    else
                        hours = "8";
                if (checkTwo != "")
                    if ((Integer.parseInt(checkTwo) < 24) && (Integer.parseInt(checkTwo) >= 0))
                        sleep_timeVal = checkTwo;
                    else
                        sleep_timeVal = "17";
                if (checkThree != "")
                    if ((Integer.parseInt(checkThree) < 24) && (Integer.parseInt(checkThree) >= 0))
                        wake_timeVal = checkThree;
                    else
                        wake_timeVal = "3";
                //
                Log.d("hours",hours);
                Log.d("sleep_timeVal",sleep_timeVal);
                Log.d("wake_timeVal",wake_timeVal);
                firebase = new FirebaseHelper();
                FirebaseUser f = firebase.getUser();
                firebase.write(f.getUid(),hours);
                firebase.write(f.getUid(),sleep_timeVal);
                firebase.write(f.getUid(),wake_timeVal);
                setContentView(R.layout.fragment_clock);
                //Activity.StartActivity(new Intent(this,fragment_clock.class));
            }
        });

        */
    }
}