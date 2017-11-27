package com.a4dn.aularm.aularm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
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
import android.widget.EditText;
import android.widget.TimePicker;

public class NavigationActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    FirebaseHelper firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        firebase = new FirebaseHelper();
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
                firebase.signOut(this);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ClockFragment extends Fragment {

        public static ClockFragment newInstance() {
            ClockFragment fragment = new ClockFragment();

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(ALARM_SERVICE);
            final Calendar calendar = Calendar.getInstance();

            View rootView = inflater.inflate(R.layout.fragment_clock, container, false);
            final TimePicker clock = rootView.findViewById(R.id.timePicker);
            Button set = rootView.findViewById(R.id.btn_set);
            Button cancel = rootView.findViewById(R.id.btn_cancel);
            final PendingIntent[] intent = new PendingIntent[1];

            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent[0] = setAlarm(alarmManager, calendar, clock.getHour(), clock.getMinute());
                }});

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelAlarm(alarmManager, intent[0]);
                }});

            return rootView;
        }

        private PendingIntent setAlarm(AlarmManager alarmManager, Calendar calendar, int hourOfDay,  int minute) {

            // Set alarm time
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            // Intent to send to receiver
            Intent intent = new Intent(getActivity().getApplicationContext(), Receiver.class);

            // Delay intent until given time
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // send alarm to manager
            // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_DAY , pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Log.i("Time Changed", String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

            return pendingIntent;
        }

        private void cancelAlarm(AlarmManager alarmManager, PendingIntent i) {
            alarmManager.cancel(i);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CalendarFragment extends Fragment {

        public static CalendarFragment newInstance() {
            CalendarFragment fragment = new CalendarFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
            return rootView;
        }

    }


    public static class QuestionnaireFragment extends Fragment {

        public static QuestionnaireFragment newInstance() {
            QuestionnaireFragment fragment = new QuestionnaireFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_quest, container, false);

            Button butt = (Button) rootView.findViewById(R.id.gen_alarm_butt);

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
                    FirebaseHelper firebase = new FirebaseHelper();

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
                    Log.i("currentUser",  firebase.getUser().getUid());
                    String uid = firebase.getUser().getUid();
                    firebase.write(uid, hours);
                    firebase.write(uid, sleep_timeVal);
                    firebase.write(uid, wake_timeVal);
                    Log.i("UID", uid);
                }
            });

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
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
