package com.andro.enigma.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.andro.enigma.R;
import com.andro.enigma.database.*;
import com.andro.enigma.database.Package;
import com.andro.enigma.helper.Helper;
import com.andro.enigma.parser.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstTime = sharedPreferences.getBoolean("firstTime",true);

        Thread thread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized (this){
                        wait(1500);
                    }
                }
                catch(InterruptedException ex){
                    ex.getMessage();
                }
            }
        };

        if(firstTime){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime", false);
            editor.apply();


            Context context = getApplicationContext();
            CrosswordDbHelper mDbHelper = new CrosswordDbHelper(context);

            Crossword one = new Crossword(501,1,0,"MaKaBiiSiDoRLaKeJiaMaNeTNaRoViaRaMiS","NO","0", "sr");
            Crossword two = new Crossword(502,2,0,"KLaSiKRaSiNaaVaTaRToMaTaaRaRaTKiRiNe","NO","0", "sr");
            Crossword three = new Crossword(503,3,0,"PoSKoKoRLiNaTMuRaNKaLiTioRaNaCPiJeSa","NO","0", "sr");
            Crossword four = new Crossword(504,4,0,"VaSKRSeMeRiTNaJaVaSTaMeNKoTaRaiLiRaC","NO","0", "sr");
            Crossword five = new Crossword(505,5,0,"ZaKuPiiZaZoVMiLoJaSMiReNKuKaWeiToNaC","NO","0", "sr");
            Crossword six = new Crossword(506,6,0,"PaPKaRoTRoViTRePeTKoMiTioPoJaNPaRaNa","NO","0", "sr");
            Crossword seven = new Crossword(507,7,0,"BoRaKSoToLiTKaMaRaaKaDeMSaNaNaaRaRaT","NO","0", "sr");
            Crossword eight = new Crossword(508,8,0,"MaKaKiaNaTaSLiPaZaaMaNaTGuRaTiaSaSiN","NO","0", "sr");
            Crossword nine = new Crossword(509,9,0,"aLPaKaPaRDoNDuGaRaaTaLiKJaViTiKRoNiN","NO","0", "sr");
            Crossword ten = new Crossword(510,10,0,"KLaSaRRaRoZiaNaMiTNaGoJaKRoVaRLaNiCi","NO","0", "sr");

    //        Crossword one_en = new Crossword(11,1,"_ReaD_LaNCeTiNRuSHSCaMPiPiGeoN_DeNT_","NO","0", "en");
    //        Crossword two_en = new Crossword(12,2,"_PLuG_TRiPoDRoaDieoPiaTeTeSTeR_LeeR_","NO","0", "en");
    //        Crossword three_en = new Crossword(13,3,"_SLaG_THeSiSHaSSLeuPSiDeGeeZeR_DeeD_","NO","0", "en");
    //        Crossword four_en = new Crossword(14,4,"_DoSH_SeRieSaNNeaLSTaRVeHaTReD_LeaN_","NO","0", "en");
    //        Crossword five_en = new Crossword(15,5,"_TiNT_LiNeuPiNHeReSHaDeSPaLLeT_TeeN_","NO","0", "en");
    //        Crossword six_en = new Crossword(16,6,"_HeRS_CaViaRoRioLeMaNTiSaSCeNT_SeRe_","NO","0", "en");
    //        Crossword seven_en = new Crossword(17,7,"_aLSo_SNiPPYeNTReeReTiNaFaLTeR_LeeR_","NO","0", "en");
    //        Crossword eight_en = new Crossword(18,8,"_PLuS_TRiPuPRoaDieoPiaTeTeSTeR_ReeD_","NO","0", "en");
    //        Crossword nine_en = new Crossword(19,9,"_aPSe_GRouNDaRNiCaLeCToRaSHoRe_ToRe_","NO","0", "en");
    //        Crossword ten_en = new Crossword(20,10,"_LiSP_WaMPuMaMPeReDeiCeReNSiLe_THeY_","NO","0", "en");

    //        UNCOMMENT TO DELETE DATABASE RECORDS
    //        CrosswordDbHelper dbh = new CrosswordDbHelper(this);
    //        SQLiteDatabase db = dbh.getWritableDatabase();
    //        for (int i = 1; i <=20; i++){
    //            Log.d("DELETE", String.valueOf(i));
    //            db.delete(CrosswordContract.CrosswordEntry.TABLE_NAME, CrosswordContract.CrosswordEntry.COLUMN_NAME_ID + " = ?", new String[] {String.valueOf(i)});
    //        }
    //        db.close();

            //Adding the serbian enigma/crosswords
            mDbHelper.addCrossword(one);
            mDbHelper.addCrossword(two);
            mDbHelper.addCrossword(three);
            mDbHelper.addCrossword(four);
            mDbHelper.addCrossword(five);
            mDbHelper.addCrossword(six);
            mDbHelper.addCrossword(seven);
            mDbHelper.addCrossword(eight);
            mDbHelper.addCrossword(nine);
            mDbHelper.addCrossword(ten);


            //Adding the english_US enigma/crosswords
    //        mDbHelper.addCrossword(one_en);
    //        mDbHelper.addCrossword(two_en);
    //        mDbHelper.addCrossword(three_en);
    //        mDbHelper.addCrossword(four_en);
    //        mDbHelper.addCrossword(five_en);
    //        mDbHelper.addCrossword(six_en);
    //        mDbHelper.addCrossword(seven_en);
    //        mDbHelper.addCrossword(eight_en);
    //        mDbHelper.addCrossword(nine_en);
    //        mDbHelper.addCrossword(ten_en);
        }

       // thread.start();

        new JSONParse().execute();

        setLocale();
        Intent i = new Intent(SplashActivity.this, MainMenuActivity.class);
        startActivity(i);
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
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
    }

    private class JSONParse extends AsyncTask<String, String, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            JsonParser jsonParser = new JsonParser();
            return jsonParser.getJSONArrayFromUrl(Helper.HOME_URL + Helper.PACKAGE_TYPES_URL);
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            List<PackageType> typeList = new ArrayList<>();
            for (int i = 0; i < result.length(); i++){
                JSONObject jo;
                PackageType packageType = null;
                try {
                    jo = result.getJSONObject(i);
                    packageType = new PackageType(jo.getInt("id"), jo.getString("name"), jo.getInt("size"), jo.getDouble("price"));
                    Log.d("MYTAG",jo.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    typeList.add(i,packageType);
                }
            }
            Helper.typeList = typeList;

        }
    }
}
