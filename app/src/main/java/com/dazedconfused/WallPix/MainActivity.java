package com.dazedconfused.WallPix;

import android.annotation.SuppressLint;
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

import static com.dazedconfused.WallPix.MyRuntimePreferences.PHOTO_DATA;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_DEVICE_HEIGHT = "deviceHeight";
    public static final String KEY_DEVICE_WIDTH = "deviceWidth";
    private static final String TAG = "MainActivity";
    private static WeakReference<MainActivity> activityWeakReference;
    private static int DEVICE_WIDTH;
    private static int DEVICE_HEIGHT;
    private ImageView mainImage;
    private DrawerLayout drawerLayout;
    private int backPresses;
    private SharedPreferences sharedPreferences;
    private SharedPreferences photoDataPrefs;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView downloadButton;
    private TextView uploadedBy;

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
        return sharedPreferences;
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
        downloadButton=findViewById(R.id.btnDownloadBottomSheet);
        uploadedBy=findViewById(R.id.txtUploadedBy);
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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        photoDataPrefs=getSharedPreferences(PHOTO_DATA,MODE_PRIVATE);
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
                Toast.makeText(MainActivity.this, "Downloading in HQ", Toast.LENGTH_SHORT).show();
            }
        });
        uploadedBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uploaderLink=photoDataPrefs.getString(MyRuntimePreferences.PHOTO_UPLOADER_LINK,"N/A");
                Uri uri = Uri.parse(uploaderLink);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
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
        else if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
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
        int uiOptions =View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
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
