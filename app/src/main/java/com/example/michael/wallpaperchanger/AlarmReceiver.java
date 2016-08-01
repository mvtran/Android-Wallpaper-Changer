package com.example.michael.wallpaperchanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "ALARM RECEIVER";
    private static int counter = 0;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm " + counter, Toast.LENGTH_LONG).show();
        counter++;
    }

}
