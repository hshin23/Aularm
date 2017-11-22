package com.a4dn.aularm.aularm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class _MainActivity extends FragmentActivity implements ActionBar.TabListener{

    TabAdapter mTabAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__main);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public static class TabAdapter extends FragmentPagerAdapter {

        private final int COUNT = 3;

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    shatter();
                    // do stuff for the leftmost screen
                case 1:
                    shatter();
                    // do stuff for center screen
                case 2:
                    shatter();
                    // do stuff for the rightmost screen
            }
            return null;
        }

        @Override
        public int getCount() {
            return COUNT;
        }

        private Fragment shatter() {
            // TODO: create new fragment objects to display
            Fragment frag = new Wrapper();
            Bundle bundle = new Bundle();
            // TODO: add stuff into bundle to display
            frag.setArguments(bundle);
            return frag;
        }
    }

    public static class Wrapper extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
            View view = inflater.inflate(R.layout.activity_clock, container, false);
            Bundle bundle = getArguments();
            return view;
        }
    }
}
