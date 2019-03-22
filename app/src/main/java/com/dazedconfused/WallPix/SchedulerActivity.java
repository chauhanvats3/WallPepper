package com.dazedconfused.WallPix;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class SchedulerActivity extends AppCompatActivity {
    private static final String TAG = "SchedulerActivity";
    private static WeakReference<SchedulerActivity> activityWeakReference;
    private final int JOB_ID = 6969;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    public static WeakReference<SchedulerActivity> getWeakReference() {
        return activityWeakReference;
    }

    protected void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        activityWeakReference = new WeakReference<>(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.scheduler_toolbar);
        toolbar.setTitle("Scheduler");
        setSupportActionBar(toolbar);
        hideStatusBar();
        MyNavItemListener navItemListener = new MyNavItemListener();
        navigationView.setNavigationItemSelectedListener(navItemListener.navigationItemSelectedListener);
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

    public void scheduleJob(View view) {
        ComponentName componentName = new ComponentName(this, MyScheduledJob.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    public void cancelJob(View view) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
    }

}
