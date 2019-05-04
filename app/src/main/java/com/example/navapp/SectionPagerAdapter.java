package com.example.navapp;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


//Class for storing fragment for tabs

public class SectionPagerAdapter extends FragmentPagerAdapter {


    private static final String TAG = "SectionPagerAdapter";
    private final List<Fragment> mFragment=new ArrayList<>();


    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    public void addFragment(Fragment fragment)
    {
        mFragment.add(fragment);
    }
}
