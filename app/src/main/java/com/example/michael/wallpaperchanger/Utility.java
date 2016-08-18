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

    /*
    This is what is used to store the keys for each schedule. They are distinguished
    simply by their time in the format "schedule-[time]". Since no two schedules
    can have the same time, this guarantees all keys are unique.
     */
    public static String timeAsPrefKey(String time) {
        return "schedule-"+time;
    }

    public static String getCurrentTime(boolean in24HourFormat) {
        if (in24HourFormat) {
            return new SimpleDateFormat("HH:mm", Locale.US).format(new Date());
        } else {
            DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
            return df.format(new Date());
        }
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
     * @param time: A string representing a time in the format "xx:xx [AM|PM]"
     */
    public static int getHourFrom12HourTime(String time) {
        String[] split = time.split(":");
        String[] split2 = split[1].split(" ");
        int hour = Integer.parseInt(split[0]);

        if (split2[1].equalsIgnoreCase("PM"))
            return hour + 12;
        if (hour == 12)
            return 0;
        return hour;
    }

    /*
     * @param time: A string representing a time in the format "xx:xx [AM|PM]"
     */
    public static int getMinuteFrom12HourTime(String time) {
        String[] split = time.split(":");
        String[] split2 = split[1].split(" ");
        return Integer.parseInt(split2[0]);
    }

    /*
     * @param imageIdString: A string representing an Android drawable resource, e.g. R.drawable.picture
     */
    public static Uri idToUri(String imageIdString) {
        String fileName = imageIdString.split("\\.")[2];
        return Uri.parse("android.resource://com.example.michael.wallpaperchanger/drawable/"+fileName);
    }
}
