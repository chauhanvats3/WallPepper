package com.dazedconfused.WallPix;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_PREF_FEATURED;
import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_PREF_ORIENTATION;
import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_PREF_SCHEDULER_SWITCH;
import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_PREF_SCHEDULER_TIME;
import static com.dazedconfused.WallPix.MyRuntimePreferences.KEY_PREF_SEARCH_QUERY;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG="SettingsFragment";
    private DrawerLayout drawerLayout;
    private WeakReference<SettingsActivity> settingsWeakReference;
private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;


@SuppressLint("ClickableViewAccessibility")
@Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.preferences);
        settingsWeakReference= SettingsActivity.getWeakReference();
        drawerLayout=settingsWeakReference.get().getDrawerLayout();
    MySettingsGestureResponses mySettingsGestureResponses = new MySettingsGestureResponses();

    drawerLayout.setOnTouchListener(mySettingsGestureResponses.settingsActivityGestures);
        onSharedPreferenceChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, String key) {
    switch (key){
        case KEY_PREF_FEATURED:
            break;
        case KEY_PREF_ORIENTATION:
            Preference orientationPref =findPreference(key);
            orientationPref.setSummary(sharedPreferences.getString(key,""));
            break;
        case KEY_PREF_SEARCH_QUERY:
            break;
        case KEY_PREF_SCHEDULER_SWITCH:
            break;
        case KEY_PREF_SCHEDULER_TIME:
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
