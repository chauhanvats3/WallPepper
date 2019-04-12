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
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class SchedulerActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH_STATUS = "switchStatus";
    public static final String INTERVALS = "intervals";
    public static final String POSITION = "position";
    private static final String TAG = "SchedulerActivity";
    private static WeakReference<SchedulerActivity> activityWeakReference;
    private static int interval;
    private static int selectedPosition;
    private final int JOB_ID = 6969;
    private int first_spinner = 0, first_spinner_counter = 0;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Switch switchButton;
    private Spinner spinner;

    public static WeakReference<SchedulerActivity> getWeakReference() {
        return activityWeakReference;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String stringArray[] = getResources().getStringArray(R.array.pref_scheduler_values);
        selectedPosition = spinner.getSelectedItemPosition();
        interval = Integer.parseInt(stringArray[selectedPosition]);
        editor.putInt(INTERVALS, interval);
        editor.putBoolean(SWITCH_STATUS, switchButton.isChecked());
        editor.putInt(POSITION, selectedPosition);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        interval = sharedPreferences.getInt(INTERVALS, 1);
        switchButton.setChecked(sharedPreferences.getBoolean(SWITCH_STATUS, false));
        selectedPosition = sharedPreferences.getInt(POSITION, 1);
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
        spinner = findViewById(R.id.spinnerInterval);
        hideStatusBar();
        loadData();
        toolbar.setTitle("Automatic Wallpaper Change");
        setSupportActionBar(toolbar);
        MyNavItemListener navItemListener = new MyNavItemListener(activityWeakReference);
        navigationView.setNavigationItemSelectedListener(navItemListener.navigationItemSelectedListener);
        MySchedulerGestureResponses mySchedulerGestureResponses = new MySchedulerGestureResponses();
        drawerLayout.setOnTouchListener(mySchedulerGestureResponses.schedulerGestures);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.pref_scheduler_title, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        if (switchButton.isChecked())
            spinner.setEnabled(true);
        else spinner.setEnabled(false);
        spinner.setSelection(selectedPosition);
        first_spinner = 1;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (first_spinner_counter < first_spinner) {
                    first_spinner_counter++;
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
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scheduleJob();
                    spinner.setEnabled(true);
                } else {
                    cancelJob();
                    spinner.setEnabled(false);
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
