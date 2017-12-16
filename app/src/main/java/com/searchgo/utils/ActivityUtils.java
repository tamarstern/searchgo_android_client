package com.searchgo.utils;

import android.content.Context;
import android.content.Intent;

import com.searchgo.SearchEventActivity;
import com.searchgo.dto.service.EmergencyEventServiceDto;

import static com.searchgo.constants.ApplicationConstants.EVENT_DTO;

/**
 * Created by tamar.twena on 12/11/2017.
 */

public class ActivityUtils {

    public static void startEventPageActivity(Context activity , EmergencyEventServiceDto dto){
        Intent intent = new Intent(activity, SearchEventActivity.class);
        intent.putExtra(EVENT_DTO, dto);
        activity.startActivity(intent);
    }
}
