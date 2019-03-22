package com.dazedconfused.WallPix;

class MyRuntimePreferences {
    private static final String TAG="MyRuntimePref";

    private static final MyRuntimePreferences ourInstance = new MyRuntimePreferences();
    public static final String KEY_PREF_FEATURED="pref_search_featured";
    public static final String KEY_PREF_SEARCH_QUERY="pref_search_query";
    public static final String KEY_PREF_ORIENTATION ="pref_orientation";
    private static boolean imageSetStatus=false;
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
