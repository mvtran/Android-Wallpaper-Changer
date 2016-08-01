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
    private MyTimePicker picker = null;

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

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent,
            // use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        myState.value = prevHour+":"+prevMinute;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        picker.setHour(prevHour);
        picker.setMinute(prevMinute);
    }

    private String timeToString(int hour, int min) {
        return Integer.toString(hour) + ":" + Integer.toString(min);
    }

    private static class SavedState extends BaseSavedState {
        // Member that holds the setting's value
        // Change this data type to match the type saved by your Preference
        String value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the current preference's value
            value = source.readString();  // Change this to read the appropriate data type
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeString(value);  // Change this to write the appropriate data type
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
