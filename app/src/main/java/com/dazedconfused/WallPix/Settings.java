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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class Settings extends AppCompatActivity {
    private static WeakReference<Settings> myWeakReference;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
   /* private FrameLayout settingsFrameLayout;
    private LinearLayout settingLinearLayout;
*/
    public static WeakReference<Settings> getWeakReference() {
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myWeakReference = new WeakReference<>(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        /*settingsFrameLayout = findViewById(R.id.fragment_container_settings);
        settingLinearLayout=findViewById(R.id.setting_linear_layout);
        MySettingsGestureResponses mySettingsGestureResponses = new MySettingsGestureResponses();*/
        MyNavItemListener navItemListener = new MyNavItemListener();
        navigationView.setNavigationItemSelectedListener(navItemListener.navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_settings, new SettingsFragment()).commit();
        /*settingLinearLayout.setOnTouchListener(mySettingsGestureResponses.settingsActivityGestures);*/

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
}
