package com.dazedconfused.WallPepper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;

import static android.content.Context.MODE_PRIVATE;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.FEATURED;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.ORIENTATION;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_APERTURE;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_CAMERA_MAKE;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_CAMERA_MODEL;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_CITY;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_COLOR;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_COUNTRY;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_CREATED_ON;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_DATA;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_DEFAULT_VALUE;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_DOWNLOADS;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_EXPOSURE;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_FOCAL_LENGTH;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_FULL;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_HOTLINK;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_ID;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_ISO;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_LIKES;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_LINK;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_RAW;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_REG;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_SIZE;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PHOTO_UPLOADER_NAME;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.SEARCH_QUERY;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.SHARED_PREFS;
import static com.kc.unsplash.Unsplash.ORIENTATION_LANDSCAPE;
import static com.kc.unsplash.Unsplash.ORIENTATION_PORTRAIT;
import static com.kc.unsplash.Unsplash.ORIENTATION_SQUARISH;

class MyImageSetter {

    private static final String TAG = "MyImageSetter";
    private String model;
    private String focalLength;
    private String aperture;
    private String iso;
    private String hotlink;
    private String createdOn;
    private String exposure;
    private String link;
    private String make;
    private Context context;
    private String clientId;
    private Unsplash unsplash;
    private ImageView mainImage;
    private WallpaperManager myWallpaperManager;
    private SharedPreferences photoDataPrefs;
    private SharedPreferences newSharedPrefs;
    private int devHeight;
    private int devWidth;
    private String name;
    private Photo myPhoto;
    private String country;
    private String city;


