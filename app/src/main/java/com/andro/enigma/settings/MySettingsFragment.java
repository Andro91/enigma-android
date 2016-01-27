package com.andro.enigma.settings;

/**
 * Created by andrija.karadzic on 05.08.2015.
 */

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.andro.enigma.R;
import com.andro.enigma.activity.MainMenuActivity;

import java.util.Locale;

public class MySettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.my_settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String languageToLoad  = sharedPreference.getString("listPref", "sr_RS");
        Locale locale = new Locale(languageToLoad.split("_",2)[0],languageToLoad.split("_",2)[1]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        getActivity().invalidateOptionsMenu();
        getActivity().recreate();
        MainMenuActivity.activity.recreate();
    }



}