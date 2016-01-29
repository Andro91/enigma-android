package com.andro.enigma.activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.widget.ListView;

import com.andro.enigma.R;
import com.andro.enigma.adapter.ListAdapter;
import com.andro.enigma.adapter.ListSelectPackageAdapter;
import com.andro.enigma.database.Package;
import com.andro.enigma.database.CrosswordDbHelper;

import java.util.ArrayList;

public class SelectPackage extends Activity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_package);

        lv = (ListView) findViewById(R.id.listview_select_package);

        CrosswordDbHelper mDBHelper = new CrosswordDbHelper(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String locale = null;

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
                int type = c.getInt(1);
                String lang = c.getString(2);
                String packageTitle = c.getString(3);
                pack = new Package(""+packageId,packageTitle,lang,"","",""+type);
                packageList.add(pack);
            }
            c.close();
        }

        lv.setAdapter(new ListSelectPackageAdapter(this,R.layout.list_item,packageList));
    }

}
