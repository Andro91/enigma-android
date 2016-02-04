package com.andro.enigma.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.andro.enigma.R;
import com.andro.enigma.adapter.ListAdapter;
import com.andro.enigma.parser.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import com.andro.enigma.database.Package;
import com.andro.enigma.settings.MySettings;

public class PackageActivty extends Activity {

    private ListView lv;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_activty);

//        String languageToLoad  = PreferenceManager.getDefaultSharedPreferences(this)
//                .getString("listPref", "sr_RS");
//        lang = languageToLoad.split("_",2)[0];
//        lv = (ListView) findViewById(R.id.listView);
//
//        new JSONParse().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocale();
        String languageToLoad  = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("listPref", "sr_RS");
        lang = languageToLoad.split("_",2)[0];
        lv = (ListView) findViewById(R.id.listView);
        new JSONParse().execute();
        //lv.invalidate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

    public void setLocale() {
        SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(this);
        String languageToLoad  = shpref.getString("listPref", "sr_RS");
        Log.d("MYTAG","LangToLoad = " + languageToLoad);
        Locale locale = new Locale(languageToLoad.split("_",2)[0],languageToLoad.split("_",2)[1]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        ActionBar actionBar = getActionBar();
        if (actionBar != null){
            actionBar.setTitle(getResources().getString(R.string.title_activity_package_activty));
        }
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
            Intent set = new Intent(PackageActivty.this, MySettings.class);
            startActivity(set);
        }
        else if(id == R.id.action_about){
            Intent ab = new Intent(PackageActivty.this, AboutActivity.class);
            startActivity(ab);
        }

        return super.onOptionsItemSelected(item);
    }

    private class JSONParse extends AsyncTask<String, String, JSONArray> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            JsonParser jsonParser = new JsonParser();
            Log.d("MYTAG","Lang = " + lang);
            JSONArray json = jsonParser.getJSONArrayFromUrl("http://www.develop.enigma-aeiou.net/service/getpackages?seckey=EnIgmAAEIOU&id=1&lang=" + lang);

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
                    int enigmaCount = 10;
                    int solvedCount = 0;

                    pack = new Package(id,title,lang,dateCreated,idType, enigmaCount, solvedCount);

                }catch (JSONException ex){
                    Log.d("MYTAG", "JSONException " + ex.getMessage());
                }finally {
                    list.add(pack);
                    Log.d("MYTAG", "List items = " + list.size());
                }
            }

            lv.setAdapter(new ListAdapter(PackageActivty.this, R.layout.list_item, list));
        }
    }

}
