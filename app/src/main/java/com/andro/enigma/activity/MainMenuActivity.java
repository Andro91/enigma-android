package com.andro.enigma.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.andro.enigma.R;
import com.andro.enigma.settings.MySettings;

import java.util.Locale;

public class MainMenuActivity extends Activity {

    Button free;
    Button paid25;
    Button paid50;
    Button settings;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        activity = this;

        //paid50 = (Button) findViewById(R.id.button_50);
        paid25 = (Button) findViewById(R.id.button_25);
        free = (Button) findViewById(R.id.button_free);
        settings = (Button) findViewById(R.id.button_settings);

        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, SelectPackage.class);
                i.putExtra("crosswordNumber",1);
                startActivity(i);
            }
        });

        paid25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, PackageActivity.class);
                startActivity(i);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, MySettings.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocale();
    }


    public void setLocale() {
        SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(this);
        String languageToLoad  = shpref.getString("listPref", "sr_RS");
        Locale locale;
        try {
            locale = new Locale(languageToLoad.split("_", 2)[0], languageToLoad.split("_", 2)[1]);
        }catch (ArrayIndexOutOfBoundsException arex){
            Log.d("MYTAG",arex.getMessage());
            locale = new Locale("en", "US");
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent set = new Intent(MainMenuActivity.this, MySettings.class);
            startActivity(set);
        }
        else if(id == R.id.action_about){
            Intent ab = new Intent(MainMenuActivity.this, AboutActivity.class);
            startActivity(ab);
        }

        return super.onOptionsItemSelected(item);
    }


}
