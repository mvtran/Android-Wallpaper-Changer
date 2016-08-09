package com.example.michael.wallpaperchanger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utility {
    private Utility() {}

    public static String get24HourTime() {
        return new SimpleDateFormat("HH:mm", Locale.US).format(new Date());
    }

    public static String getTime() {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
        return df.format(new Date());
    }
}
