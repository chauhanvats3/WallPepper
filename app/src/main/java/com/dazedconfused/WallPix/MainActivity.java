package com.dazedconfused.WallPix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static WeakReference<MainActivity> activityWeakReference;
    private ImageView mainImage;
    private DrawerLayout drawerLayout;
    private int backPresses;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;

    public static MainActivity getMInstanceActivityContext() {
        return activityWeakReference.get();
    }

    public static WeakReference<MainActivity> getMActivityWeakReference() {
        return activityWeakReference;

    }

    public SharedPreferences getMainSharedPreferences() {
        return sharedPreferences;
    }

    public FragmentManager getFragManager() {
        return getSupportFragmentManager();
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public ImageView getImageView() {
        return mainImage;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        activityWeakReference = new WeakReference<>(this);
        backPresses = 0;

        //Required Objects :
        mainImage = findViewById(R.id.imgMainImage);
        MyMainGestureResponses myGestureResponses = new MyMainGestureResponses();

        mainImage.setOnTouchListener(myGestureResponses.mainActivityGestures);
        MyPermissionChecker checker = new MyPermissionChecker();
        checker.startCheck(activityWeakReference);

        MyNavItemListener navItemListener = new MyNavItemListener();
        navigationView.setNavigationItemSelectedListener(navItemListener.navigationItemSelectedListener);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (backPresses == 0) {
            Toast.makeText(this, "Press Back Again To Close!", Toast.LENGTH_SHORT).show();
            backPresses++;
        } else {
            backPresses = 0;
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
