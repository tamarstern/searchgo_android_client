package com.searchgo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.searchgo.HomePageActivity;

import java.util.HashMap;

/**
 * Created by tamar.twena on 11/21/2017.
 */



public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs = 2;
    private HashMap<Integer, Fragment> fragmentMap = new HashMap<Integer, Fragment>();

    public PageAdapter(FragmentManager fm, int NumOfTabs, HomePageActivity homePageActivity) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        HomePageFragment homePageFragment = new HomePageFragment();
        homePageFragment.setHomePageActivity(homePageActivity);
        fragmentMap.put(0, homePageFragment);
        MyEventsFragment myEventsFragment = new MyEventsFragment();
        fragmentMap.put(1, myEventsFragment);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return fragmentMap.get(0);
            case 1:
                return fragmentMap.get(1);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
