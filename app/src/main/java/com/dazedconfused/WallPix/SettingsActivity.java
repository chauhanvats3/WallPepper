package com.dazedconfused.WallPix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private static WeakReference<SettingsActivity> myWeakReference;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    public static WeakReference<SettingsActivity> getWeakReference() {
        return myWeakReference;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myWeakReference = new WeakReference<>(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setTitle("SettingsActivity");
        setSupportActionBar(toolbar);
        hideStatusBar();
        MyNavItemListener navItemListener = new MyNavItemListener();
        navigationView.setNavigationItemSelectedListener(navItemListener.navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_settings, new SettingsFragment()).commit();
        MySettingsGestureResponses mySettingsGestureResponses = new MySettingsGestureResponses();
        drawerLayout.setOnTouchListener(mySettingsGestureResponses.settingsActivityGestures);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            startActivity(new Intent(this, MainActivity.class));
            super.onBackPressed();
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            hideStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }
}
