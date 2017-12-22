package com.searchgo.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.searchgo.application.SearchGoApplication;
import com.searchgo.constants.ApplicationConstants;
import com.strongloop.android.loopback.RestAdapter;

import org.json.JSONArray;

/**
 * Created by tamar.twena on 12/22/2017.
 */

public class LoginUtils {

    public static void StoreAccessToken(String accessToken, Activity activity) {
        SharedPreferences.Editor editor = ActivityUtils.getSharedPreferencesEditor(activity);
        editor.putString(ApplicationConstants.ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public static String getAccessToken(Activity activity) {
        SharedPreferences appSharedPrefs = ActivityUtils.getSharedPreferences(activity);
        return appSharedPrefs.getString(ApplicationConstants.ACCESS_TOKEN, null);

    }

    public static void StoreUserId(String result, Activity activity) {
        SharedPreferences.Editor editor = ActivityUtils.getSharedPreferencesEditor(activity);
        String json = new JSONArray().put(result).toString();
        editor.putString(ApplicationConstants.USER_ID, json);
        editor.commit();
    }

    public static void initAccessTokenForRestCalls(String accessToken, Activity activity) {

        SearchGoApplication app = (SearchGoApplication) activity.getApplication();
        RestAdapter adapter = app.getLoopBackAdapter();
        adapter.setAccessToken(accessToken);

    }

}
