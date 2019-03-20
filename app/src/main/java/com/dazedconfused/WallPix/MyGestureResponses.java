package com.dazedconfused.WallPix;

import android.view.View;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

class MyGestureResponses {
    private  WeakReference<MainActivity> mainActivityWeakReference= MainActivity.getMActivityWeakReference();
    private EditText editTextSearchQuery = MainActivity.getMInstanceActivityContext().getEditTextSearchQuery();


     MyOnSwipeListener mainActivityGestures=new MyOnSwipeListener(mainActivityWeakReference.get()) {
         MyImageSetter imageSetter;

         @Override
        public void onClick() {
            super.onClick();
            if(!MyRuntimePreferences.isSettingImage()){
                imageSetter = new MyImageSetter();
                imageSetter.setImage();
            }
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
            mainActivityWeakReference.get().closeKeyboard();

            editTextSearchQuery.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSwipeDown() {
            super.onSwipeDown();
            mainActivityWeakReference.get().closeKeyboard();

            editTextSearchQuery.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onSwipeLeft() {
            super.onSwipeLeft();
            mainActivityWeakReference.get().closeKeyboard();

            // your swipe left here.
        }

        @Override
        public void onSwipeRight() {
            super.onSwipeRight();
            mainActivityWeakReference.get().closeKeyboard();
            DrawerLayout drawerLayout=mainActivityWeakReference.get().getDrawerLayout();
            drawerLayout.openDrawer(GravityCompat.START);
            // your swipe right here.
        }
    };
}
