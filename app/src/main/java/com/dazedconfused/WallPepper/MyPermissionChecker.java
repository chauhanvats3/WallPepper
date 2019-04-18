package com.dazedconfused.WallPepper;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;


class MyPermissionChecker {
    private static final String TAG = "MyPermissionChecker";
    private int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

    private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

    void startCheck(WeakReference<MainActivity> myAppRefernce) {
        Context myAppContext = myAppRefernce.get();
        Activity myAppActivity = myAppRefernce.get();
        ImageView mainImage = MainActivity.getMInstanceActivityContext().getImageView();
        final WallpaperManager myWallpaperManager = WallpaperManager.getInstance(myAppContext.getApplicationContext());
        //Checking permissions to read current wallpaper
        if (PermissionChecker.checkSelfPermission(myAppContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(myAppActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(myAppContext, "Need To Read Current Wallpaper, Grant Storage Permission", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(myAppActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                Drawable currentWallpaper = myWallpaperManager.getFastDrawable();
                mainImage.setImageDrawable(currentWallpaper);
                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Drawable currentWallpaper = myWallpaperManager.getFastDrawable();
            mainImage.setImageDrawable(currentWallpaper);
        }
        ActivityCompat.requestPermissions(myAppActivity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

    }
}
