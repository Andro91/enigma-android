package com.andro.enigma.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.andro.enigma.R;
import com.andro.enigma.adapter.ListAdapter;
import com.andro.enigma.parser.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.andro.enigma.database.Package;

public class PackageActivty extends Activity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_activty);


        lv = (ListView) findViewById(R.id.listView);

        new JSONParse().execute();

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

            JSONArray json = jsonParser.getJSONArrayFromUrl("http://www.develop.enigma-aeiou.net/service/getpackages?seckey=EnIgmAAEIOU&id=1&lang=en");

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
                    Log.d("MYTAG","Title = " + obj.getString("title"));
                    pack = new Package(obj.getString("id"),obj.getString("title"),obj.getString("lang"),obj.getString("date_created"),obj.getString("published"),obj.getString("id_type"));
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
