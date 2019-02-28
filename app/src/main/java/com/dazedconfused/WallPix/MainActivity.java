package com.dazedconfused.WallPix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static WeakReference<MainActivity> activityWeakReference;
    private ImageView mainImage;
    private EditText editTextSearchQuery;
    private DrawerLayout drawerLayout;
    private int backPresses;

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
        MyGestureResponses myGestureResponses = new MyGestureResponses();
        MyPermissionChecker checker = new MyPermissionChecker();

        checker.startCheck(activityWeakReference);
        mainImage.setOnTouchListener(myGestureResponses.mainActivityGestures);


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
}
