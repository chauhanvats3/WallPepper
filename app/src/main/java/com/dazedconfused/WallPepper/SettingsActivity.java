package com.dazedconfused.WallPepper;

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
import android.widget.RadioGroup;
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

import static com.dazedconfused.WallPepper.MyRuntimePreferences.IS_JOB_GOING;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.NO_OF_JOBS_DONE;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_DOWNLOAD_CHECKED_RDBTN;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_DURATION_SPINNER_POSITION;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_FEATURED;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_INTERVALS;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_ORIENTATION_CHECKED_RDBTN;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_SEARCH_QUERY;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_SWITCH_STATUS;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_WALLPAPER_CHECKED_RDBTN;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.SHARED_PREFS;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private static WeakReference<SettingsActivity> activityWeakReference;
    private static int interval;
    private static int durationSpinnerPosition;
    private final int JOB_ID = 6969;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Switch switchButton;
    private Spinner durationSpinner;
    private CheckBox featuredPhotos;
    private EditText searchTags;
    private TextView durationTxtView;
    private RadioGroup rdgrpWallpaperQuality;
    private RadioGroup rdgrpOrientation;
    private RadioGroup rdgrpDownloadQuality;
    private int first_spinner_duration = 0, first_spinner_duration_counter = 0;


    public static WeakReference<SettingsActivity> getWeakReference() {

        return activityWeakReference;
    }

    public void saveData() {
        Log.d(TAG,"saveDATA<----------------");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        durationSpinnerPosition = durationSpinner.getSelectedItemPosition();
        String stringArray[] = getResources().getStringArray(R.array.pref_scheduler_values);
        interval = Integer.parseInt(stringArray[durationSpinnerPosition]);
        editor.putInt(PREF_INTERVALS, interval);
        editor.putBoolean(PREF_SWITCH_STATUS, switchButton.isChecked());
        editor.putInt(PREF_DURATION_SPINNER_POSITION, durationSpinnerPosition);
        editor.putBoolean(PREF_FEATURED, featuredPhotos.isChecked());
        editor.putString(PREF_SEARCH_QUERY, searchTags.getText().toString());
        editor.putInt(PREF_WALLPAPER_CHECKED_RDBTN, rdgrpWallpaperQuality.getCheckedRadioButtonId());
        editor.putInt(PREF_ORIENTATION_CHECKED_RDBTN, rdgrpOrientation.getCheckedRadioButtonId());
        editor.putInt(PREF_DOWNLOAD_CHECKED_RDBTN, rdgrpDownloadQuality.getCheckedRadioButtonId());
        editor.apply();
    }

    public void loadData() {
        Log.d(TAG,"loadData<--------------------");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        interval = sharedPreferences.getInt(PREF_INTERVALS, 1);
        switchButton.setChecked(sharedPreferences.getBoolean(PREF_SWITCH_STATUS, false));
        durationSpinnerPosition = sharedPreferences.getInt(PREF_DURATION_SPINNER_POSITION, 1);
        featuredPhotos.setChecked(sharedPreferences.getBoolean(PREF_FEATURED, true));
        searchTags.setText(sharedPreferences.getString(PREF_SEARCH_QUERY, ""));
        rdgrpWallpaperQuality.check(sharedPreferences.getInt(PREF_WALLPAPER_CHECKED_RDBTN, R.id.rdbtnWlprRegular));
        rdgrpOrientation.check(sharedPreferences.getInt(PREF_ORIENTATION_CHECKED_RDBTN, R.id.rdbtnPortrait));
        rdgrpDownloadQuality.check(sharedPreferences.getInt(PREF_DOWNLOAD_CHECKED_RDBTN, R.id.rdbtnDwnldRegular));
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
        setContentView(R.layout.activity_settings);
        activityWeakReference = new WeakReference<>(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.scheduler_toolbar);
        switchButton = findViewById(R.id.swichScheduler);
        durationSpinner = findViewById(R.id.spinnerInterval);
        featuredPhotos = findViewById(R.id.chkbxFeatured);
        searchTags = findViewById(R.id.txtSearchQuery);
        durationTxtView = findViewById(R.id.txtInterval);
        rdgrpWallpaperQuality = findViewById(R.id.rdgrpWlpr);
        rdgrpDownloadQuality = findViewById(R.id.rdgrpDwnload);
        rdgrpOrientation = findViewById(R.id.rdgrpOrientation);
        hideStatusBar();
        loadData();
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        MyNavItemListener navItemListener = new MyNavItemListener(activityWeakReference);
        navigationView.setNavigationItemSelectedListener(navItemListener.navigationItemSelectedListener);
        MySettingsGestureResponses mySettingsGestureResponses = new MySettingsGestureResponses();
        drawerLayout.setOnTouchListener(mySettingsGestureResponses.schedulerGestures);
        ArrayAdapter<CharSequence> durationArrayAdapter = ArrayAdapter.createFromResource(this, R.array.pref_scheduler_title, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> orientationArrayAdapter = ArrayAdapter.createFromResource(this, R.array.pref_orientation_title, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> qualityArrayAdapter = ArrayAdapter.createFromResource(this, R.array.pref_image_quality, android.R.layout.simple_spinner_item);
        durationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orientationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationArrayAdapter);
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
                    Toast.makeText(SettingsActivity.this, "Interval Set : " + interval + " Hour", Toast.LENGTH_SHORT).show();
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


        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SettingsActivity.this.scheduleJob();
                    durationTxtView.setEnabled(true);
                    durationSpinner.setEnabled(true);
                } else {
                    SettingsActivity.this.cancelJob();
                    durationTxtView.setEnabled(false);
                    durationSpinner.setEnabled(false);
                }
                SettingsActivity.this.saveData();
            }
        });
        rdgrpWallpaperQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                SettingsActivity.this.saveData();
                Toast.makeText(SettingsActivity.this, "Wallpaper Quality Changed!", Toast.LENGTH_SHORT).show();
            }
        });
        rdgrpDownloadQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                saveData();
                Toast.makeText(SettingsActivity.this, "Download Quality Changed!", Toast.LENGTH_SHORT).show();
            }
        });
        rdgrpOrientation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                saveData();
                Toast.makeText(SettingsActivity.this, "Orientation Changed!", Toast.LENGTH_SHORT).show();
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

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_JOB_GOING, true);
        editor.apply();
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
            Log.d(TAG, "Job is Scheduled<------------------------------");
        } else
            Log.d(TAG,"scheduling failed "+res);
            Toast.makeText(this, "Scheduling Failed!", Toast.LENGTH_SHORT).show();
    }

    public void cancelJob() {

        Log.d(TAG, "cancelJob: <-----------------");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_JOB_GOING, false);
        editor.putInt(NO_OF_JOBS_DONE, 0);
        editor.apply();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
        Toast.makeText(this, "Job Cancelled", Toast.LENGTH_SHORT).show();
    }

    public void isGoingOn(View view) {

        boolean res = isJobServiceOn(this);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int count = sharedPreferences.getInt(NO_OF_JOBS_DONE, 0);
        Toast.makeText(this, "Job Status : " + res + " Times Done : " + count, Toast.LENGTH_SHORT).show();
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
