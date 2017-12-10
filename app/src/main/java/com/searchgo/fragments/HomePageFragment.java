package com.searchgo.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.searchgo.HomePageActivity;
import com.searchgo.R;

/**
 * Created by tamar.twena on 11/21/2017.
 */

public class HomePageFragment extends Fragment {


    private static final String TAG = HomePageFragment.class.getSimpleName();
    private HomePageActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home_page_fragment, container, false);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        if (childFragment.getId() == R.id.mapHomePage) {
            if (activity != null) {
                Log.d(TAG, "Found home page activity");
                SupportMapFragment mapFragment = (SupportMapFragment) childFragment;
                ((SupportMapFragment) childFragment).getMapAsync(activity);
            }
            else {
                Log.d(TAG, "Did not find home page activity");
            }
        }
    }

    public void setHomePageActivity(HomePageActivity activity) {
        this.activity = activity;
    }

}
