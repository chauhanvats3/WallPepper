package com.dazedconfused.WallPix;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import static com.dazedconfused.WallPix.MyRuntimePreferences.FEATURED;
import static com.dazedconfused.WallPix.MyRuntimePreferences.INTERVALS;
import static com.dazedconfused.WallPix.MyRuntimePreferences.ORIENTATION;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PREF_DURATION_SPINNER_POSITION;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PREF_ORIENTATION_SPINNER_POSITION;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PREF_QUALITY_SPINNER_POSITION;
import static com.dazedconfused.WallPix.MyRuntimePreferences.QUALITY;
import static com.dazedconfused.WallPix.MyRuntimePreferences.SEARCH_QUERY;
import static com.dazedconfused.WallPix.MyRuntimePreferences.SHARED_PREFS;
import static com.dazedconfused.WallPix.MyRuntimePreferences.SWITCH_STATUS;

public class SchedulerActivity extends AppCompatActivity {

    private static final String TAG = "SchedulerActivity";
    private static WeakReference<SchedulerActivity> activityWeakReference;
    private static int interval;
    private static int orientation;
    private static int quality;
    private static int durationSpinnerPosition;
    private static int orientationSpinnerPosition;
    private static int qualitySpinnerPosition;
    private final int JOB_ID = 6969;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Switch switchButton;
    private Spinner durationSpinner;
    private Spinner orientationSpinner;
    private Spinner qualitySpinner;
    private CheckBox featuredPhotos;
    private EditText searchTags;
    private TextView durationTxtView;
    private int first_spinner_duration = 0, first_spinner_duration_counter = 0;
    private int first_spinner_orientation = 0, first_spinner_orientation_counter = 0;
    private int first_spinner_quality = 0, first_spinner_quality_counter = 0;


    public static WeakReference<SchedulerActivity> getWeakReference() {

        return activityWeakReference;
    }

