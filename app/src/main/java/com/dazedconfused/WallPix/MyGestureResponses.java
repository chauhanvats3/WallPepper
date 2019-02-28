package com.dazedconfused.WallPix;

import android.view.View;
import android.widget.EditText;

import java.lang.ref.WeakReference;

class MyGestureResponses {
    private  WeakReference<MainActivity> mainActivityWeakReference= MainActivity.getMActivityWeakReference();
    private EditText editTextSearchQuery = MainActivity.getMInstanceActivityContext().getEditTextSearchQuery();


     MyOnSwipeListener mainActivityGestures=new MyOnSwipeListener(mainActivityWeakReference.get()) {

        @Override
        public void onClick() {
            super.onClick();
            MyImageSetter imageSetter = new MyImageSetter(mainActivityWeakReference);
            imageSetter.setImage();
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
            editTextSearchQuery.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSwipeDown() {
            super.onSwipeDown();
            editTextSearchQuery.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onSwipeLeft() {
            super.onSwipeLeft();
            // your swipe left here.
        }

        @Override
        public void onSwipeRight() {
            super.onSwipeRight();
            // your swipe right here.
        }
    };
}
