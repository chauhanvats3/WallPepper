package com.dazedconfused.WallPix;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;

import java.lang.ref.WeakReference;

class MyImageSetter {
    private Context appContext;
    private Unsplash unsplash;
    private String searchQuery;
    private ImageView mainImage;
    private WallpaperManager myWallpaperManager;

    MyImageSetter(WeakReference<MainActivity> receivedActivity) {
        appContext = receivedActivity.get();
        String clientId = "a68a45531fb8cb9d6f22bff8a1af925eb454f99c1adb0f2dce4f8f8da75ad2ed";
        unsplash = new Unsplash(clientId);
        searchQuery = MainActivity.getMInstanceActivityContext().getSearchQuery();
        mainImage = MainActivity.getMInstanceActivityContext().getImageView();
        myWallpaperManager = WallpaperManager.getInstance(appContext);
    }

    void setImage() {
        mainImage.setClickable(false);
        unsplash.getRandomPhoto("", true, "", searchQuery, null, null, Unsplash.ORIENTATION_PORTRAIT, new Unsplash.OnPhotoLoadedListener() {
            @Override
            public void onComplete(Photo photo) {
                String regPhotoUrl = photo.getUrls().getRegular();
                // Toast.makeText(appContext, "Link Loaded!", Toast.LENGTH_SHORT).show();

                //Without picasso downloading image
                MyDownloader downloadImage = new MyDownloader( new AsyncResponse() {
                    @Override
                    public void processFinish(Bitmap image) {
                        mainImage.setImageBitmap(image);
                        try {
                            myWallpaperManager.setBitmap(image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mainImage.setClickable(true);
                    }
                });
                downloadImage.execute(regPhotoUrl);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(appContext, "Can't Access Unsplash.com!", Toast.LENGTH_SHORT).show();
                mainImage.setClickable(true);

            }
        });

    }
}
