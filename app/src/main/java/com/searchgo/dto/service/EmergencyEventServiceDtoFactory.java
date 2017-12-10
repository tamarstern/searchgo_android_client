package com.searchgo.dto.service;

import android.app.Activity;
import android.content.Context;

import com.searchgo.CreateEventActivity;
import com.searchgo.application.SearchGoApplication;
import com.searchgo.dto.activity.EmergencyEventActivityDto;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class EmergencyEventServiceDtoFactory {

    public static EmergencyEventServiceDto generateEmergencyEventService(SearchGoApplication application, EmergencyEventActivityDto dto) {

        EmergencyEventRepository repository = getEmergencyEventRepository(application);

        EmergencyEventServiceDto serviceEvent = repository.createObject(new HashMap<String,Object>());
        serviceEvent.setId(dto.getId());
        serviceEvent.setAddress(dto.getAddress());
        serviceEvent.setName(dto.getName());
        //serviceEvent.setDescription(dto.getDescription());
        serviceEvent.setLastSeen(dto.getLastSeen());
        serviceEvent.setCreatedOn(dto.getCreatedOn());
        serviceEvent.setCategory(dto.getCategory());
        return serviceEvent;

    }

    public static EmergencyEventRepository getEmergencyEventRepository(SearchGoApplication application) {
        RestAdapter adapter = application.getLoopBackAdapter();
        return adapter.createRepository(EmergencyEventRepository.class);
    }
}
