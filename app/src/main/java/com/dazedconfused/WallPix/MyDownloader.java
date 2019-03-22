package com.dazedconfused.WallPix;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


//Interface is for passing the downloaded image to the image view in main thread
interface AsyncResponse {
    void processFinish(Bitmap image);
}

//Background thread Class To download the image to be set
class MyDownloader extends AsyncTask<String, Void, Bitmap> {
    private AsyncResponse delegate;
    private String searchQuery;
    private WeakReference<MainActivity> weakReference;
    private SharedPreferences sharedPreferences;
    private static final String TAG="MyDownloader";


    //Custom Constructor To Implement Interface
    MyDownloader(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interface through constructor
        weakReference = MainActivity.getMActivityWeakReference();
        sharedPreferences=weakReference.get().getMainSharedPreferences();
        searchQuery=sharedPreferences.getString(MyRuntimePreferences.KEY_PREF_SEARCH_QUERY,"") ;   }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(weakReference.get(), "Downloading " + searchQuery + " Image", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        URL url;
        HttpURLConnection httpURLConnection;

        try {
            url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream is = httpURLConnection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(weakReference.get(), "Couldn't Download " + searchQuery + " Image.", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        delegate.processFinish(bitmap);
    }
}

