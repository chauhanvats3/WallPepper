package com.dazedconfused.WallPix;

class MyRuntimePreferences {
    private static final MyRuntimePreferences ourInstance = new MyRuntimePreferences();
    private static boolean imageSetStatus;

    private MyRuntimePreferences() {
        imageSetStatus = false;
    }

    static MyRuntimePreferences getInstance() {
        return ourInstance;
    }

    public static boolean isSettingImage() {
        return imageSetStatus;
    }

    public static void setImageSettingStatus(boolean value) {
        imageSetStatus = value;
    }
}
