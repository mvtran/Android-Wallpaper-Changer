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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Map;

// TODO: create preference to show 24 hour time or am/pm time. after all, the Utility class is made for both.

public class MainActivity extends AppCompatActivity implements ScheduleRecyclerViewFragment.OnFragmentInteractionListener,
                                                                ScheduleDetailsFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    public static final String ALARM_PREFS = "com.example.michael.wallpaperchanger.alarmPrefs";
    FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActivityLayout();
        //TODO: the params passed here will probably be data on the complete schedule of wallpapers. gotta find somewhere to store persistent data...
        ScheduleRecyclerViewFragment fragment = ScheduleRecyclerViewFragment.newInstance("some param", "some other param");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, fragment)
                    .commit();
        }
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
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(ALARM_PREFS, 0);
        Map<String,?> all = prefs.getAll();
        Iterator it = all.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Log.d(TAG, pair.getKey() + ", " + pair.getValue());
            it.remove();
        }
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

    public void setupActivityLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openScheduleDetails(Utility.get12HourTime(), R.drawable.placeholder_wallpaper_full);
                }
            });
    }

    public void onScheduleClicked(String time, int imageURL) {
        openScheduleDetails(time, imageURL);
        //Toast.makeText(this, "Text: " + time, Toast.LENGTH_SHORT).show();
    }

    public void onFragmentClicked() {
        Toast.makeText(this, "onFragmentClicked()", Toast.LENGTH_SHORT).show();
    }

    public void openScheduleDetails(String time, int imageURL) {
        ScheduleDetailsFragment fragment = ScheduleDetailsFragment.newInstance(time, imageURL);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
        fab.setVisibility(View.GONE);
    }

    public void setTime(String pickerTime, boolean in24HourFormat) {
        ScheduleDetailsFragment fragment = (ScheduleDetailsFragment)getSupportFragmentManager().
                                            findFragmentById(R.id.main_container);
        fragment.setTime(Utility.to12HourTime(pickerTime));
    }
}
