package com.searchgo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.searchgo.SearchEventActivity;
import com.searchgo.constants.ApplicationConstants;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.tasks.SetEventImageTask;

import java.io.File;

import static com.searchgo.constants.ApplicationConstants.EVENT_DTO;

/**
 * Created by tamar.twena on 12/11/2017.
 */

public class ActivityUtils {

    public static SharedPreferences.Editor getSharedPreferencesEditor(Activity activity) {
        SharedPreferences.Editor editor = getSharedPreferences(activity).edit();
        return editor;
    }

    public static SharedPreferences getSharedPreferences(Activity activity) {
        return activity.getApplicationContext().getSharedPreferences(ApplicationConstants.USER_DETAILS_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void saveEventPicture(String eventId, Bitmap eventImage, Activity context, String token) {
        if (eventImage != null) {
            Object[] eventImageParams = new Object[]{ token,
                    context.getFilesDir().getPath().toString() + File.separator + eventId};
            new SetEventImageTask( eventId, eventImage).execute(eventImageParams);
        }
    }
}
