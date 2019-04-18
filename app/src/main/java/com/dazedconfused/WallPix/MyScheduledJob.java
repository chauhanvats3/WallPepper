package com.dazedconfused.WallPix;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import static com.dazedconfused.WallPix.MyRuntimePreferences.IS_JOB_GOING;
import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_DEVICE_HEIGHT;
import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_DEVICE_WIDTH;
import static com.dazedconfused.WallPix.MyRuntimePreferences.NO_OF_JOBS_DONE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.SHARED_PREFS;

public class MyScheduledJob extends JobService {
    private static final String TAG = "MyScheduledJob";
    private static int DEVICE_WIDTH;
    private static int DEVICE_HEIGHT;
    private static WeakReference<MyScheduledJob> jobWeakReference;
    private static boolean jobStatus;

    public static WeakReference<MyScheduledJob> getScheduleJobReference() {
        return jobWeakReference;
    }

    public int getDeviceHeight() {
        return DEVICE_HEIGHT;
    }

    public int getDeviceWidth() {
        return DEVICE_WIDTH;
    }

    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(jobWeakReference.get());
    }

    public void loadDeviceDisplayInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        DEVICE_HEIGHT = sharedPreferences.getInt(KEY_DEVICE_HEIGHT, 1080);
        DEVICE_WIDTH = sharedPreferences.getInt(KEY_DEVICE_WIDTH, 720);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        jobWeakReference = new WeakReference<>(this);
        loadDeviceDisplayInfo();
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        jobStatus=sharedPreferences.getBoolean(IS_JOB_GOING,true);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        int noOfJobs=sharedPreferences.getInt(NO_OF_JOBS_DONE,0);
        noOfJobs++;
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt(NO_OF_JOBS_DONE,noOfJobs);
        editor.apply();
        Toast.makeText(this, "Job Started Now", Toast.LENGTH_SHORT).show();
        Log.wtf(TAG, "onStartJob called<------------------------------");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyImageSetter imageSetter = new MyImageSetter(jobWeakReference.get());
                imageSetter.setImage();
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobStatus = false;
        Toast.makeText(this, "Job Cancelled!", Toast.LENGTH_SHORT).show();
        return true;
    }
}
