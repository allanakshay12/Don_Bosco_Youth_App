package com.allanakshay.donboscoyouth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Allan Akshay on 24-08-2017.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent intent1 = new Intent(context, NotificationServiceDBY.class);
            context.startService(intent1);
        }
    }
}
