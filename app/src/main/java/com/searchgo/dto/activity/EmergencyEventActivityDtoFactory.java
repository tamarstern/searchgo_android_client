package com.searchgo.dto.activity;

import android.support.annotation.NonNull;

/**
 * Created by tamar.twena on 11/26/2017.
 */

public class EmergencyEventActivityDtoFactory {


    @NonNull
    public static EmergencyEventActivityDto generateEmergencyEvent(String address) {
        EmergencyEventActivityDto event = new EmergencyEventActivityDto();
        event.setAddress(address);
        return event;
    }

}
