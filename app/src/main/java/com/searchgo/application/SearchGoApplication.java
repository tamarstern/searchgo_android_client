package com.searchgo.application;

import android.app.Application;

import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashSet;

import static com.searchgo.constants.ServiceConstants.SERVER_API_URL;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class SearchGoApplication extends Application{

    private RestAdapter adapter;

    private HashSet<EmergencyEventServiceDto> searchEventsToSave = new HashSet<EmergencyEventServiceDto>();

    public RestAdapter getLoopBackAdapter() {
        if (adapter == null) {

            adapter = new RestAdapter(
                    getApplicationContext(), SERVER_API_URL);

        }
        return adapter;
    }

    public HashSet<EmergencyEventServiceDto> getSearchEventsToSave() {
        return searchEventsToSave;
    }
}
