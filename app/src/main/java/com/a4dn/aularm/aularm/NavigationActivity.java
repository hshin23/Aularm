package com.a4dn.aularm.aularm;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private static FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference userRef;

    final String TAG = "NAVIGATION_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mAuth =  FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid());

        Log.i(TAG, "Initializatioin started...");
        setToolbar();
        setNavigation();
        Log.i(TAG, "Initializatioin complete...");
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i(TAG, "R.id.toolbar set as ActionBar");
    }

    private void setNavigation() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Log.e(TAG, String.valueOf(getIntent().getIntExtra("page", 0)));
        mViewPager.setCurrentItem(getIntent().getIntExtra("page", 1));

        Log.i(TAG, "SectionsPagerAdapter and ViewPager set");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_signout:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }
                startActivity(new Intent(this, LoginActivity.class));
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ClockFragment extends Fragment {

        final String TAG = "CLOCK_FRAGMENT";
        DatabaseReference mDatabase;
        FirebaseAuth mAuth;

        public ClockFragment() {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
        }

        public static ClockFragment newInstance() {
            return new ClockFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Initialize objects to use alarms
            View rootView = inflater.inflate(R.layout.fragment_clock, container, false);

            final AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(ALARM_SERVICE);
            final Calendar calendar = Calendar.getInstance();
            final TimePicker clock = rootView.findViewById(R.id.timePicker);
            final PendingIntent[] intent = new PendingIntent[1];
            final TextView time = rootView.findViewById(R.id.timeView);

            mDatabase.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        time.setText(dataSnapshot.child("alarm").getValue().toString());
                    } catch (Exception e) {
                        time.setText("");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "error:", databaseError.toException());
                }
            });

            Button btn_set = rootView.findViewById(R.id.btn_set);
            Button btn_cancel = rootView.findViewById(R.id.btn_cancel);

            btn_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent[0] = setAlarm(alarmManager, calendar, clock.getHour(), clock.getMinute());
                }});

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelAlarm(alarmManager, intent[0]);
                }});

            return rootView;
        }

        private PendingIntent setAlarm(AlarmManager alarmManager, Calendar calendar, int hourOfDay,  int minute) {
            // Pre-process Calendar object for calculation
            Calendar cur_cal = Calendar.getInstance();
            cur_cal.set(Calendar.SECOND, 0);
            calendar.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            // difference in times
            // = 0 if same time
            // > 0 if alarm time is before (set next day alarm)
            // < 0 if alarm time is after  (set same day alarm)
            int time_diff = cur_cal.compareTo(calendar);
            if (time_diff == 0) {
                Toast.makeText(getActivity().getApplicationContext(), "Wrong time set", Toast.LENGTH_SHORT);
            }

            if (time_diff > 0) {
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            }

            // Intent to send to receiver
            Intent intent = new Intent(getActivity().getApplicationContext(), Receiver.class);

            // Delay intent until given time
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // send alarm to manager
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            // Update current alarm time to firebase
            write(mAuth.getUid(), "alarm", calendar.getTime().toString());

            return pendingIntent;
        }

        private void cancelAlarm(AlarmManager alarmManager, PendingIntent pendingIntent) {
            if (pendingIntent == null) {
                // Intent to send to receiver
                Intent intent = new Intent(getActivity().getApplicationContext(), Receiver.class);
                // Delay intent until given time
                pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
            }
            // send alarm to manager
            alarmManager.cancel(pendingIntent);
            write(mAuth.getUid(), "alarm", " ");
        }

        void write(String uid, String keyString, String msg) {
            String key = mDatabase.child(uid).getKey();
            Map<String, Object> childUpdates = new HashMap<>();
            System.out.println("Writing to Firebase: " + keyString + " = " + msg);
            childUpdates.put("/users/" + uid + "/" + keyString, msg);

            mDatabase.updateChildren(childUpdates);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CalendarFragment extends Fragment {

        public static CalendarFragment newInstance() {
            return new CalendarFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_calendar, container, false);
        }

    }

    public static class QuestionnaireFragment extends Fragment {
        DatabaseReference mDatabase;

        public QuestionnaireFragment() {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        public static QuestionnaireFragment newInstance() {
            return new QuestionnaireFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_quest, container, false);

            Button butt = rootView.findViewById(R.id.gen_alarm_butt);

            final EditText anOne = rootView.findViewById(R.id.answer_1);
            final EditText anTwo = rootView.findViewById(R.id.answer_2);
            final EditText anThree = rootView.findViewById(R.id.answer_3);

            butt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View goTo) {
                    String checkOne = anOne.getText().toString();
                    String checkTwo = anTwo.getText().toString();
                    String checkThree = anThree.getText().toString();
                    String hours = "";
                    String sleep_timeVal = "";
                    String wake_timeVal = "";

                    if (!checkOne.equals("")) {
                        if ((Integer.parseInt(checkOne) < 24) && (Integer.parseInt(checkOne) >= 0)) {
                            hours = checkOne;
                        } else {
                            hours = "8";
                        }
                    }
                    if (!checkTwo.equals("")) {
                        if ((Integer.parseInt(checkTwo) < 24) && (Integer.parseInt(checkTwo) >= 0)) {
                            sleep_timeVal = checkTwo;
                        } else {
                            sleep_timeVal = "17";
                        }
                    }
                    if (!checkThree.equals("")) {
                        if ((Integer.parseInt(checkThree) < 24) && (Integer.parseInt(checkThree) >= 0)) {
                            wake_timeVal = checkThree;
                        } else {
                            wake_timeVal = "3";
                        }
                    }

                    Log.d("hours", hours);
                    Log.d("sleep_timeVal",sleep_timeVal);
                    Log.d("wake_timeVal",wake_timeVal);
                    Log.i("currentUser",  mAuth.getCurrentUser().getUid());
                    String uid = mAuth.getCurrentUser().getUid();
                    write(uid, "hours", hours);
                    write(uid, "sleep", sleep_timeVal);
                    write(uid, "wake", wake_timeVal);
                    write(uid,  "firsttime", "false");
                    Log.i("UID", uid);
                }
            });

            return rootView;
        }

        void write(String uid, String keyString, String msg) {
            String key = mDatabase.child(uid).getKey();
            Map<String, Object> childUpdates = new HashMap<>();
            System.out.println("Writing to Firebase: " + keyString + " = " + msg);
            childUpdates.put("/users/" + uid + "/" + keyString, msg);

            mDatabase.updateChildren(childUpdates);
        }
    }
    //setting up the settings option fragment
    //still missing a lot of functionality like when user sleects the heavy alarm and we should
    //different sound profile or multiple alarms

    public static class SettingsFragment extends Fragment {
        DatabaseReference mDatabase;
         CheckBox one,two,three;


        public SettingsFragment() {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        public static SettingsFragment newInstance() {

            return new SettingsFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.settings, container, false);

            final CheckBox one =rootView.findViewById(R.id.checkBox);
            final CheckBox two =rootView.findViewById(R.id.checkBox2);
            final CheckBox three =rootView.findViewById(R.id.checkBox3);
            Button butt = rootView.findViewById(R.id.gen_alarm_butt);
/*
            final EditText anOne = rootView.findViewById(R.id.answer_1);
            final EditText anTwo = rootView.findViewById(R.id.answer_2);
            final EditText anThree = rootView.findViewById(R.id.answer_3);

            butt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View goTo) {
                    String checkOne = anOne.getText().toString();
                    String checkTwo = anTwo.getText().toString();
                    String checkThree = anThree.getText().toString();
                    String hours = "";
                    String sleep_timeVal = "";
                    String wake_timeVal = "";

                    if (!checkOne.equals("")) {
                        if ((Integer.parseInt(checkOne) < 24) && (Integer.parseInt(checkOne) >= 0)) {
                            hours = checkOne;
                        } else {
                            hours = "8";
                        }
                    }
                    if (!checkTwo.equals("")) {
                        if ((Integer.parseInt(checkTwo) < 24) && (Integer.parseInt(checkTwo) >= 0)) {
                            sleep_timeVal = checkTwo;
                        } else {
                            sleep_timeVal = "17";
                        }
                    }
                    if (!checkThree.equals("")) {
                        if ((Integer.parseInt(checkThree) < 24) && (Integer.parseInt(checkThree) >= 0)) {
                            wake_timeVal = checkThree;
                        } else {
                            wake_timeVal = "3";
                        }
                    }

                    Log.d("hours", hours);
                    Log.d("sleep_timeVal",sleep_timeVal);
                    Log.d("wake_timeVal",wake_timeVal);
                    Log.i("currentUser",  mAuth.getCurrentUser().getUid());
                    String uid = mAuth.getCurrentUser().getUid();
                    write(uid, "hours", hours);
                    write(uid, "sleep", sleep_timeVal);
                    write(uid, "wake", wake_timeVal);
                    write(uid,  "firsttime", "false");
                    Log.i("UID", uid);
                }
            });
          */

            butt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringBuffer result = new StringBuffer();
                    result.append("Heavy Alarm profile : ").append(one.isChecked());
                    result.append("Normal Alarm profie: ").append(two.isChecked());
                    result.append("lite Alarm profile:  ") .append(three.isChecked());

                }
            });


            return rootView;
        }

        void write(String uid, String keyString, String msg) {
            String key = mDatabase.child(uid).getKey();
            Map<String, Object> childUpdates = new HashMap<>();
            System.out.println("Writing to Firebase: " + keyString + " = " + msg);
            childUpdates.put("/users/" + uid + "/" + keyString, msg);

            mDatabase.updateChildren(childUpdates);

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            getItem(1);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CalendarFragment.newInstance();
                case 1:
                    return ClockFragment.newInstance();
                case 2:
                    return QuestionnaireFragment.newInstance();
                case 3:
                    return SettingsFragment.newInstance(); // display this after
                default:
                    return ClockFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}
