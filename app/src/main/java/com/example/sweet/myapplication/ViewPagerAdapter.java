package com.example.sweet.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT =12;
    private String titles[] ;
    private ArrayList<SampleFragment> lists;
    private String[] urls = {
            "https://v2ex.com/?tab=tech",
            "https://v2ex.com/?tab=creative",
            "https://v2ex.com/?tab=play",
            "https://v2ex.com/?tab=apple",
            "https://v2ex.com/?tab=jobs",
            "https://v2ex.com/?tab=deals",
            "https://v2ex.com/?tab=city",
            "https://v2ex.com/?tab=qna",
            "https://v2ex.com/?tab=hot",
            "https://v2ex.com/?tab=all",
            "https://v2ex.com/?tab=r2",
            "https://v2ex.com/?tab=nodes"
    };

    public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        return SampleFragment.newInstance(position, urls[position]);
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}