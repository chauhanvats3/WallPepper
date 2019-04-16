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

import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_APERTURE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_CAMERA_MAKE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_CAMERA_MODEL;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_CITY;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_COLOR;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_COUNTRY;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_CREATED_ON;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_DATA;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_DOWNLOADS;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_EXPOSURE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_FOCAL_LENGTH;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_FULL;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_HOTLINK;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_ISO;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_LIKES;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_LINK;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_RAW;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_REG;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_SIZE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_UPLOADER_NAME;

class MyImageSetter {

    private static final String TAG = "MyImageSetter";
    private Context context;
    private String clientId;
    private Unsplash unsplash;
    private ImageView mainImage;
    private WallpaperManager myWallpaperManager;
    private SharedPreferences defaultSharedPreferences;
    private SharedPreferences photoDataPrefs;
    private int devHeight;
    private int devWidth;
    private Photo myPhoto;

    MyImageSetter(MainActivity mainActivity) {
        context = mainActivity;
        clientId = mainActivity.getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        mainImage = mainActivity.getImageView();
        myWallpaperManager = WallpaperManager.getInstance(mainActivity);
        defaultSharedPreferences = mainActivity.getMainSharedPreferences();
        photoDataPrefs = mainActivity.getSharedPreferences(PHOTO_DATA, Context.MODE_PRIVATE);
        devHeight = mainActivity.getDeviceHeight();
        devWidth = mainActivity.getDeviceWidth();
    }

    MyImageSetter(MyScheduledJob myScheduledJob) {
        context = myScheduledJob;
        clientId = myScheduledJob.getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        myWallpaperManager = WallpaperManager.getInstance(myScheduledJob);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        devHeight = myScheduledJob.getDeviceHeight();
        devWidth = myScheduledJob.getDeviceWidth();
        photoDataPrefs = myScheduledJob.getSharedPreferences(PHOTO_DATA, Context.MODE_PRIVATE);
    }

    private void logImageDetails() {
        Log.wtf(TAG, "color: " + myPhoto.getColor());
        Log.wtf(TAG, "Created At:" + myPhoto.getCreatedAt());
        Log.wtf(TAG, "getID():" + myPhoto.getId());
        Log.wtf(TAG, "First Name:" + myPhoto.getUser().getFirstName());
        Log.wtf(TAG, "Username:" + myPhoto.getUser().getUsername());
        Log.wtf(TAG, "Device w:" + devWidth + " h:" + devHeight);
        Log.wtf(TAG, "Height: " + myPhoto.getHeight() + " Width:" + myPhoto.getWidth());
        Log.wtf(TAG, "Likes:" + myPhoto.getLikes());
        Log.wtf(TAG, "DownloadLocation:" + myPhoto.getLinks().getDownloadLocation());
        Log.wtf(TAG, "getPhotos():" + myPhoto.getLinks().getPhotos());
        Log.wtf(TAG, "getSelf():" + myPhoto.getLinks().getSelf());
        Log.wtf(TAG, "getPortfolio:" + myPhoto.getLinks().getPortfolio());
        Log.wtf(TAG, "Reg : " + myPhoto.getUrls().getRegular());
        Log.wtf(TAG, "Raw : " + myPhoto.getUrls().getRaw());
        Log.wtf(TAG, "Full:" + myPhoto.getUrls().getFull());
        Log.wtf(TAG, "download:" + myPhoto.getLinks().getDownload());
        Log.wtf(TAG, "Hotlink : " + myPhoto.getLinks().getDownloadLocation());
    }

    private void getNewReference() {
        mainImage = MainActivity.getMActivityWeakReference().get().getImageView();
    }

    private void savePhotoData() {
        SharedPreferences.Editor editor = photoDataPrefs.edit();
        String name = myPhoto.getUser().getName();
        editor.putString(PHOTO_LINK, myPhoto.getLinks().getHtml());
        editor.putString(PHOTO_COLOR, myPhoto.getColor());
        try {
            editor.putString(PHOTO_COUNTRY, myPhoto.getLocation().getCountry());
        } catch (Exception ex) {
            editor.putString(PHOTO_COUNTRY, "Unknown");
        }
        try {
            editor.putString(PHOTO_CITY, myPhoto.getLocation().getCity());
        } catch (Exception ex) {
            editor.putString(PHOTO_CITY, "Unknown");
        }
        if (name != null)
            editor.putString(PHOTO_UPLOADER_NAME, name);
        editor.putString(PHOTO_DOWNLOADS, myPhoto.getDownloads() + "");
        editor.putString(PHOTO_LIKES, myPhoto.getLikes() + "");
        editor.putString(PHOTO_CAMERA_MAKE, myPhoto.getExif().getMake());
        editor.putString(PHOTO_CAMERA_MODEL, myPhoto.getExif().getModel());
        editor.putString(PHOTO_SIZE, myPhoto.getHeight() + "(h) X " + myPhoto.getWidth() + "(w)");
        editor.putString(PHOTO_FOCAL_LENGTH, myPhoto.getExif().getFocalLength());
        editor.putString(PHOTO_APERTURE, myPhoto.getExif().getAperture());
        editor.putString(PHOTO_EXPOSURE, myPhoto.getExif().getExposureTime());
        editor.putString(PHOTO_ISO, Integer.toString(myPhoto.getExif().getIso()));
        editor.putString(PHOTO_REG, myPhoto.getUrls().getRegular());
        editor.putString(PHOTO_RAW, myPhoto.getUrls().getRaw());
        editor.putString(PHOTO_FULL, myPhoto.getUrls().getFull());
        editor.putString(PHOTO_CREATED_ON, myPhoto.getCreatedAt().substring(0, 10));
        editor.putString(PHOTO_HOTLINK, myPhoto.getLinks().getDownloadLocation());
        editor.apply();
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
        final String searchQuery = defaultSharedPreferences.getString(MyRuntimePreferences.KEY_PREF_SEARCH_QUERY, "");
        String prefOrientation = defaultSharedPreferences.getString(MyRuntimePreferences.KEY_PREF_ORIENTATION, "portrait");
        unsplash.getRandomPhoto("", prefFeatured, "", searchQuery, null, null, prefOrientation, new Unsplash.OnPhotoLoadedListener() {
            @Override
            public void onComplete(final Photo photo) {
                Toast.makeText(context, "Downloading " + searchQuery + " Image", Toast.LENGTH_SHORT).show();
                myPhoto = photo;
                savePhotoData();
                logImageDetails();
                String regPhotoUrl = photo.getUrls().getRegular();
                Log.wtf(TAG, "Link Acquired<------------------------------");
                //Without picasso downloading image
                MyDownloader downloadImage = new MyDownloader(new AsyncResponse() {
                    @Override
                    public void processFinish(Bitmap image) {
                        Bitmap croppedImage;
                        croppedImage = scaleCenterCrop(image, devHeight, devWidth);
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
                        if (context instanceof MainActivity)
                            MainActivity.getMInstanceActivityContext().setupPhotoDetails();
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
