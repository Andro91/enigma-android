package com.andro.enigma.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
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
import com.andro.enigma.adapter.ListAdapter;
import com.andro.enigma.database.Package;
import com.andro.enigma.helper.Helper;
import com.andro.enigma.parser.JsonParser;
import com.andro.enigma.settings.MySettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class PackageActivity extends AppCompatActivity {

    private ListView lv;
    private String lang;
    private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_activty);

        Helper.inicActionBarDrawer(this, getResources().getString(R.string.title_activity_package_activty));

        RadioGroup languageGroup = (RadioGroup) findViewById(R.id.radio_group_lang);
        RadioGroup typeGroup = (RadioGroup) findViewById(R.id.radio_group_type);

        for (int i = 0; i < Helper.typeList.size(); i++) {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(Helper.typeList.get(i).id);
            rdbtn.setText(Helper.typeList.get(i).name);
            rdbtn.setTextColor(Color.parseColor("#FFFFFF"));
            ((ViewGroup) findViewById(R.id.radio_group_type)).addView(rdbtn);
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#5087E1"), PorterDuff.Mode.MULTIPLY);

        languageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Log.d("chk", "id" + checkedId);
                if (checkedId == R.id.radio_english) {
                    lang = "en";
                } else if (checkedId == R.id.radio_serbian) {
                    lang = "sr";
                }
                new JSONParse().execute();
            }
        });

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Log.d("chk", "id" + checkedId);
                type = checkedId;
                new JSONParse().execute();
            }
        });

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
        String languageToLoad  = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("listPref", "sr_RS");
        if(lang == null) {
            lang = languageToLoad.split("_", 2)[0];
        }
        try {
            type = Helper.typeList.get(0).id;
        }catch (Exception ex){
            //la la
        }
        lv = (ListView) findViewById(R.id.listView);
        new JSONParse().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
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
        Helper.inicActionBarDrawer(this,getResources().getString(R.string.title_activity_package_activty));
        invalidateOptionsMenu();
        lang = languageToLoad.split("_",2)[0];
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
            Intent set = new Intent(PackageActivity.this, MySettings.class);
            startActivity(set);
        }
        else if(id == R.id.action_about){
            Intent ab = new Intent(PackageActivity.this, AboutActivity.class);
            startActivity(ab);
        }
        else if(android.R.id.home == id) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class JSONParse extends AsyncTask<String, String, JSONArray> {
        @Override
        protected void onPreExecute() {
            findViewById(R.id.listView).setVisibility(View.GONE);
            findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            JsonParser jsonParser = new JsonParser();
            Log.d("MYTAG","Lang = " + lang);
            JSONArray json = jsonParser.getJSONArrayFromUrl(Helper.HOME_URL + "/service/getpackages?seckey=EnIgmAAEIOU&id_type=" + type + "&lang=" + lang + "&user_id=1");
            Log.d("MYTAG", "URL = " + Helper.HOME_URL + "/service/getpackages?seckey=EnIgmAAEIOU&id_type=" + type + "&lang=" + lang + "&user_id=1");
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            ArrayList<Package> list = new ArrayList<>();
            Package pack = null;

            Log.d("MYTAG",""+result.length());

            for(int i = 0; i < result.length(); i++){

                try {
                    JSONObject obj = result.getJSONObject(i);
                    Log.d("MYTAG", "Title = " + obj.getString("title"));

                    int id = Integer.parseInt(obj.getString("id"));
                    String title = obj.getString("title");
                    String lang = obj.getString("lang");
                    String dateCreated = obj.getString("date_created");
                    int idType = Integer.parseInt(obj.getString("id_type"));
                    int enigmaCount = 0;
                    for (int j = 0; j < Helper.typeList.size(); j++){
                        if(Helper.typeList.get(j).id == idType){
                            enigmaCount = Helper.typeList.get(j).size;
                        }
                    }
                    int solvedCount = 0;
                    int purchased = Integer.parseInt(obj.getString("buyed_status"));
                    double price = Double.parseDouble(obj.getString("price"));

                    pack = new Package(id,title,lang,dateCreated,idType, enigmaCount, solvedCount, purchased, price);

                }catch (JSONException ex){
                    Log.d("MYTAG", "JSONException " + ex.getMessage());
                }finally {
                    list.add(pack);
                    Log.d("MYTAG", "List items = " + list.size());
                }
            }

            lv.setAdapter(new ListAdapter(PackageActivity.this, R.layout.list_item, list, type));
            findViewById(R.id.listView).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar1).setVisibility(View.GONE);
        }
    }

}
