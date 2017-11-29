package com.searchgo.dto.activity;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by tamar.twena on 11/26/2017.
 */

public class EmergencyEventActivityDtoFactory {


    @NonNull
    public static EmergencyEventActivityDto generateEmergencyEvent(String address) {
        EmergencyEventActivityDto event = new EmergencyEventActivityDto();
        String id = UUID.randomUUID().toString();
        event.setId(id);
        event.setAddress(address);
        return event;
    }

}
