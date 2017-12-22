package com.searchgo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.searchgo.SearchEventActivity;
import com.searchgo.constants.ApplicationConstants;
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

    public static SharedPreferences.Editor getSharedPreferencesEditor(Activity activity) {
        SharedPreferences.Editor editor = getSharedPreferences(activity).edit();
        return editor;
    }

    public static SharedPreferences getSharedPreferences(Activity activity) {
        return activity.getApplicationContext().getSharedPreferences(ApplicationConstants.USER_DETAILS_PREFERENCES, Context.MODE_PRIVATE);
    }
}
