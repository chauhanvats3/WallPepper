package com.dazedconfused.WallPepper;

class MyRuntimePreferences {
    static final String SHARED_PREFS = "sharedPrefs";
    static final String SWITCH_STATUS = "switchStatus";
    static final String INTERVALS = "pref_intervals";
    static final String NO_OF_JOBS_DONE="noOfJobsDone";
    static final String IS_JOB_GOING="isJobGoing";
    static final String QUALITY="pref_quality";
    static final String ORIENTATION="pref_orientation";
    static final String PHOTO_ID="photoID";
    static final String FEATURED="pref_featured";
    static final String SEARCH_QUERY="pref_search_query_new";
    static final String PREF_DURATION_SPINNER_POSITION = "durationSpinnerPosition";
    static final String PREF_ORIENTATION_SPINNER_POSITION = "orientationSpinnerPosition";
    static final String PREF_QUALITY_SPINNER_POSITION = "qualitySpinnerPosition";
    static final String PHOTO_HOTLINK = "photoHotlink";
    static final String PHOTO_DEFAULT_VALUE = "n/a";
    static final String PHOTO_CREATED_ON = "createdOn";
    static final String PHOTO_REG = "photoRegular";
    static final String PHOTO_FULL = "photoFull";
    static final String PHOTO_RAW = "photoRaw";
    static final String PHOTO_LINK = "photoLink";
    static final String PHOTO_COLOR = "photoColor";
    static final String PHOTO_COUNTRY = "photoCountry";
    static final String PHOTO_CITY = "photoCity";
    static final String PHOTO_UPLOADER_NAME = "photoUploaderName";
    static final String PHOTO_DOWNLOADS = "photoDownloads";
    static final String PHOTO_LIKES = "photoLikes";
    static final String PHOTO_CAMERA_MAKE = "photoCameraMake";
    static final String PHOTO_CAMERA_MODEL = "photoCameraModel";
    static final String PHOTO_SIZE = "photoSize";
    static final String PHOTO_FOCAL_LENGTH = "photoFocalLength";
    static final String PHOTO_APERTURE = "photoAperture";
    static final String PHOTO_EXPOSURE = "photoExposure";
    static final String PHOTO_ISO = "photoISO";
    static final String KEY_PREF_FEATURED = "pref_search_featured";
    static final String KEY_PREF_SEARCH_QUERY = "pref_search_query";
    static final String KEY_PREF_ORIENTATION = "pref_orientation";
    static final String KEY_PREF_SCHEDULER_SWITCH = "pref_scheduler_switch";
    static final String KEY_PREF_SCHEDULER_TIME = "pref_scheduler_time";
    static final String KEY_DEVICE_HEIGHT = "deviceHeight";
    static final String KEY_DEVICE_WIDTH = "deviceWidth";
    static final String PHOTO_DATA = "photoData";
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
