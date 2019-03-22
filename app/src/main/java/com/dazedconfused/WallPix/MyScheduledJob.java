package com.dazedconfused.WallPix;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class MyScheduledJob extends JobService {
    private static final String TAG="MyScheduledJob";
    private boolean jobCancelled=false;
    @Override
    public boolean onStartJob(JobParameters params) {
        doBackgroundWork(params);
        return true;
    }
    private void doBackgroundWork(final JobParameters params){
        new Thread(new Runnable() {
            @Override
            public void run(){
                for (int i=0;i<11;i++){
                    if(jobCancelled)
                        return;
                    Log.d(TAG,i+"th run");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG,"Job Finished");

                jobFinished(params,false);
            }

        });
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancelled=true;
        return true;
    }
}
