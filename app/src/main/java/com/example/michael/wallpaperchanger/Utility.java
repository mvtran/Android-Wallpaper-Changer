package com.example.michael.wallpaperchanger;

import android.content.res.Resources;
import android.net.Uri;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utility {
    private Utility() {}

    public static String get24HourTime() {
        return new SimpleDateFormat("HH:mm", Locale.US).format(new Date());
    }

    public static String get12HourTime() {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
        return df.format(new Date());
    }

    public static String to12HourTime(String time) {
        String[] hourAndMin = time.split(":");
        int hour = Integer.parseInt(hourAndMin[0]),
            min  = Integer.parseInt(hourAndMin[1]);
        String amOrPm = "AM", minStr = ""+min;

        if (hour > 11) {
            if (hour > 12)
                hour -= 12;
            amOrPm = "PM";
        }
        if (hour == 0)
            hour = 12;
        if (min < 10)
            minStr = "" + 0 + min;

        return "" + hour + ":" + minStr + " " + amOrPm;
    }

    public static String to24HourTime(String time) {
        return time;
    }

    /*
     * @param imageIdString: A string representing an Android drawable resource, e.g. R.drawable.picture
     */
    public static Uri idToUri(String imageIdString) {
        String fileName = imageIdString.split("\\.")[2];
        return Uri.parse("android.resource://com.example.michael.wallpaperchanger/drawable/"+fileName);
    }
}
