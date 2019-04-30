package com.dazedconfused.WallPepper;

import android.content.Intent;
import android.util.Log;

import java.lang.ref.WeakReference;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

class MyMainGestureResponses {
    private static final String TAG = "MyMainGesture";

    private WeakReference<MainActivity> mainActivityWeakReference = MainActivity.getMActivityWeakReference();
    //THESE ARE SWIPE DEFINITIONS FOR MAIN ACTIVITY
    MyOnSwipeListener mainActivityGestures = new MyOnSwipeListener(mainActivityWeakReference.get()) {


        @Override
        public void onClick() {

            super.onClick();
            Log.d(TAG, "onClick: <---------------");


            Intent serviceIntent = new Intent(mainActivityWeakReference.get(), ImageSetterService.class);

            ContextCompat.startForegroundService(mainActivityWeakReference.get(), serviceIntent);


        }

        @Override
        public void onDoubleClick() {

            super.onDoubleClick();
            mainActivityWeakReference.get().closeKeyboard();
            // your on onDoubleClick here
        }

        @Override
        public void onLongClick() {

            super.onLongClick();
            mainActivityWeakReference.get().closeKeyboard();
            // your on onLongClick here
        }

        @Override
        public void onSwipeUp() {

            super.onSwipeUp();
        }

        @Override
        public void onSwipeDown() {

            super.onSwipeDown();
        }

        @Override
        public void onSwipeLeft() {

            super.onSwipeLeft();
            // your swipe left here.
        }

        @Override
        public void onSwipeRight() {

            super.onSwipeRight();
            DrawerLayout drawerLayout = mainActivityWeakReference.get().getDrawerLayout();
            drawerLayout.openDrawer(GravityCompat.START);
            // your swipe right here.
        }
    };

}
