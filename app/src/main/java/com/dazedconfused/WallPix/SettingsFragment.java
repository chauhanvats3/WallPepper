package com.dazedconfused.WallPix;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_PREF_FEATURED;
import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_PREF_ORIENTATION;
import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_PREF_SEARCH_QUERY;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG="SettingsFragment";

    /*   public static final String KEY_PREF_FEATURED="pref_search_featured";
    public static final String KEY_PREF_SEARCH_QUERY="pref_search_query";
    public static final String KEY_PREF_ORIENTATION ="pref_orientation";*/
private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;


@Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        onSharedPreferenceChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    switch (key){
        case KEY_PREF_FEATURED:
            break;
        case KEY_PREF_ORIENTATION:
            Preference orientationPref =findPreference(key);
            orientationPref.setSummary(sharedPreferences.getString(key,""));
            break;
        case KEY_PREF_SEARCH_QUERY:
            break;
    }
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        Preference orientPref=findPreference(KEY_PREF_ORIENTATION);
        orientPref.setSummary(getPreferenceScreen().getSharedPreferences().getString(KEY_PREF_ORIENTATION," "));
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }
}
