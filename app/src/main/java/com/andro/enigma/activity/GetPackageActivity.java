package com.andro.enigma.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andro.enigma.R;
import com.andro.enigma.adapter.ListAdapter;
import com.andro.enigma.database.*;
import com.andro.enigma.database.Package;
import com.andro.enigma.parser.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetPackageActivity extends Activity {

    private int packageId;
    private String packageTitle;
    private int type;
    private String lang;
    CrosswordDbHelper mDbHelper;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_package);

        packageId = Integer.parseInt(getIntent().getStringExtra("id"));
        packageTitle = getIntent().getStringExtra("title");
        lang = getIntent().getStringExtra("lang");
        type = Integer.parseInt(getIntent().getStringExtra("type"));

        mDbHelper = new CrosswordDbHelper(GetPackageActivity.this);

        TextView title = (TextView) findViewById(R.id.textView_package_title);


        button = (Button) findViewById(R.id.button_get_package);

        title.setText(packageTitle);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new JSONParse().execute();
            }
        });

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            button.setEnabled(false);
            button.setTextColor(888888);
            findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textView_package_done)).setText("Please wait!");
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JsonParser jsonParser = new JsonParser();

            JSONObject json = jsonParser.getJSONFromUrl("http://www.develop.enigma-aeiou.net/service/getenigmasforpackage?seckey=EnIgmAAEIOU&id=" + packageId + "&user=13");

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            ArrayList<Crossword> list = new ArrayList<>();
            Crossword enigma = null;

            JSONArray data = new JSONArray();

            try {
                data = result.getJSONArray("data");
            } catch (JSONException e) {
                Log.d("MYTAG", "" + e.getMessage());
            }

            Package p = new Package(""+packageId,packageTitle,lang,"","",""+type);

            mDbHelper.addPackage(p);

            for(int i = 0; i < data.length(); i++){
                try {
                    JSONObject obj = data.getJSONObject(i);
                    enigma = new Crossword(
                            Integer.parseInt(obj.getString("id_crw")),
                            i+1,
                            packageId,
                            obj.getString("words"),
                            "NO",
                            "0",
                            "en"
                    );
                }catch (JSONException ex){
                    Log.d("MYTAG", "JSONException " + ex.getMessage());
                }finally {
                    Log.d("MYTAG", "Insert ID " + mDbHelper.addCrossword(enigma));
                }
            }
            ((TextView) findViewById(R.id.textView_package_done)).setText("DONE!");
            findViewById(R.id.progressBar2).setVisibility(View.GONE);
        }
    }

}
