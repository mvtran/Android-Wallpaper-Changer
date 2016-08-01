package com.example.michael.wallpaperchanger;

import android.content.Context;
import android.os.Build;
import android.widget.TimePicker;

/*
    This class is the same as the normal TimePicker except it handles
     the whole deprecated method situation. It helps with refactoring/
     maintenance to keep it in this place. Therefore, only use MyTimePicker
     rather than TimePicker at all times.

     Suggestion found at http://stackoverflow.com/questions/33393137/button-settextappearance-is-deprecated
 */
public class MyTimePicker extends TimePicker {
    public MyTimePicker(Context ctx) {
        super(ctx);
    }

    public int getHour() {
        if (Build.VERSION.SDK_INT < 23)
            return super.getCurrentHour();
        else
            return super.getHour();
    }

    public int getMinute() {
        if (Build.VERSION.SDK_INT < 23)
            return super.getCurrentMinute();
        else
            return super.getMinute();
    }

    public void setHour(int hour) {
        if (Build.VERSION.SDK_INT < 23)
            super.setCurrentHour(hour);
        else
            super.setHour(hour);
    }

    public void setMinute(int minute) {
        if (Build.VERSION.SDK_INT < 23)
            super.setCurrentMinute(minute);
        else
            super.setMinute(minute);
    }
}
