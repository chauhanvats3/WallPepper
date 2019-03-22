package com.dazedconfused.WallPix;
import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

class MyDownloaderPicasso {
    void getImage(final Activity mainActivity, final ImageView mainImage, final String regPhotoUrl) {
        Picasso.get().load(regPhotoUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    Toast.makeText(mainActivity.getApplicationContext(), "Image Downloaded!", Toast.LENGTH_SHORT).show();

                    WallpaperManager myWallpaperManager=WallpaperManager.getInstance(mainActivity.getApplicationContext());
                    myWallpaperManager.setBitmap(bitmap);

                    mainImage.setImageBitmap(bitmap);
                    mainImage.setClickable(true);

                } catch (IOException e) {
                    Toast.makeText(mainActivity.getApplicationContext(),"Can't Set Wallpaper!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                e.printStackTrace();
                Toast.makeText(mainActivity.getApplicationContext(), "Can't Download Image!", Toast.LENGTH_SHORT).show();
                mainImage.setClickable(true);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Toast.makeText(mainActivity.getApplicationContext(), "Downloading " + " Image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
