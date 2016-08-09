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

public class MainActivity extends AppCompatActivity implements ScheduleRecyclerViewFragment.OnFragmentInteractionListener,
                                                                ScheduleDetailsFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean shmuck = sp.getBoolean("pref_shmuck", true);
        TextView tv = (TextView) findViewById(R.id.settingsText);
        if (tv != null)
            tv.setText("Shmuck: " + shmuck);
    }

    public void setupActivityLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openScheduleDetails(Utility.get24HourTime(), R.drawable.placeholder_wallpaper_full);
                }
            });
    }

    public void onScheduleClicked(String time, int imageURL) {
        openScheduleDetails(time, imageURL);
        //Toast.makeText(this, "Text: " + time, Toast.LENGTH_SHORT).show();
    }

    public void onFragmentClicked() {
        // TODO: what do?!
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: request code: big number, result code: -1. uh oh.
        if (requestCode == ScheduleRecyclerViewFragment.PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "ya done fucked up A-A-ron", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "preezent", Toast.LENGTH_SHORT).show();
                // get ScheduleDetailsFragment so you can add image?
                Uri selectedImage = data.getData();
                String fileString = selectedImage.getPath();
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap thumbnail = BitmapFactory.decodeFile(fileString, options);
                // TODO: pass this to main activity so ScheduleDetailsFragment can access it?
            }
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
}
