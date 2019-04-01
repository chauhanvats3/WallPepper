package com.dazedconfused.WallPix;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MyScheduledJob extends JobService {
    private static final String TAG="MyScheduledJob";
    private boolean jobCancelled=false;
    private static WeakReference<MyScheduledJob> jobWeakReference;
    public static WeakReference<MyScheduledJob> getScheduleJobReference(){
        return jobWeakReference;
    }
public SharedPreferences getSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(jobWeakReference.get());
}
    @Override
    public void onCreate() {
        super.onCreate();
        jobWeakReference=new WeakReference<>(this);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Toast.makeText(this, "Job Started Now", Toast.LENGTH_SHORT).show();
        Log.wtf(TAG,"onStartJob called<------------------------------");
        doBackgroundWork(params);
        return true;
    }
    private void doBackgroundWork(final JobParameters params){
        new Thread(new Runnable() {
            @Override
            public void run(){
                    if(jobCancelled)
                        return;
                MyImageSetter imageSetter=new MyImageSetter(jobWeakReference.get());
                imageSetter.setImage();
                jobFinished(params,false);
            }

        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancelled=true;
        Toast.makeText(this, "Job Cancelled!", Toast.LENGTH_SHORT).show();
        return true;
    }
}
