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

    private WeakReference<MainActivity> mainActivityWeakReference = MainActivity.getMActivityWeakReference();
    private DrawerLayout drawerLayout = mainActivityWeakReference.get().getDrawerLayout();

    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_favorite:
                    /*mainActivityWeakReference.get().getFragManager().beginTransaction().replace(R.id.fragment_container,new FavoriteFragment()).commit();*/
                    break;
                case R.id.nav_schedule:
                    /*mainActivityWeakReference.get().getFragManager().beginTransaction().replace(R.id.fragment_container,new SchedulerFragment()).commit();*/
                    break;
                case R.id.nav_settings:
                    if (mainActivityWeakReference != null)
                        mainActivityWeakReference.get().finish();
                    mainActivityWeakReference.get().startActivity(new Intent(mainActivityWeakReference.get(), Settings.class));
                    break;
                case R.id.nav_contact:
                    Toast.makeText(mainActivityWeakReference.get(), "Contact", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_share:
                    Toast.makeText(mainActivityWeakReference.get(), "Share", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_home:
                    if (Settings.getWeakReference() != null)
                        Settings.getWeakReference().get().finish();
                    mainActivityWeakReference.get().startActivity(new Intent(mainActivityWeakReference.get(), MainActivity.class));
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

    };

}
