package com.example.michael.wallpaperchanger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Map;

// TODO: create preference to show 24 hour time or am/pm time.
// TODO: handle landscape mode (or force portrait?)

public class MainActivity extends AppCompatActivity implements ScheduleRecyclerViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    public static final String ALARM_PREFS = "com.example.michael.wallpaperchanger.alarmPrefs";
    FloatingActionButton fab = null;

    Button clear = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActivityLayout();
        showScheduleList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean shmuck = sp.getBoolean("pref_shmuck", true);
        TextView tv = (TextView) findViewById(R.id.settingsText);
        if (tv != null)
            tv.setText("Shmuck: " + shmuck);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showScheduleList() {
        ScheduleRecyclerViewFragment fragment = ScheduleRecyclerViewFragment.newInstance("some param", "some other param");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    public void setupActivityLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        clear = (Button)findViewById(R.id.temp_clear_button);

        if (clear != null)
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearList();
                }
            });

        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri placeholderImageUri = Utility.idToUri("R.drawable.placeholder_wallpaper_full");
                    openScheduleDetails(Utility.getCurrentTime(false), placeholderImageUri, false);
                }
            });
    }

    public void clearList() {
        SharedPreferences prefs = getSharedPreferences(ALARM_PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        showScheduleList();
        Toast.makeText(this, "Schedules cleared", Toast.LENGTH_SHORT).show();
    }

    // Called when clicking on an existing schedule
    public void onScheduleClicked(String time, Uri imageUri) {
        openScheduleDetails(time, imageUri, true);
    }

    public void openScheduleDetails(String time, Uri imageUri, boolean isEditing) {
        ScheduleDetailsFragment fragment = ScheduleDetailsFragment.newInstance(time, imageUri.toString(), isEditing);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
        fab.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
    }

    public void setTime(String pickerTime, boolean in24HourFormat) {
        ScheduleDetailsFragment fragment = (ScheduleDetailsFragment)getSupportFragmentManager().
                                            findFragmentById(R.id.main_container);
        fragment.setTime(Utility.to12HourTime(pickerTime));
    }
}
