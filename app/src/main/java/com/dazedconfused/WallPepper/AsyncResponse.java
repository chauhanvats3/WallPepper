package com.dazedconfused.WallPepper;

import android.graphics.Bitmap;

//Interface is for passing the downloaded image to the image view in main thread
interface AsyncResponse {
    void processFinish(Bitmap image);
}