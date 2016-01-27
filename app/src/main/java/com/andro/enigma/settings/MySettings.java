package com.andro.enigma.settings;

/**
 * Created by andrija.karadzic on 05.08.2015.
 */
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.andro.enigma.R;

public class MySettings extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            addPreferencesFromResource(R.xml.my_settings);
        }else{
            getFragmentManager().beginTransaction().replace(android.R.id.content,new MySettingsFragment()).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
