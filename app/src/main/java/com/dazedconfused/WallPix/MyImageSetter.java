package com.dazedconfused.WallPix;

import android.app.WallpaperManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;

class MyImageSetter {


    private Unsplash unsplash;
    private ImageView mainImage;
    private WallpaperManager myWallpaperManager;

    MyImageSetter() {
        String clientId = MainActivity.getMActivityWeakReference().get().getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        mainImage = MainActivity.getMActivityWeakReference().get().getImageView();
        myWallpaperManager = WallpaperManager.getInstance(MainActivity.getMActivityWeakReference().get());
    }

    private void getNewReference() {
        mainImage = MainActivity.getMActivityWeakReference().get().getImageView();
    }

    void setImage() {
        getNewReference();
        MyRuntimePreferences.setImageSettingStatus(true);
        SharedPreferences sharedPreferences = MainActivity.getMActivityWeakReference().get().getMainSharedPreferences();
        boolean prefFeatured = sharedPreferences.getBoolean(MyRuntimePreferences.KEY_PREF_FEATURED,true);
        String searchQuery = sharedPreferences.getString(MyRuntimePreferences.KEY_PREF_SEARCH_QUERY, "");
        String prefOrientation = sharedPreferences.getString(MyRuntimePreferences.KEY_PREF_ORIENTATION,"portrait");
        unsplash.getRandomPhoto("",prefFeatured , "", searchQuery, null, null, prefOrientation, new Unsplash.OnPhotoLoadedListener() {
            @Override
            public void onComplete(Photo photo) {
                String regPhotoUrl = photo.getUrls().getRegular();
                //Without picasso downloading image
                MyDownloader downloadImage = new MyDownloader(new AsyncResponse() {
                    @Override
                    public void processFinish(Bitmap image) {
                        try {
                            myWallpaperManager.setBitmap(image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        getNewReference();
                        mainImage.setImageBitmap(image);
                        MyRuntimePreferences.setImageSettingStatus(false);
                        Toast.makeText(MainActivity.getMActivityWeakReference().get(), "Image Set!", Toast.LENGTH_SHORT).show();
                    }
                });
                downloadImage.execute(regPhotoUrl);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.getMActivityWeakReference().get(), "Can't Access Unsplash.com!", Toast.LENGTH_SHORT).show();
                getNewReference();
                MyRuntimePreferences.setImageSettingStatus(false);
            }
        });

    }
}
