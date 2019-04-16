package com.dazedconfused.WallPix;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_APERTURE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_CAMERA_MAKE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_CAMERA_MODEL;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_CITY;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_COLOR;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_COUNTRY;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_CREATED_ON;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_DATA;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_DEFAULT_VALUE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_DOWNLOADS;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_EXPOSURE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_FOCAL_LENGTH;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_ISO;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_LIKES;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_LINK;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_SIZE;
import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_UPLOADER_NAME;
import static com.dazedconfused.WallPix.MyRuntimePreferences.SHARED_PREFS;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_DEVICE_HEIGHT = "deviceHeight";
    public static final String KEY_DEVICE_WIDTH = "deviceWidth";
    private static final String TAG = "MainActivity";
    private static WeakReference<MainActivity> activityWeakReference;
    private static int DEVICE_WIDTH;
    private static int DEVICE_HEIGHT;
    private ImageView mainImage;
    private DrawerLayout drawerLayout;
    private int backPresses;
    private SharedPreferences defaultSharedPrefs;
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


    public BottomSheetBehavior getBottomSheetBehavior() {

        return bottomSheetBehavior;
    }

    public SharedPreferences getMainSharedPreferences() {

        return defaultSharedPrefs;
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
        focalLength.setText(photoData.getString(PHOTO_FOCAL_LENGTH, PHOTO_DEFAULT_VALUE));
        aperture.setText(photoData.getString(PHOTO_APERTURE, PHOTO_DEFAULT_VALUE));
        exposure.setText(photoData.getString(PHOTO_EXPOSURE, PHOTO_DEFAULT_VALUE));
        iso.setText(photoData.getString(PHOTO_ISO, PHOTO_DEFAULT_VALUE));

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
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        photoData = getSharedPreferences(PHOTO_DATA, MODE_PRIVATE);
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

                Toast.makeText(MainActivity.this, "Download in HQ", Toast.LENGTH_SHORT).show();
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

}
