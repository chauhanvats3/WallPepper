package com.dazedconfused.WallPix;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
/*
Intent intent = new Intent(OldActivity.this, NewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/

class MyNavItemListener {
    private static final String TAG = "MyNavItemListener";
    Context context;
    private WeakReference<MainActivity> mainActivityWeakReference;
    private WeakReference<SettingsActivity> settingsWeakReference;
    private WeakReference<SchedulerActivity> schedulerActivityWeakReference;
    private DrawerLayout drawerLayout;
    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            if (context instanceof MainActivity){
                mainActivityWeakReference= MainActivity.getMActivityWeakReference();
                drawerLayout=mainActivityWeakReference.get().getDrawerLayout();
            }else if(context instanceof SettingsActivity){
                settingsWeakReference=SettingsActivity.getWeakReference();
                drawerLayout=settingsWeakReference.get().getDrawerLayout();
            }else if (context instanceof SchedulerActivity){
                schedulerActivityWeakReference=SchedulerActivity.getWeakReference();
                drawerLayout=schedulerActivityWeakReference.get().getDrawerLayout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.nav_favorite:
                    break;
                case R.id.nav_schedule:
                    intent = new Intent(context, SchedulerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    /*
                    if (mainActivityWeakReference != null) {
                        mainActivityWeakReference.get().finish();
                    }
                    if (settingsWeakReference != null) {
                        settingsWeakReference.get().finish();
                    }
                    mainActivityWeakReference.get().startActivity(new Intent(mainActivityWeakReference.get(), SchedulerActivity.class));*/

                    break;
                case R.id.nav_settings:
                    intent = new Intent(context, SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    /*
                    if (mainActivityWeakReference != null)
                        mainActivityWeakReference.get().finish();
                    mainActivityWeakReference.get().startActivity(new Intent(mainActivityWeakReference.get(), SettingsActivity.class));*/
                    break;
                case R.id.nav_contact:
                    Toast.makeText(context, "Contact", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_share:
                    Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_home:
                    intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    /*if (settingsWeakReference != null)
                        settingsWeakReference.get().finish();
                    if(schedulerActivityWeakReference!=null)
                        schedulerActivityWeakReference.get().finish();
                    mainActivityWeakReference.get().startActivity(new Intent(mainActivityWeakReference.get(), MainActivity.class));*/
                    break;
            }
            return true;
        }
    };

     MyNavItemListener(WeakReference reference){
         context=(Context) reference.get();
    }
}
