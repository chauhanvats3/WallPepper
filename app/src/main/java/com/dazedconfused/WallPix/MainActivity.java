package com.dazedconfused.WallPix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_DEVICE_HEIGHT="deviceHeight";
    public static final String KEY_DEVICE_WIDTH="deviceWidth";
    private static WeakReference<MainActivity> activityWeakReference;
    private ImageView mainImage;
    private DrawerLayout drawerLayout;
    private int backPresses;
    private SharedPreferences sharedPreferences;
    private BottomSheetBehavior bottomSheetBehavior;
    private static int DEVICE_WIDTH;
    private static int DEVICE_HEIGHT;
    public int getDeviceWidth(){
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

    public static MainActivity getMInstanceActivityContext() {
        return activityWeakReference.get();
    }

    public static WeakReference<MainActivity> getMActivityWeakReference() {
        return activityWeakReference;

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
        editor.putInt(KEY_DEVICE_HEIGHT,DEVICE_HEIGHT);
        editor.putInt(KEY_DEVICE_WIDTH,DEVICE_WIDTH);
        editor.apply();
    }

    public void loadDeviceDisplayInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        DEVICE_HEIGHT=sharedPreferences.getInt(KEY_DEVICE_HEIGHT,1080);
        DEVICE_WIDTH=sharedPreferences.getInt(KEY_DEVICE_WIDTH,720);
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


        activityWeakReference = new WeakReference<>(this);
        backPresses = 0;

        MyMainGestureResponses myGestureResponses = new MyMainGestureResponses();

        mainImage.setOnTouchListener(myGestureResponses.mainActivityGestures);
        MyPermissionChecker checker = new MyPermissionChecker();
        checker.startCheck(activityWeakReference);

        MyNavItemListener navItemListener = new MyNavItemListener();
        navigationView.setNavigationItemSelectedListener(navItemListener.navigationItemSelectedListener);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (backPresses == 0) {
            Toast.makeText(this, "Press Back Again To Close!", Toast.LENGTH_SHORT).show();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            backPresses++;
        } else {
            backPresses = 0;
            finishAffinity();
            super.onBackPressed();
        }
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