    public void saveData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        durationSpinnerPosition = durationSpinner.getSelectedItemPosition();
        orientationSpinnerPosition = orientationSpinner.getSelectedItemPosition();
        qualitySpinnerPosition = qualitySpinner.getSelectedItemPosition();
        String stringArray[] = getResources().getStringArray(R.array.pref_scheduler_values);
        interval = Integer.parseInt(stringArray[durationSpinnerPosition]);
        stringArray = getResources().getStringArray(R.array.pref_orientation_values);
        orientation = Integer.parseInt(stringArray[orientationSpinnerPosition]);
        stringArray = getResources().getStringArray(R.array.pref_image_quality_values);
        quality = Integer.parseInt(stringArray[qualitySpinnerPosition]);
        editor.putInt(INTERVALS, interval);
        editor.putInt(ORIENTATION,orientation);
        editor.putInt(QUALITY,quality);
        editor.putBoolean(SWITCH_STATUS, switchButton.isChecked());
        editor.putInt(PREF_DURATION_SPINNER_POSITION, durationSpinnerPosition);
        editor.putInt(PREF_ORIENTATION_SPINNER_POSITION, orientationSpinnerPosition);
        editor.putInt(PREF_QUALITY_SPINNER_POSITION, qualitySpinnerPosition);
        editor.putBoolean(FEATURED,featuredPhotos.isChecked());
        editor.putString(SEARCH_QUERY,searchTags.getText().toString());
        editor.apply();
    }

    public void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        interval = sharedPreferences.getInt(INTERVALS, 1);
        switchButton.setChecked(sharedPreferences.getBoolean(SWITCH_STATUS, false));
        durationSpinnerPosition = sharedPreferences.getInt(PREF_DURATION_SPINNER_POSITION, 1);
        orientationSpinnerPosition = sharedPreferences.getInt(PREF_ORIENTATION_SPINNER_POSITION, 1);
        qualitySpinnerPosition = sharedPreferences.getInt(PREF_QUALITY_SPINNER_POSITION, 1);
        quality = sharedPreferences.getInt(QUALITY, 1);
        orientation = sharedPreferences.getInt(ORIENTATION, 1);
        featuredPhotos.setChecked(sharedPreferences.getBoolean(FEATURED,true));
        searchTags.setText(sharedPreferences.getString(SEARCH_QUERY,""));
    }


    protected void hideStatusBar() {

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public DrawerLayout getDrawerLayout() {

        return drawerLayout;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        activityWeakReference = new WeakReference<>(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.scheduler_toolbar);
        switchButton = findViewById(R.id.swichScheduler);
        durationSpinner = findViewById(R.id.spinnerInterval);
        orientationSpinner = findViewById(R.id.spinnerOrientation);
        qualitySpinner = findViewById(R.id.spinnerImageQuality);
        featuredPhotos = findViewById(R.id.chkbxFeatured);
        searchTags = findViewById(R.id.txtSearchQuery);
        durationTxtView = findViewById(R.id.txtInterval);
        hideStatusBar();
        loadData();
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        MyNavItemListener navItemListener = new MyNavItemListener(activityWeakReference);
        navigationView.setNavigationItemSelectedListener(navItemListener.navigationItemSelectedListener);
        MySchedulerGestureResponses mySchedulerGestureResponses = new MySchedulerGestureResponses();
        drawerLayout.setOnTouchListener(mySchedulerGestureResponses.schedulerGestures);
        ArrayAdapter<CharSequence> durationArrayAdapter = ArrayAdapter.createFromResource(this, R.array.pref_scheduler_title, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> orientationArrayAdapter = ArrayAdapter.createFromResource(this, R.array.pref_orientation_title, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> qualityArrayAdapter = ArrayAdapter.createFromResource(this, R.array.pref_image_quality, android.R.layout.simple_spinner_item);
        durationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orientationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationArrayAdapter);
        qualitySpinner.setAdapter(qualityArrayAdapter);
        orientationSpinner.setAdapter(orientationArrayAdapter);
        if (switchButton.isChecked()) {
            durationTxtView.setEnabled(true);
            durationSpinner.setEnabled(true);
        } else {
            durationTxtView.setEnabled(false);
            durationSpinner.setEnabled(false);
        }
        durationSpinner.setSelection(durationSpinnerPosition);
        first_spinner_duration = 1;
        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (first_spinner_duration_counter < first_spinner_duration) {
                    first_spinner_duration_counter++;
                } else {
                    String stringArray[] = getResources().getStringArray(R.array.pref_scheduler_values);
                    interval = Integer.parseInt(stringArray[position]);
                    Toast.makeText(SchedulerActivity.this, "Interval Set : " + interval + " Hour", Toast.LENGTH_SHORT).show();
                    saveData();
                    cancelJob();
                    scheduleJob();
                    hideStatusBar();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                hideStatusBar();
            }
        });

        //ForOrientationSpinner
        orientationSpinner.setSelection(orientationSpinnerPosition);
        first_spinner_orientation = 1;
        orientationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (first_spinner_orientation_counter < first_spinner_orientation) {
                    first_spinner_orientation_counter++;
                } else {
                    String stringArray[] = getResources().getStringArray(R.array.pref_orientation_values);
                    orientation = Integer.parseInt(stringArray[position]);
                    Toast.makeText(SchedulerActivity.this, "Orientation Set : " + orientation, Toast.LENGTH_SHORT).show();
                    saveData();
                    hideStatusBar();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                hideStatusBar();
            }
        });
        //QualitySpinner
        qualitySpinner.setSelection(qualitySpinnerPosition);
        first_spinner_quality = 1;
        qualitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (first_spinner_quality_counter < first_spinner_quality) {
                    first_spinner_quality_counter++;
                } else {
                    String stringArray[] = getResources().getStringArray(R.array.pref_image_quality_values);
                    quality = Integer.parseInt(stringArray[position]);
                    Toast.makeText(SchedulerActivity.this, "Quality Set : " + quality, Toast.LENGTH_SHORT).show();
                    saveData();
                    hideStatusBar();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideStatusBar();
            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    scheduleJob();
                    durationTxtView.setEnabled(true);
                    durationSpinner.setEnabled(true);
                } else {
                    cancelJob();
                    durationTxtView.setEnabled(false);
                    durationSpinner.setEnabled(false);
                }
                saveData();
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            startActivity(new Intent(this, MainActivity.class));
            super.onBackPressed();
        }
    }

    public void scheduleJob() {

        ComponentName componentName = new ComponentName(this, MyScheduledJob.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setPersisted(true)
                .setPeriodic(interval * 60 * 60 * 1000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int res = jobScheduler.schedule(jobInfo);
        if (res == JobScheduler.RESULT_SUCCESS) {
            Toast.makeText(this, "Scheduled for " + interval + " Hour intervals", Toast.LENGTH_SHORT).show();
            Log.wtf(TAG, "Job is Scheduled<------------------------------");
        } else
            Toast.makeText(this, "Scheduling Failed!", Toast.LENGTH_SHORT).show();
    }

    public void cancelJob() {

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
        Toast.makeText(this, "Job Cancelled", Toast.LENGTH_SHORT).show();
    }

    public void isGoingOn(View view) {

        boolean res = isJobServiceOn(this);
        Toast.makeText(this, "" + res, Toast.LENGTH_SHORT).show();
    }

    public boolean isJobServiceOn(Context context) {

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        boolean hasBeenScheduled = false;
        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JOB_ID) {
                hasBeenScheduled = true;
                break;
            }
        }
        return hasBeenScheduled;
    }

    @Override
    protected void onResume() {

        super.onResume();
        hideStatusBar();
    }

    @Override
    protected void onDestroy() {

        saveData();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            hideStatusBar();
    }

}
