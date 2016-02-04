package com.andro.enigma.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.andro.enigma.R;
import com.andro.enigma.adapter.ListAdapter;
import com.andro.enigma.adapter.ListSelectPackageAdapter;
import com.andro.enigma.database.Package;
import com.andro.enigma.database.CrosswordDbHelper;
import com.andro.enigma.settings.MySettings;

import java.util.ArrayList;
import java.util.Locale;

public class SelectPackage extends Activity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_package);

        lv = (ListView) findViewById(R.id.listview_select_package);

        CrosswordDbHelper mDBHelper = new CrosswordDbHelper(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String locale;

        if(sharedPreferences.getString("listPref", "sr_RS").equalsIgnoreCase("sr_RS")){
            locale = "sr";
        }else{
            locale = "en";
        }

        ArrayList<Package> packageList = new ArrayList<>();
        Cursor c = mDBHelper.getAllPackages(locale);

        Package pack;
        if (c != null) {
            while(c.moveToNext()) {
                int packageId = c.getInt(0);
                String packageTitle = c.getString(1);
                String lang = c.getString(2);
                int type = c.getInt(3);
                String date = c.getString(4);
                int count = c.getInt(5);
                int solved = c.getInt(6);
                pack = new Package(packageId,packageTitle,lang,date,type,count,solved);
                packageList.add(pack);
            }
            c.close();
        }

        lv.setAdapter(new ListSelectPackageAdapter(this, R.layout.list_item, packageList));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocale();
    }

    public void setLocale() {
        SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(this);
        String languageToLoad  = shpref.getString("listPref", "sr_RS");
        Locale locale = new Locale(languageToLoad.split("_",2)[0],languageToLoad.split("_",2)[1]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        ActionBar actionBar = getActionBar();
        if (actionBar != null){
            actionBar.setTitle(getResources().getString(R.string.title_activity_select_package));
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent set = new Intent(SelectPackage.this, MySettings.class);
            startActivity(set);
        }
        else if(id == R.id.action_about){
            Intent ab = new Intent(SelectPackage.this, AboutActivity.class);
            startActivity(ab);
        }

        return super.onOptionsItemSelected(item);
    }

}
