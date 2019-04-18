package com.dazedconfused.WallPepper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

class MyNavItemListener {
    private static final String TAG = "MyNavItemListener";
    private Context context;
    private DrawerLayout drawerLayout;
    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;
            if (context instanceof MainActivity) {
                WeakReference<MainActivity> mainActivityWeakReference = MainActivity.getMActivityWeakReference();
                drawerLayout = mainActivityWeakReference.get().getDrawerLayout();
            } else if (context instanceof SettingsActivity) {
                WeakReference<SettingsActivity> schedulerActivityWeakReference = SettingsActivity.getWeakReference();
                drawerLayout = schedulerActivityWeakReference.get().getDrawerLayout();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.nav_favorite:
                    break;
                case R.id.nav_settings:
                    intent = new Intent(context, SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    /*
                    if (mainActivityWeakReference != null) {
                        mainActivityWeakReference.get().finish();
                    }
                    if (settingsWeakReference != null) {
                        settingsWeakReference.get().finish();
                    }
                    mainActivityWeakReference.get().startActivity(new Intent(mainActivityWeakReference.get(), SettingsActivity.class));*/

                    break;
                case R.id.nav_contact:
                    Intent contactIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "dazed11confused17@gmail.com", null));
                    contactIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    context.startActivity(Intent.createChooser(contactIntent, ""));
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

    MyNavItemListener(WeakReference reference) {

        context = (Context) reference.get();
    }

}
