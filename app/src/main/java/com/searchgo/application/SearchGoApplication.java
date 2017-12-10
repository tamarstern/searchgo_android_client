package com.searchgo.application;

import android.app.Application;

import com.searchgo.dto.activity.EmergencyEventActivityDto;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashSet;

import static com.searchgo.constants.ServiceConstants.SERVER_URL;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class SearchGoApplication extends Application{

    private RestAdapter adapter;

    private HashSet<EmergencyEventActivityDto> searchEventsToSave = new HashSet<EmergencyEventActivityDto>();

    public RestAdapter getLoopBackAdapter() {
        if (adapter == null) {

            adapter = new RestAdapter(
                    getApplicationContext(), SERVER_URL);

        }
        return adapter;
    }

    public HashSet<EmergencyEventActivityDto> getSearchEventsToSave() {
        return searchEventsToSave;
    }
}