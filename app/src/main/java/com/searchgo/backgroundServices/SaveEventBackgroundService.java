package com.searchgo.backgroundServices;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.searchgo.application.SearchGoApplication;
import com.searchgo.dto.activity.EmergencyEventActivityDto;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.dto.service.EmergencyEventServiceDtoFactory;
import com.strongloop.android.loopback.Model;

import java.util.HashSet;

/**
 * Created by tamar.twena on 11/29/2017.
 */

public class SaveEventBackgroundService extends IntentService {

    public SaveEventBackgroundService() {
        super("SaveEventBackgroundService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            Log.i("StartSaveEventService", "start StartSaveEventService");

            SearchGoApplication app = (SearchGoApplication) this.getApplication();
            final HashSet<EmergencyEventActivityDto> searchEventsToSaveContainer = app.getSearchEventsToSave();
            HashSet<EmergencyEventActivityDto> containerToItertateOn = new HashSet<>();
            containerToItertateOn.addAll(searchEventsToSaveContainer);
            for (final EmergencyEventActivityDto searchEventsToSave : containerToItertateOn) {
                EmergencyEventServiceDto serviceDto = EmergencyEventServiceDtoFactory.generateEmergencyEventService(app, searchEventsToSave);
                serviceDto.save(new Model.Callback() {

                    @Override
                    public void onSuccess() {
                        searchEventsToSaveContainer.remove(searchEventsToSave);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("ErrorOnSave", "Cannot save Emergency Event model.", t);
                    }
                });
            }
        } catch (Exception e) {
            Log.e("failedSaveEventService", "failed to save events", e);
        } finally {
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

    }
}
