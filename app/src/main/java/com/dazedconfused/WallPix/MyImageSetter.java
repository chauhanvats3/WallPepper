package com.dazedconfused.WallPix;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;

class MyImageSetter {


    private Unsplash unsplash;
    private String searchQuery;
    private ImageView mainImage;
    private WallpaperManager myWallpaperManager;


     MyImageSetter() {
        String clientId = "a68a45531fb8cb9d6f22bff8a1af925eb454f99c1adb0f2dce4f8f8da75ad2ed";
        unsplash = new Unsplash(clientId);
        searchQuery = MainActivity.getMActivityWeakReference().get().getSearchQuery();
        mainImage = MainActivity.getMActivityWeakReference().get().getImageView();
        myWallpaperManager = WallpaperManager.getInstance(MainActivity.getMActivityWeakReference().get());
    }

    private void getNewReference() {
        mainImage = MainActivity.getMActivityWeakReference().get().getImageView();
    }

    void setImage() {
        getNewReference();
        MyRuntimePreferences.setImageSettingStatus(true);

        unsplash.getRandomPhoto("", true, "", searchQuery, null, null, Unsplash.ORIENTATION_PORTRAIT, new Unsplash.OnPhotoLoadedListener() {
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
