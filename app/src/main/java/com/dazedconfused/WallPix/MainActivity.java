package com.dazedconfused.WallPix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {
    private static WeakReference<MainActivity> activityWeakReference;
    private ImageView mainImage;
    private EditText editTextSearchQuery;
    private DrawerLayout drawerLayout;
    private int backPresses;
    private NavigationView navigationView;

    public static MainActivity getMInstanceActivityContext() {
        return activityWeakReference.get();
    }

    public static WeakReference<MainActivity> getMActivityWeakReference() {
        return activityWeakReference;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public ImageView getImageView() {
        return mainImage;
    }

    public EditText getEditTextSearchQuery() {
        return editTextSearchQuery;
    }

    public String getSearchQuery() {
        return editTextSearchQuery.getText().toString().trim();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        activityWeakReference = new WeakReference<>(this);
        backPresses = 0;

        //Required Objects :
        mainImage = findViewById(R.id.imgMainImage);
        editTextSearchQuery = findViewById(R.id.editTextSearchQuery);
        navigationView=findViewById(R.id.nav_view);
        MyGestureResponses myGestureResponses = new MyGestureResponses();
        MyPermissionChecker checker = new MyPermissionChecker();

        checker.startCheck(activityWeakReference);
        mainImage.setOnTouchListener(myGestureResponses.mainActivityGestures);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_favorite:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FavoriteFragment()).addToBackStack("favorite").commit();
                        break;
                    case R.id.nav_schedule:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SchedulerFragment()).addToBackStack("scheduler").commit();
                        break;
                    case R.id.nav_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).addToBackStack("settings").commit();
                        break;
                    case R.id.nav_contact:
                        Toast.makeText(MainActivity.this, "Contact", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_home:
                        //getSupportFragmentManager().popBackStack();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });

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
