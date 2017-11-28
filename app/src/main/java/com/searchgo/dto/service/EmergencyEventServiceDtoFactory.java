package com.searchgo.dto.service;

import com.searchgo.dto.activity.EmergencyEventActivityDto;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class EmergencyEventServiceDtoFactory {

    public static EmergencyEventServiceDto generateEmergencyEventService(RestAdapter adapter, EmergencyEventActivityDto dto) {

        EmergencyEventServiceDto.EmergencyEventRepository repository = adapter.createRepository(EmergencyEventServiceDto.EmergencyEventRepository.class);

        EmergencyEventServiceDto serviceEvent = repository.createObject(new HashMap<String,Object>());
        serviceEvent.setAddress(dto.getAddress());
        serviceEvent.setName(dto.getName());
        //serviceEvent.setDescription(dto.getDescription());
        serviceEvent.setLastSeen(dto.getLastSeen());
        serviceEvent.setCreatedOn(dto.getCreatedOn());
        serviceEvent.setCategory(dto.getCategory());
        return serviceEvent;

    }
}
