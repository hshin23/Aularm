package com.a4dn.aularm.aularm;

import android.content.Intent;
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
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {
    String hours,sleep_timeVal,wake_timeVal;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public FirebaseHelper firebase;
    private static List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
    private final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        firebase = new FirebaseHelper();
    }

    private void foo() {
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
                String uid = firebase.getUser().getUid();
                firebase.write(uid, hours);
                firebase.write(uid, sleep_timeVal);
                firebase.write(uid, wake_timeVal);
                System.out.println("UID = " + uid);
                setContentView(R.layout.fragment_clock);
            }
        });
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
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class Clock extends Fragment {

        public static Clock newInstance() {
            Clock fragment = new Clock();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_clock, container, false);
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            /*switch (position) {
                case 1:
                    return  Questionnaire.newInstance();
                default:
                    return Clock.newInstance();
            }*/
            return Questionnaire.newInstance();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public static class Questionnaire extends Fragment {

        public static Questionnaire newInstance() {
            Questionnaire fragment = new Questionnaire();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_quest, container, false);
            //Button btn = rootView.findViewById(R.id.gen_alarm_butt);



            return rootView;
        }
    }


}
