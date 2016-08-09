package com.example.michael.wallpaperchanger;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



public class TimePickerPreference extends DialogPreference {
    private static final String DEFAULT_TIME = "0:00";

    private int prevHour = 0;
    private int prevMinute = 0;
    private com.example.michael.wallpaperchanger.MyTimePicker picker = null;

    public TimePickerPreference(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);

        setLayoutResource(R.layout.time_picker_preference);
        setDialogLayoutResource(R.layout.time_picker_dialog);
        setPositiveButtonText(R.string.ok);
        setNegativeButtonText(R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected View onCreateDialogView() {
        picker = new MyTimePicker(getContext());
        return picker;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        picker.setHour(prevHour);
        picker.setMinute(prevMinute);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state. DEFAULT_* is an argument just in case getPersistedInt() fails
            String time = this.getPersistedString(DEFAULT_TIME);
            String[] hourMin = time.split(":");
            prevHour = Integer.parseInt(hourMin[0]);
            prevMinute = Integer.parseInt(hourMin[1]);
        } else {
            // Set default state from XML attribute
            persistString((String)defaultValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int idx) {
        return a.getString(idx);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            prevHour = picker.getHour();
            prevMinute = picker.getMinute();
            persistString(timeToString(prevHour, prevMinute));
            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        }
    }

    private String timeToString(int hour, int min) {
        return Integer.toString(hour) + ":" + Integer.toString(min);
    }
}
