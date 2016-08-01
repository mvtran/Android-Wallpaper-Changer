package com.example.michael.wallpaperchanger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ScheduleRecyclerViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "WALLPAPERCHANGER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActivityLayout();
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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean shmuck = sp.getBoolean("pref_shmuck", true);
        TextView tv = (TextView) findViewById(R.id.settingsText);
        tv.setText("Shmuck: " + shmuck);
    }

    public void addWallpaperSchedule(View view) {
        Intent intent = new Intent(this, AddWallpaperScheduleActivity.class);
        startActivity(intent);
    }

    public void setupActivityLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWallpaperSchedule(view);
            }
        });
    }

    public void onScheduleClicked(int position) {
        Log.d(TAG, "inside onScheduleClicked in MainActivity");
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
}