    MyImageSetter(MainActivity mainActivity) {

        context = mainActivity;
        clientId = mainActivity.getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        mainImage = mainActivity.getImageView();
        myWallpaperManager = WallpaperManager.getInstance(mainActivity);
        photoDataPrefs = mainActivity.getSharedPreferences(PHOTO_DATA, MODE_PRIVATE);
        devHeight = mainActivity.getDeviceHeight();
        devWidth = mainActivity.getDeviceWidth();
        newSharedPrefs = mainActivity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    MyImageSetter(MyScheduledJob myScheduledJob) {

        context = myScheduledJob;
        clientId = myScheduledJob.getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        myWallpaperManager = WallpaperManager.getInstance(myScheduledJob);
        devHeight = myScheduledJob.getDeviceHeight();
        devWidth = myScheduledJob.getDeviceWidth();
        photoDataPrefs = myScheduledJob.getSharedPreferences(PHOTO_DATA, MODE_PRIVATE);
        newSharedPrefs = myScheduledJob.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    private void logImageDetails() {

        //Log.wtf(TAG, "color: " + myPhoto.getColor());
        //Log.wtf(TAG, "Created At:" + myPhoto.getCreatedAt());
        Log.wtf(TAG, "getID():" + myPhoto.getId());
        Log.wtf(TAG, "First Name:" + name);
        //Log.wtf(TAG, "Username:" + myPhoto.getUser().getUsername());
        //Log.wtf(TAG, "Device w:" + devWidth + " h:" + devHeight);
        //Log.wtf(TAG, "Height: " + myPhoto.getHeight() + " Width:" + myPhoto.getWidth());
        //Log.wtf(TAG, "Likes:" + myPhoto.getLikes());
        //Log.wtf(TAG, "getPhotos():" + myPhoto.getLinks().getPhotos());
        //Log.wtf(TAG, "getSelf():" + myPhoto.getLinks().getSelf());
        //Log.wtf(TAG, "getPortfolio:" + myPhoto.getLinks().getPortfolio());
        //Log.wtf(TAG, "Reg : " + myPhoto.getUrls().getRegular());
        //Log.wtf(TAG, "Raw : " + myPhoto.getUrls().getRaw());
        //Log.wtf(TAG, "Full:" + myPhoto.getUrls().getFull());
        //Log.wtf(TAG, "download:" + myPhoto.getLinks().getDownload());
        Log.wtf(TAG, "Hotlink : " + hotlink);
    }

    private void getNewReference() {

        mainImage = MainActivity.getMActivityWeakReference().get().getImageView();
    }

    private void savePhotoData() {

        SharedPreferences.Editor editor = photoDataPrefs.edit();

        try {
            name = myPhoto.getUser().getName();
        } catch (Exception e) {
            name = PHOTO_DEFAULT_VALUE;
            e.printStackTrace();
        }
        try {
            link = myPhoto.getLinks().getHtml();
        } catch (Exception e) {
            link = PHOTO_DEFAULT_VALUE;
            e.printStackTrace();
        }

        try {
            country = myPhoto.getLocation().getCountry();
        } catch (Exception ex) {
            country = "Unknown";
        }
        try {
            city = myPhoto.getLocation().getCity();
        } catch (Exception ex) {
            city = "Unknown";
        }
        try {
            make = myPhoto.getExif().getMake();
        } catch (Exception e) {
            make = PHOTO_DEFAULT_VALUE;
            e.printStackTrace();
        }
        try {
            model = myPhoto.getExif().getModel();
        } catch (Exception e) {
            model = PHOTO_DEFAULT_VALUE;
            e.printStackTrace();
        }

        try {
            focalLength = myPhoto.getExif().getFocalLength();
        } catch (Exception e) {
            focalLength = PHOTO_DEFAULT_VALUE;
            e.printStackTrace();
        }
        try {
            aperture = myPhoto.getExif().getAperture();
        } catch (Exception e) {
            aperture = PHOTO_DEFAULT_VALUE;
            e.printStackTrace();
        }
        try {
            exposure = myPhoto.getExif().getExposureTime();
        } catch (Exception e) {
            exposure = PHOTO_DEFAULT_VALUE;
            e.printStackTrace();
        }
        try {
            iso = Integer.toString(myPhoto.getExif().getIso());
        } catch (Exception ex) {
            iso = PHOTO_DEFAULT_VALUE;
        }
        try {
            createdOn = myPhoto.getCreatedAt().substring(0, 10);
        } catch (Exception e) {
            createdOn = PHOTO_DEFAULT_VALUE;
            e.printStackTrace();
        }
        try {
            hotlink = myPhoto.getLinks().getDownloadLocation();
        } catch (Exception e) {
            hotlink = PHOTO_DEFAULT_VALUE;
            e.printStackTrace();
        }
        editor.putString(PHOTO_UPLOADER_NAME, name);
        editor.putString(PHOTO_CAMERA_MAKE, make);
        editor.putString(PHOTO_EXPOSURE, exposure);
        editor.putString(PHOTO_LINK, link);
        editor.putString(PHOTO_COLOR, myPhoto.getColor());
        editor.putString(PHOTO_COUNTRY, country);
        editor.putString(PHOTO_CITY, city);
        editor.putString(PHOTO_ISO, iso);
        editor.putString(PHOTO_REG, myPhoto.getUrls().getRegular());
        editor.putString(PHOTO_RAW, myPhoto.getUrls().getRaw());
        editor.putString(PHOTO_FULL, myPhoto.getUrls().getFull());
        editor.putString(PHOTO_FOCAL_LENGTH, focalLength);
        editor.putString(PHOTO_CREATED_ON, createdOn);
        editor.putString(PHOTO_APERTURE, aperture);
        editor.putString(PHOTO_DOWNLOADS, myPhoto.getDownloads() + "");
        editor.putString(PHOTO_LIKES, myPhoto.getLikes() + "");
        editor.putString(PHOTO_CAMERA_MODEL, model);
        editor.putString(PHOTO_SIZE, myPhoto.getHeight() + "(h) X " + myPhoto.getWidth() + "(w)");
        editor.putString(PHOTO_HOTLINK, hotlink);
        editor.putString(PHOTO_ID,myPhoto.getId());
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

        if (context instanceof MainActivity)
            getNewReference();
        MyRuntimePreferences.setImageSettingStatus(true);
        boolean prefFeatured = newSharedPrefs.getBoolean(FEATURED, true);
        final String searchQuery = newSharedPrefs.getString(SEARCH_QUERY, "");
        int prefOrientationInt = newSharedPrefs.getInt(ORIENTATION, 1);
        String prefOrientation = "";
        switch (prefOrientationInt) {
            case 1:
                prefOrientation = ORIENTATION_PORTRAIT;
                break;
            case 2:
                prefOrientation = ORIENTATION_LANDSCAPE;
                break;
            case 3:
                prefOrientation = ORIENTATION_SQUARISH;
                break;
        }

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
