package com.searchgo.backgroundServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tamar.twena on 12/19/2017.
 */

public class GetMyEventsEventReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, GetMyEventsBackgroundService.class);
        context.startService(i);

    }
}
