package com.dazedconfused.WallPepper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


//Background thread Class To download the image to be set
class MyDownloader extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "MyDownloader";
    private AsyncResponse delegate;
    private WeakReference<MainActivity> mainActivityWeakReference;
    private WeakReference<MyScheduledJob> scheduledJobWeakReference;
    private int context;

    //Custom Constructor To Implement Interface
    MyDownloader(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interface through constructor
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (MainActivity.getMInstanceActivityContext() != null) {
            mainActivityWeakReference = MainActivity.getMActivityWeakReference();
            context = 1;
            Log.wtf(TAG,"Main Activity Reference got.<-------------------");
        } else if (MyScheduledJob.getScheduleJobReference() != null) {
            scheduledJobWeakReference = MyScheduledJob.getScheduleJobReference();
            context = 2;
            Log.wtf(TAG,"Scheduler Reference got<-----------------------");
        }
        Log.wtf(TAG, "PreDownload Phase<---------------------");
        /*if (context == 1)
            Toast.makeText(mainActivityWeakReference.get(), "Downloading " + searchQuery + " Image", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(scheduledJobWeakReference.get(), "Downloading " + searchQuery + " Image", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        URL url;
        HttpURLConnection httpURLConnection;
        Log.wtf(TAG, "Downloading The image<--------------------");
        try {
            url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream is = httpURLConnection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            if (context == 1)
                Toast.makeText(mainActivityWeakReference.get(), "Couldn't Download Image.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(scheduledJobWeakReference.get(), "Couldn't Download Image.", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        delegate.processFinish(bitmap);
    }
}

