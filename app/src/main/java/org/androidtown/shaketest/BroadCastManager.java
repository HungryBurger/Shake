package org.androidtown.shaketest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class BroadCastManager  {
    private static BroadCastManager bcmManager;
    private static BroadcastReceiver broadcastReceiver;

    private final String SCREEN_ON = "android.intent.action.USER_PRESENT";
    private final String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

    private BroadCastManager (Context context) {
        registerMyReceiver(context);
    }

    public static BroadCastManager getInstance (Context context) {
        if (bcmManager == null) {
            bcmManager = new BroadCastManager(context);
        } return bcmManager;
    }

    public void registerMyReceiver (Context context) {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SCREEN_ON);
        intentFilter.addAction(SCREEN_OFF);

        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent serviceIntent = new Intent(context, ShakeService.class);
                if (intent.getAction().equals(SCREEN_ON)) {
                    Log.d("MyReceiver", "OnScreen ");
                    context.startService(serviceIntent);
                } else if (intent.getAction().equals(SCREEN_OFF)) {
                    Log.e("MyReceiver", "OffScreen");
                    context.stopService(serviceIntent);
                }
            }
        };
        context.registerReceiver(this.broadcastReceiver, intentFilter);
    }

    public static void unregisterMyReceiver (Context context) {
        Log.d("MyReceiver", "동적 해제 최종");
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver);
        } broadcastReceiver = null;
    }
}
