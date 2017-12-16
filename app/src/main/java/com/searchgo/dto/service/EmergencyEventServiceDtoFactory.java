package com.searchgo.dto.service;

import com.searchgo.application.SearchGoApplication;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class EmergencyEventServiceDtoFactory {

    public static EmergencyEventServiceDto generateEmergencyEventServiceDto(SearchGoApplication application) {
        EmergencyEventRepository repository = getEmergencyEventRepository(application);
        EmergencyEventServiceDto dto = repository.createObject(new HashMap<String, Object>());
        String id = UUID.randomUUID().toString();
        dto.setId(id);
        return dto;
    }

    public static EmergencyEventRepository getEmergencyEventRepository(SearchGoApplication application) {
        RestAdapter adapter = application.getLoopBackAdapter();
        return adapter.createRepository(EmergencyEventRepository.class);
    }
}
