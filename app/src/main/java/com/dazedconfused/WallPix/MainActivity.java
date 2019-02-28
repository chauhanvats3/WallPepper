package com.dazedconfused.WallPix;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static WeakReference<MainActivity> activityWeakReference;
    private ImageView mainImage;
    private EditText editTextSearchQuery;

    public static MainActivity getMInstanceActivityContext() {
        return activityWeakReference.get();
    }

    public static WeakReference<MainActivity> getMActivityWeakReference() {
        return activityWeakReference;
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
        activityWeakReference = new WeakReference<>(this);

        //Required Objects :
        mainImage = findViewById(R.id.imgMainImage);
        editTextSearchQuery = findViewById(R.id.editTextSearchQuery);
        MyGestureResponses myGestureResponses = new MyGestureResponses();
        MyPermissionChecker checker = new MyPermissionChecker();

        checker.startCheck(activityWeakReference);
        mainImage.setOnTouchListener(myGestureResponses.mainActivityGestures);

    }

}
