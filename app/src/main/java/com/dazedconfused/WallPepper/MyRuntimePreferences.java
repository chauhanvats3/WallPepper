package com.dazedconfused.WallPepper;

class MyRuntimePreferences {
    static final String SHARED_PREFS = "sharedPrefs";
    static final String PREF_SWITCH_STATUS = "switchStatus";
    static final String PREF_INTERVALS = "pref_intervals";
    static final String NO_OF_JOBS_DONE = "noOfJobsDone";
    static final String IS_JOB_GOING = "isJobGoing";
    static final String PREF_FEATURED = "pref_featured";
    static final String PREF_SEARCH_QUERY = "pref_search_query_new";
    static final String PREF_DURATION_SPINNER_POSITION = "durationSpinnerPosition";
    static final String PREF_WALLPAPER_CHECKED_RDBTN = "prefWallpaperQualityCheckedRDBTN";
    static final String PREF_DOWNLOAD_CHECKED_RDBTN = "prefDownloadQualityCheckedRDBTN";
    static final String PREF_ORIENTATION_CHECKED_RDBTN = "prefOrientationCheckedRDBTN";
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
    static final String KEY_DEVICE_HEIGHT = "deviceHeight";
    static final String KEY_DEVICE_WIDTH = "deviceWidth";
    static final String PHOTO_DATA = "photoData";
    static final String PHOTO_ID = "photoID";
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
