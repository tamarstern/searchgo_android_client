package com.searchgo.dto.service;

import com.searchgo.application.SearchGoApplication;
import com.strongloop.android.loopback.RestAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class EmergencyEventServiceDtoFactory {


    public static EmergencyEventServiceDto generateEmergencyEventServiceDto(SearchGoApplication application, EmergencyEventServiceDto dto) {
        EmergencyEventRepository repository = getEmergencyEventRepository(application);
        EmergencyEventServiceDto serviceEvent = repository.createObject(new HashMap<String, Object>());
        serviceEvent.setId(dto.getId());
        serviceEvent.setAddress(dto.getAddress());
        serviceEvent.setName(dto.getName());
        //serviceEvent.setDescription(dto.getDescription());
        serviceEvent.setLastSeen(dto.getLastSeen());
        serviceEvent.setCreatedOn(dto.getCreatedOn());
        serviceEvent.setCategory(dto.getCategory());
        return serviceEvent;
    }

    public static EmergencyEventServiceDto generateEmergencyEventServiceDto(SearchGoApplication application) {
        EmergencyEventRepository repository = getEmergencyEventRepository(application);
        EmergencyEventServiceDto dto = repository.createObject(new HashMap<String, Object>());
        String id = UUID.randomUUID().toString();
        dto.setId(id);
        dto.setCreatedOn(new Date());
        return dto;
    }

    public static EmergencyEventRepository getEmergencyEventRepository(SearchGoApplication application) {
        RestAdapter adapter = application.getLoopBackAdapter();
        return adapter.createRepository(EmergencyEventRepository.class);
    }
}
