package com.searchgo.backgroundServices;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tamar.twena on 11/29/2017.
 */

public class BackgroundServiceScheduler {

    private static boolean saveEventServiceRunning = false;


    public static void scheduleSaveEmergencyEventService(Activity activity) {

        saveEventServiceRunning = true;

        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        Intent saveEventReceiver = new Intent(activity.getApplicationContext(), SaveEmergencyEventReceiver.class);
        final PendingIntent saveEventIntent = PendingIntent.getBroadcast(activity, SaveEmergencyEventReceiver.REQUEST_CODE,
                saveEventReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                3*60*1000, saveEventIntent);
    }

    public static boolean isSaveEventServiceRunning() {
        return saveEventServiceRunning;
    }
}
