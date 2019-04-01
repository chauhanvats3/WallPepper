package com.dazedconfused.WallPix;

class MyRuntimePreferences {
    public static final String KEY_PREF_FEATURED = "pref_search_featured";
    public static final String KEY_PREF_SEARCH_QUERY = "pref_search_query";
    public static final String KEY_PREF_ORIENTATION = "pref_orientation";
    public static final String KEY_PREF_SCHEDULER_SWITCH = "pref_scheduler_switch";
    public static final String KEY_PREF_SCHEDULER_TIME = "pref_scheduler_time";
    private static final String TAG = "MyRuntimePref";
    private static final MyRuntimePreferences ourInstance = new MyRuntimePreferences();
    private static boolean imageSetStatus = false;

    private MyRuntimePreferences() {
        imageSetStatus = false;
    }

    static MyRuntimePreferences getInstance() {
        return ourInstance;
    }

    static boolean isSettingImage() {
        return imageSetStatus;
    }

    static void setImageSettingStatus(boolean value) {
        imageSetStatus = value;
    }
}
