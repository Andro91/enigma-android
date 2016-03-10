package com.andro.enigma.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.andro.enigma.R;
import com.andro.enigma.adapter.ListSelectPackageAdapter;
import com.andro.enigma.database.Package;
import com.andro.enigma.database.CrosswordDbHelper;
import com.andro.enigma.helper.Helper;
import com.andro.enigma.settings.MySettings;

import java.util.ArrayList;
import java.util.Locale;

public class SelectPackage extends AppCompatActivity {

    ListView lv;
    ProgressBar progressBar;
    private String lang = "en";
    private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_package);

        lv = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        RadioGroup languageGroup = (RadioGroup) findViewById(R.id.radio_group_lang);
        RadioGroup typeGroup = (RadioGroup) findViewById(R.id.radio_group_type);

        for (int i = 0; i < Helper.typeList.size(); i++) {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(Helper.typeList.get(i).id);
            rdbtn.setText(Helper.typeList.get(i).name);
            rdbtn.setTextColor(Color.parseColor("#FFFFFF"));
            ((ViewGroup) findViewById(R.id.radio_group_type)).addView(rdbtn);
            if(i == 0 && Helper.typeList.size() != 0){ typeGroup.check(rdbtn.getId()); }
        }

        languageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Log.d("chk", "id" + checkedId);
                if (checkedId == R.id.radio_english) {
                    lang = "en";
                } else if (checkedId == R.id.radio_serbian) {
                    lang = "sr";
                }
                refreshList(type,lang);
            }
        });

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Log.d("chk", "id" + checkedId);
                type = checkedId;
                refreshList(type,lang);
            }
        });

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#5087E1"), PorterDuff.Mode.MULTIPLY);

        languageGroup.check(R.id.radio_english);

        Helper.inicActionBarDrawer(this,getResources().getString(R.string.title_activity_select_package));

    }

    private void refreshList(int type, String lang){
        lv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        CrosswordDbHelper mDBHelper = new CrosswordDbHelper(this);
        ArrayList<Package> packageList = new ArrayList<>();
        Cursor c = mDBHelper.getAllPackages(lang,type);

        Package pack;
        if (c != null) {
            while(c.moveToNext()) {
                int packageId = c.getInt(0);
                String packageTitle = c.getString(1);
                String language = c.getString(2);
                int packageType = c.getInt(3);
                String date = c.getString(4);
                int count = c.getInt(5);
                int solved = c.getInt(6);
                int purchased = c.getInt(7);
                double price = c.getDouble(8);
                pack = new Package(packageId,packageTitle,language,date,packageType,count,solved,purchased,price);
                packageList.add(pack);
            }
            c.close();
        }
        lv.setAdapter(new ListSelectPackageAdapter(this, R.layout.list_item, packageList));
        lv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        Helper.inicActionBarDrawer(this,getResources().getString(R.string.title_activity_select_package));
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
