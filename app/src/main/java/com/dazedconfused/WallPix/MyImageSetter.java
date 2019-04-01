package com.dazedconfused.WallPix;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;

class MyImageSetter {

    private static final String TAG = "MyImageSetter";
    private static Context context;
    private String clientId;
    private Unsplash unsplash;
    private ImageView mainImage;
    private WallpaperManager myWallpaperManager;
    private SharedPreferences sharedPreferences;

    MyImageSetter(MainActivity mainActivity) {
        context = mainActivity;
        clientId = mainActivity.getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        mainImage = mainActivity.getImageView();
        myWallpaperManager = WallpaperManager.getInstance(mainActivity);
        sharedPreferences = mainActivity.getMainSharedPreferences();
    }

    MyImageSetter(MyScheduledJob myScheduledJob) {
        context = myScheduledJob;
        clientId = myScheduledJob.getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        myWallpaperManager = WallpaperManager.getInstance(myScheduledJob);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void getNewReference() {
        mainImage = MainActivity.getMActivityWeakReference().get().getImageView();
    }

    void setImage() {
      /*  if (MyRuntimePreferences.isSettingImage())
            return;*/
        if (context instanceof MainActivity)
            getNewReference();
        MyRuntimePreferences.setImageSettingStatus(true);

        boolean prefFeatured = sharedPreferences.getBoolean(MyRuntimePreferences.KEY_PREF_FEATURED, true);
        String searchQuery = sharedPreferences.getString(MyRuntimePreferences.KEY_PREF_SEARCH_QUERY, "");
        String prefOrientation = sharedPreferences.getString(MyRuntimePreferences.KEY_PREF_ORIENTATION, "portrait");


        unsplash.getRandomPhoto("", prefFeatured, "", searchQuery, null, null, prefOrientation, new Unsplash.OnPhotoLoadedListener() {
            @Override
            public void onComplete(final Photo photo) {
                String regPhotoUrl = photo.getUrls().getRegular();
                Log.wtf(TAG, "Link Acquired<------------------------------");
                //Without picasso downloading image
                MyDownloader downloadImage = new MyDownloader(new AsyncResponse() {
                    @Override
                    public void processFinish(Bitmap image) {
                        try {

                            myWallpaperManager.setBitmap(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (context instanceof MainActivity) {
                            getNewReference();
                            mainImage.setImageBitmap(image);
                        }

                        MyRuntimePreferences.setImageSettingStatus(false);
                        Log.wtf(TAG, "Image set<-------------------------");
                        Toast.makeText(context, "Image set", Toast.LENGTH_SHORT).show();

                    }
                });
                downloadImage.execute(regPhotoUrl);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, "Can't Access Unsplash.com!", Toast.LENGTH_SHORT).show();
                getNewReference();
                MyRuntimePreferences.setImageSettingStatus(false);
            }
        });

    }
}
