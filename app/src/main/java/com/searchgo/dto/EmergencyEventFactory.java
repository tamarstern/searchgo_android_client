package com.searchgo.dto;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by tamar.twena on 11/26/2017.
 */

public class EmergencyEventFactory {


    @NonNull
    public static EmergencyEvent generateEmergencyEvent(String address) {
        EmergencyEvent event = new EmergencyEvent();
        String uuid = UUID.randomUUID().toString();
        event.setId(uuid);
        event.setAddress(address);
        return event;
    }

}
