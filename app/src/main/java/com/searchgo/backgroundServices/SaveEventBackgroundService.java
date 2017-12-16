package com.searchgo.backgroundServices;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.searchgo.application.SearchGoApplication;
import com.searchgo.dto.service.EmergencyEventServiceDto;
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
            final HashSet<EmergencyEventServiceDto> searchEventsToSaveContainer = app.getSearchEventsToSave();
            HashSet<EmergencyEventServiceDto> containerToItertateOn = new HashSet<>();
            containerToItertateOn.addAll(searchEventsToSaveContainer);
            for (final EmergencyEventServiceDto searchEventsToSave : containerToItertateOn) {
                searchEventsToSave.save(new Model.Callback() {

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
