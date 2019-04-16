package com.dazedconfused.WallPix;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.lang.ref.WeakReference;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

class MyMainGestureResponses {
    private static final String TAG = "MyMainGesture";

    private WeakReference<MainActivity> mainActivityWeakReference = MainActivity.getMActivityWeakReference();
    private BottomSheetBehavior bottomSheetBehavior;
    //THESE ARE SWIPE DEFINITIONS FOR MAIN ACTIVITY
    MyOnSwipeListener mainActivityGestures = new MyOnSwipeListener(mainActivityWeakReference.get()) {
        MyImageSetter imageSetter;


        @Override
        public void onClick() {
            super.onClick();
            if (!MyRuntimePreferences.isSettingImage()) {
                imageSetter = new MyImageSetter(mainActivityWeakReference.get());
                imageSetter.setImage();
            } else
                mainActivityWeakReference.get().closeKeyboard();
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
