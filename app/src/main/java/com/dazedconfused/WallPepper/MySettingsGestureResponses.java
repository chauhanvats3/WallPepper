package com.dazedconfused.WallPepper;

import java.lang.ref.WeakReference;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

class MySettingsGestureResponses {
    private static final String TAG = "SchedulerGestureResponses";

    private WeakReference<SettingsActivity> schedulerReference = SettingsActivity.getWeakReference();

    //THESE ARE SWIPE DEFINITIONS FOR MAIN ACTIVITY
    MyOnSwipeListener schedulerGestures = new MyOnSwipeListener(schedulerReference.get()) {
        @Override
        public void onClick() {

            super.onClick();
        }

        @Override
        public void onDoubleClick() {

            super.onDoubleClick();
            // your on onDoubleClick here
        }

        @Override
        public void onLongClick() {

            super.onLongClick();
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
            DrawerLayout drawerLayout = schedulerReference.get().getDrawerLayout();
            drawerLayout.openDrawer(GravityCompat.START);
            // your swipe right here.
        }
    };

}
