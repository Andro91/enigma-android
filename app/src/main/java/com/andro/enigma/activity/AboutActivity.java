package com.andro.enigma.activity;

/**
 * Created by andrija.karadzic on 05.08.2015.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.andro.enigma.R;
import com.andro.enigma.helper.Helper;
import com.andro.enigma.settings.MySettings;

import java.util.Locale;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //setLocale();

        Helper.inicActionBarUp(this, getResources().getString(R.string.title_activity_about));

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView tv = (TextView) findViewById(R.id.textVersion);
            tv.setText(versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }



	@Override
	protected void onResume() {
		super.onResume();
		setLocale();
	}



    public void setLocale() {
        boolean languageChanged = false;
        SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(this);
        String languageToLoad  = shpref.getString("listPref", "sr_RS");
        Locale locale;
        try {
            locale = new Locale(languageToLoad.split("_", 2)[0], languageToLoad.split("_", 2)[1]);
        }catch (ArrayIndexOutOfBoundsException arex){
            Log.d("MYTAG",arex.getMessage());
            locale = new Locale("en", "US");
        }
        Log.d("languageToLoad", languageToLoad);
        Log.d("default", Locale.getDefault().toString());
        if (!languageToLoad.equals(Locale.getDefault().toString())){
            languageChanged = true;
        }
        else{
            return;
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        Helper.inicActionBarUp(this, getResources().getString(R.string.title_activity_about));
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings){
            Intent set = new Intent(AboutActivity.this, MySettings.class);
            startActivity(set);
        }else if(android.R.id.home == id) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

