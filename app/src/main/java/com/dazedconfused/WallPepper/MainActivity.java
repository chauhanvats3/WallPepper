package com.dazedconfused.WallPepper;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
import static com.dazedconfused.WallPepper.MyRuntimePreferences.QUALITY;
import static com.dazedconfused.WallPepper.MyRuntimePreferences.SHARED_PREFS;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_DEVICE_HEIGHT = "deviceHeight";
    public static final String KEY_DEVICE_WIDTH = "deviceWidth";
    private static final String TAG = "MainActivity";
    private static final String DIR_NAME = "WallPepper";
    private static WeakReference<MainActivity> activityWeakReference;
    private static int DEVICE_WIDTH;
    private static int DEVICE_HEIGHT;
    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            // Do Something
            String hotlink=photoData.getString(PHOTO_HOTLINK,"");
            if (hotlink!=null) {
                hotlink=hotlink.concat("?client_id=");
                hotlink=hotlink.concat(getResources().getString(R.string.client_id));
            }else{
                Log.wtf(TAG,"HOTLINK NOT CORRECT<----------------");
            }
            OkHttpClient client = new OkHttpClient();

            String url =hotlink;

            Request request = new Request.Builder()
                    .url(url)
                    .build();
           // Toast.makeText(ctxt, "Downloading Completed Modafuka!", Toast.LENGTH_SHORT).show();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        Log.wtf(TAG,"Hotlinking Successful!<--------------------");
                    }
                    else Log.wtf(TAG,"Hotlinking UNSUCCESSSFULL<----------------");
                    Log.wtf(TAG,response.body().string());
                }
            });
        }
    };
    private ImageView mainImage;
    private DrawerLayout drawerLayout;
    private int backPresses;
    private SharedPreferences photoData;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView downloadButton;
    private TextView uploadedBy;
    private ImageView openInNew;
    private TextView location;
    private TextView createdOn;
    private TextView downloads;
    private TextView likes;
    private TextView cameraMake;
    private TextView cameraModel;
    private TextView imageSize;
    private TextView focalLength;
    private TextView aperture;
    private TextView exposure;
    private TextView iso;
    private TextView colorPalette;
    private String qualityName;

    public static MainActivity getMInstanceActivityContext() {

        return activityWeakReference.get();
    }

    public static WeakReference<MainActivity> getMActivityWeakReference() {

        return activityWeakReference;
    }

    public int getDeviceWidth() {

        return DEVICE_WIDTH;
    }

    public int getDeviceHeight() {

        return DEVICE_HEIGHT;
    }

    private void widthHeightCalculate() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DEVICE_WIDTH = size.x;
        DEVICE_HEIGHT = size.y;
    }


    public DrawerLayout getDrawerLayout() {

        return drawerLayout;
    }

    public ImageView getImageView() {

        return mainImage;
    }

    private void saveDeviceDisplayInfo() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_DEVICE_HEIGHT, DEVICE_HEIGHT);
        editor.putInt(KEY_DEVICE_WIDTH, DEVICE_WIDTH);
        editor.apply();
    }
    private SharedPreferences appPrefs;

    public void setupPhotoDetails() {

        colorPalette.setText(photoData.getString(PHOTO_COLOR, PHOTO_DEFAULT_VALUE));
        uploadedBy.setText(photoData.getString(PHOTO_UPLOADER_NAME, PHOTO_DEFAULT_VALUE));
        String locationSTR = photoData.getString(PHOTO_CITY, "Unknown") + ", " + photoData.getString(PHOTO_COUNTRY, "Unknown");
        if (locationSTR.equals("Unknown, Unknown"))
            location.setText("Unknown");
        else
            location.setText(locationSTR);
        createdOn.setText(photoData.getString(PHOTO_CREATED_ON, PHOTO_DEFAULT_VALUE));
        downloads.setText(photoData.getString(PHOTO_DOWNLOADS, PHOTO_DEFAULT_VALUE));
        likes.setText(photoData.getString(PHOTO_LIKES, PHOTO_DEFAULT_VALUE));
        cameraMake.setText(photoData.getString(PHOTO_CAMERA_MAKE, PHOTO_DEFAULT_VALUE));
        cameraModel.setText(photoData.getString(PHOTO_CAMERA_MODEL, PHOTO_DEFAULT_VALUE));
        imageSize.setText(photoData.getString(PHOTO_SIZE, PHOTO_DEFAULT_VALUE));
        if (photoData.getString(PHOTO_FOCAL_LENGTH, PHOTO_DEFAULT_VALUE) != null)
            focalLength.setText(photoData.getString(PHOTO_FOCAL_LENGTH, PHOTO_DEFAULT_VALUE) + "mm");
        else
            focalLength.setText("n/a");
        if(photoData.getString(PHOTO_APERTURE, PHOTO_DEFAULT_VALUE)!=null)
        aperture.setText("f/"+photoData.getString(PHOTO_APERTURE, PHOTO_DEFAULT_VALUE));
        else
            aperture.setText("n/a");
        exposure.setText(photoData.getString(PHOTO_EXPOSURE, PHOTO_DEFAULT_VALUE));
        iso.setText(photoData.getString(PHOTO_ISO, PHOTO_DEFAULT_VALUE));

    }

    @Override
    protected void onResume() {

        super.onResume();
        hideStatusBar();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else if (backPresses == 0) {
            Toast.makeText(this, "Press Back Again To Close!", Toast.LENGTH_SHORT).show();
            backPresses++;
        } else {
            backPresses = 0;
            finishAffinity();
            super.onBackPressed();
        }
    }

    protected void hideStatusBar() {

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void closeKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        widthHeightCalculate();
        saveDeviceDisplayInfo();
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mainImage = findViewById(R.id.imgMainImage);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);
        downloadButton = findViewById(R.id.btnDownloadBottomSheet);
        uploadedBy = findViewById(R.id.txtUploader);
        location = findViewById(R.id.txtLocation);
        createdOn = findViewById(R.id.txtDateCreated);
        downloads = findViewById(R.id.txtDownloads);
        likes = findViewById(R.id.txtLikes);
        cameraMake = findViewById(R.id.txtCameraMake);
        cameraModel = findViewById(R.id.txtCameraModel);
        imageSize = findViewById(R.id.txtImageSize);
        focalLength = findViewById(R.id.txtFocalLength);
        aperture = findViewById(R.id.txtAperture);
        exposure = findViewById(R.id.txtExposure);
        iso = findViewById(R.id.txtISO);
        colorPalette = findViewById(R.id.txtColorPalette);
        openInNew = findViewById(R.id.imgOpenInNew);
        activityWeakReference = new WeakReference<>(this);
        backPresses = 0;
        hideStatusBar();
        MyMainGestureResponses myGestureResponses = new MyMainGestureResponses();
        mainImage.setOnTouchListener(myGestureResponses.mainActivityGestures);
        MyPermissionChecker checker = new MyPermissionChecker();
        checker.startCheck(activityWeakReference);
        MyNavItemListener navItemListener = new MyNavItemListener(activityWeakReference);
        navigationView.setNavigationItemSelectedListener(navItemListener.navigationItemSelectedListener);
        photoData = getSharedPreferences(PHOTO_DATA, MODE_PRIVATE);
        appPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        setupPhotoDetails();

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        backPresses = 0;
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int quality = appPrefs.getInt(QUALITY, 1);

                final String fileName = photoData.getString(PHOTO_ID, "123");
                String downloadLink = null;
                switch (quality) {
                    case 1:
                        downloadLink = photoData.getString(PHOTO_REG, "");
                        qualityName = "Regular";
                        //Regular
                        break;
                    case 2:
                        downloadLink = photoData.getString(PHOTO_FULL, "");
                        //HD
                        qualityName = "HD";
                        break;
                    case 3:
                        downloadLink = photoData.getString(PHOTO_RAW, "");
                        //RAW
                        qualityName = "RAW";
                        break;
                }
                downloadUsingManager(downloadLink);
                Toast.makeText(MainActivity.this, "Downloading in "+qualityName, Toast.LENGTH_SHORT).show();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        openInNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                String photoLink = photoData.getString(PHOTO_LINK, "");
                if (photoLink != null) {
                    Uri uri = Uri.parse(photoLink);
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(MainActivity.this, "Can't Open!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Couldn't Find Photo's Link!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void downloadUsingManager(String downloadUrlOfImage) {

        String filename = photoData.getString(PHOTO_ID, "1234567") +"-"+ qualityName + ".jpg";

        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + DIR_NAME + "/");


        if (!direct.exists()) {
            direct.mkdir();
            Log.d(TAG, "dir created for first time");
        }

        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(downloadUrlOfImage);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + DIR_NAME + File.separator + filename);

        dm.enqueue(request);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }
}
