package com.searchgo.backgroundServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tamar.twena on 11/29/2017.
 */

public class SaveEmergencyEventReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SaveEventBackgroundService.class);
        context.startService(i);
    }
}
