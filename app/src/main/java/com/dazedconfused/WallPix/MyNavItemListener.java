package com.dazedconfused.WallPix;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MyNavItemListener {
    private static final String TAG = "MyNavItemListener";

    private WeakReference<MainActivity> mainActivityWeakReference = MainActivity.getMActivityWeakReference();
    private WeakReference<SettingsActivity> settingsWeakReference = SettingsActivity.getWeakReference();
    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_favorite:

                    break;
                case R.id.nav_schedule:
                    if (mainActivityWeakReference != null)
                        mainActivityWeakReference.get().finish();
                    if (settingsWeakReference != null)
                        settingsWeakReference.get().finish();
                    mainActivityWeakReference.get().startActivity(new Intent(mainActivityWeakReference.get(), SchedulerActivity.class));
                    break;
                case R.id.nav_settings:
                    if (mainActivityWeakReference != null)
                        mainActivityWeakReference.get().finish();
                    mainActivityWeakReference.get().startActivity(new Intent(mainActivityWeakReference.get(), SettingsActivity.class));
                    break;
                case R.id.nav_contact:
                    Toast.makeText(mainActivityWeakReference.get(), "Contact", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_share:
                    Toast.makeText(mainActivityWeakReference.get(), "Share", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_home:
                    if (settingsWeakReference!=null)
                    settingsWeakReference.get().finish();

                    mainActivityWeakReference.get().startActivity(new Intent(mainActivityWeakReference.get(), MainActivity.class));
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

    };
    private DrawerLayout drawerLayout = mainActivityWeakReference.get().getDrawerLayout();
    private WeakReference<SchedulerActivity> schedulerWeakReference =SchedulerActivity.getWeakReference();

}
