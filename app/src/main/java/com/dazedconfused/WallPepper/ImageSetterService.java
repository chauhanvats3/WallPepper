package com.dazedconfused.WallPepper;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.dazedconfused.WallPepper.App.CHANNEL_ID;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.KEY_DEVICE_HEIGHT;
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
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_FEATURED;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_ORIENTATION_CHECKED_RDBTN;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_SEARCH_QUERY;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.PREF_WALLPAPER_CHECKED_RDBTN;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.SHARED_PREFS;
import static com.kc.unsplash.Unsplash.ORIENTATION_LANDSCAPE;
import static com.kc.unsplash.Unsplash.ORIENTATION_PORTRAIT;
import static com.kc.unsplash.Unsplash.ORIENTATION_SQUARISH;

public class ImageSetterService extends Service {
    private static final String TAG = "ImageSetterService";
    private String model;
    private String focalLength;
    private String aperture;
    private String iso;
    private String hotlink;
    private String createdOn;
    private String exposure;
    private String link;
    private String make;
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
    private Target mTarget;
    private int context;
    private WeakReference<MainActivity> mainActivityWeakReference;
    private WeakReference<MyScheduledJob> scheduledJobWeakReference;

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG, "onCreate: started<--------------");
        try {
            if(!MainActivity.getMInstanceActivityContext().isFinishing()){

                context = 1;
                mainActivityWeakReference = MainActivity.getMActivityWeakReference();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            context = 2;
            scheduledJobWeakReference = MyScheduledJob.getScheduleJobReference();
        }

        clientId = getString(R.string.client_id);
        unsplash = new Unsplash(clientId);
        if (context == 1) {
            mainImage = mainActivityWeakReference.get().getImageView();
        }
        myWallpaperManager = WallpaperManager.getInstance(this);
        photoDataPrefs = getSharedPreferences(PHOTO_DATA, MODE_PRIVATE);
        newSharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        devHeight = newSharedPrefs.getInt(KEY_DEVICE_HEIGHT,1080);
        devWidth = newSharedPrefs.getInt(KEY_DEVICE_HEIGHT,720);
        Log.d(TAG, "onCreate: ended<--------------");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: <--------------");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("WallPepper")
                .setContentText("Setting Image")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        Log.d(TAG, "startForeground called<---------------- ");

        //do heavy work on a background thread

        setImage();
        //stopSelf();


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "onDestroy: <---------------");
        super.onDestroy();
    }

    void setImage() {

        Log.d(TAG, "setImage: <-----------------------");
        if (context == 1)
            getNewReference();
        MyRuntimePreferences.setImageSettingStatus(true);
        boolean prefFeatured = newSharedPrefs.getBoolean(PREF_FEATURED, true);
        final String searchQuery = newSharedPrefs.getString(PREF_SEARCH_QUERY, "");
        int prefOrientationInt = newSharedPrefs.getInt(PREF_ORIENTATION_CHECKED_RDBTN, R.id.rdbtnPortrait);
        String prefOrientation = "";
        switch (prefOrientationInt) {
            case R.id.rdbtnPortrait:
                prefOrientation = ORIENTATION_PORTRAIT;
                break;
            case R.id.rdbtnLandscape:
                prefOrientation = ORIENTATION_LANDSCAPE;
                break;
            case R.id.rdbtnSquarish:
                prefOrientation = ORIENTATION_SQUARISH;
                break;
        }

        unsplash.getRandomPhoto("", prefFeatured, "", searchQuery, null, null, prefOrientation, new Unsplash.OnPhotoLoadedListener() {
            @Override
            public void onComplete(final Photo photo) {

                Log.d(TAG, "Unsplash: onComplete: <---------------");

                if (context == 1)
                    Toast.makeText(ImageSetterService.this, "Downloading " + searchQuery + " Image", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ImageSetterService.this, "Downloading " + searchQuery + " Image", Toast.LENGTH_SHORT).show();

                myPhoto = photo;
                savePhotoData();
                logImageDetails();
                String regPhotoUrl;
                String type = null;
                int wlprQuality = newSharedPrefs.getInt(PREF_WALLPAPER_CHECKED_RDBTN, R.id.rdbtnWlprRegular);
                switch (wlprQuality) {
                    case R.id.rdbtnWlprRegular:
                        regPhotoUrl = myPhoto.getUrls().getRegular();
                        type = "reg";
                        break;
                    case R.id.rdbtnWlprHD:
                        regPhotoUrl = myPhoto.getUrls().getFull();
                        type = "HD";
                        break;
                    case R.id.rdbtnWlprRAW:
                        regPhotoUrl = myPhoto.getUrls().getRaw();
                        type = "RAW";
                        break;
                    default:
                        regPhotoUrl = null;
                }
                Log.d(TAG, type + " Link Acquired<------------------------------");
                //Without picasso downloading image
                /*MyDownloader downloadImage = new MyDownloader(new AsyncResponse() {
                    @Override
                    public void processFinish(Bitmap image) {

                        Bitmap croppedImage;
                        croppedImage = scaleCenterCrop(image, devHeight, devWidth);
                            setWallpaper(croppedImage);
                      }
                });
                downloadImage.execute(regPhotoUrl);*/

                //With Picasso
                mTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        Log.d(TAG, "onBitmapLoaded: <-------------");
                        mySetWallpaper(bitmap);
                       /* try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        Log.d(TAG, "onBitmapFailed: <---------------");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                        Log.d(TAG, "onPrepareLoad: <-------------------");
                    }
                };
                Log.d(TAG, "Before Picasso run line <------------");
                Picasso.get().load(regPhotoUrl).centerCrop().resize(devWidth, devHeight).into(mTarget);
                Log.d(TAG, "After Picasso run line<-------------");
                //Picasso ends
            }

            @Override
            public void onError(String error) {

                Log.d(TAG, "Unsplash: onError: <---------------");
                if (context == 1) {
                    Toast.makeText(ImageSetterService.this, "Can't Access Unsplash.com!", Toast.LENGTH_SHORT).show();
                    getNewReference();
                } else
                    Toast.makeText(ImageSetterService.this, "Can't Access Unsplash.com!", Toast.LENGTH_SHORT).show();

                MyRuntimePreferences.setImageSettingStatus(false);
            }
        });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void mySetWallpaper(Bitmap bitmap) {

        try {
            myWallpaperManager.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (context == 1) {
            getNewReference();
            mainImage.setImageBitmap(bitmap);
        }
        MyRuntimePreferences.setImageSettingStatus(false);
        Log.d(TAG, "Image set<-------------------------");

        Toast.makeText(this, "Image set", Toast.LENGTH_SHORT).show();

        if (MainActivity.getMInstanceActivityContext() != null)
            MainActivity.getMInstanceActivityContext().setupPhotoDetails();

         Intent serviceIntent = new Intent(this, ImageSetterService.class);
         stopService(serviceIntent);
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
        editor.putString(PHOTO_ID, myPhoto.getId());
        editor.apply();
        Log.d(TAG, "savePhotoData: <--------------------");
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

        mainImage = MainActivity.getMInstanceActivityContext().getImageView();
    }

}
