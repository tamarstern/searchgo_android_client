package com.searchgo.backgroundServices;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.searchgo.application.SearchGoApplication;
import com.searchgo.dto.service.EmergencyEventRepository;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.dto.service.EmergencyEventServiceDtoFactory;
import com.strongloop.android.loopback.callbacks.ListCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tamar.twena on 12/19/2017.
 */

public class GetMyEventsBackgroundService extends IntentService {

    public GetMyEventsBackgroundService() {
        super("getMyEventsBackgroundService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final SearchGoApplication app = (SearchGoApplication)getApplication();
        EmergencyEventRepository emergencyEventRepository = EmergencyEventServiceDtoFactory.getEmergencyEventRepository(app);

        /*emergencyEventRepository.findEventsUserCreated(new ListCallback<EmergencyEventServiceDto>() {
            @Override
            public void onSuccess(List<EmergencyEventServiceDto> dtos) {
                app.resetMyEvents(dtos);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("failGetMyEvents", "failed to get my events", t);
            }
        });*/

    }
}
