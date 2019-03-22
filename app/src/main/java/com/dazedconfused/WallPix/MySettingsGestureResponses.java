package com.dazedconfused.WallPix;

import android.widget.Toast;

import java.lang.ref.WeakReference;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

class MySettingsGestureResponses {
    private WeakReference<Settings> settingsWeakReference = Settings.getWeakReference();
    //THIS IS GESTURE DEFINITIONS FOR SETTINGS PAGE

    MyOnSwipeListener settingsActivityGestures = new MyOnSwipeListener(settingsWeakReference.get()) {
        @Override
        public void onClick() {
            super.onClick();
            settingsWeakReference.get().closeKeyboard();
        }

        @Override
        public void onDoubleClick() {
            super.onDoubleClick();
            settingsWeakReference.get().closeKeyboard();

            // your on onDoubleClick here
        }

        @Override
        public void onLongClick() {
            super.onLongClick();
            settingsWeakReference.get().closeKeyboard();

            // your on onLongClick here
        }

        @Override
        public void onSwipeUp() {
            super.onSwipeUp();
            settingsWeakReference.get().closeKeyboard();

        }

        @Override
        public void onSwipeDown() {
            super.onSwipeDown();
            settingsWeakReference.get().closeKeyboard();
        }

        @Override
        public void onSwipeLeft() {
            super.onSwipeLeft();
            settingsWeakReference.get().closeKeyboard();

            // your swipe left here.
        }

        @Override
        public void onSwipeRight() {
            super.onSwipeRight();
            settingsWeakReference.get().closeKeyboard();
            Toast.makeText(Settings.getWeakReference().get(), "Swipe Right!", Toast.LENGTH_SHORT).show();
            DrawerLayout drawerLayout = settingsWeakReference.get().getDrawerLayout();
            drawerLayout.openDrawer(GravityCompat.START);
            // your swipe right here.
        }
    };
    //
    //
    //
}
