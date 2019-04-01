package com.dazedconfused.WallPix;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
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
    private SharedPreferences defaultSharedPreferences;
    private int devHeight;
    private int devWidth;

    MyImageSetter(MainActivity mainActivity) {
        context = mainActivity;
        clientId = mainActivity.getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        mainImage = mainActivity.getImageView();
        myWallpaperManager = WallpaperManager.getInstance(mainActivity);
        defaultSharedPreferences = mainActivity.getMainSharedPreferences();
        devHeight=mainActivity.getDeviceHeight();
        devWidth=mainActivity.getDeviceWidth();
    }

    MyImageSetter(MyScheduledJob myScheduledJob) {
        context = myScheduledJob;
        clientId = myScheduledJob.getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        myWallpaperManager = WallpaperManager.getInstance(myScheduledJob);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        devHeight=myScheduledJob.getDeviceHeight();
        devWidth=myScheduledJob.getDeviceWidth();
    }

    private void getNewReference() {
        mainImage = MainActivity.getMActivityWeakReference().get().getImageView();
    }

    private Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    void setImage() {
      /*  if (MyRuntimePreferences.isSettingImage())
            return;*/


        if (context instanceof MainActivity)
            getNewReference();
        MyRuntimePreferences.setImageSettingStatus(true);

        boolean prefFeatured = defaultSharedPreferences.getBoolean(MyRuntimePreferences.KEY_PREF_FEATURED, true);
        String searchQuery = defaultSharedPreferences.getString(MyRuntimePreferences.KEY_PREF_SEARCH_QUERY, "");
        String prefOrientation = defaultSharedPreferences.getString(MyRuntimePreferences.KEY_PREF_ORIENTATION, "portrait");
       /* final int devWidth= defaultSharedPreferences.getInt(MyRuntimePreferences.KEY_DEVICE_WIDTH,720);
        final int devHeight= defaultSharedPreferences.getInt(MyRuntimePreferences.KEY_DEVICE_HEIGHT,1080);*/


        unsplash.getRandomPhoto("", prefFeatured, "", searchQuery, null, null, prefOrientation, new Unsplash.OnPhotoLoadedListener() {
            @Override
            public void onComplete(final Photo photo) {
                String regPhotoUrl = photo.getUrls().getRegular();
                Log.wtf(TAG, "Link Acquired<------------------------------");
                //Without picasso downloading image
                MyDownloader downloadImage = new MyDownloader(new AsyncResponse() {
                    @Override
                    public void processFinish(Bitmap image) {

                            Bitmap croppedImage=scaleCenterCrop(image,devHeight,devWidth);
                        try {
                            myWallpaperManager.setBitmap(croppedImage);
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
