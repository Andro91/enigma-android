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
                        sleep(1500);
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

            //Creating free enigma packages
//            Crossword one = new Crossword(Integer.MAX_VALUE,1,Integer.MAX_VALUE,"MAKABiiSiDoRLaKeJiaMaNeTNaRoViaRaMiS","NO","0", "sr");
//            Crossword two = new Crossword(Integer.MAX_VALUE-1,2,Integer.MAX_VALUE,"KLaSiKRaSiNaaVaTaRToMaTaaRaRaTKiRiNe","NO","0", "sr");
//            Crossword three = new Crossword(Integer.MAX_VALUE-2,3,Integer.MAX_VALUE,"PoSKoKoRLiNaTMuRaNKaLiTioRaNaCPiJeSa","NO","0", "sr");
//            Crossword four = new Crossword(Integer.MAX_VALUE-3,4,Integer.MAX_VALUE,"VaSKRSeMeRiTNaJaVaSTaMeNKoTaRaiLiRaC","NO","0", "sr");
//            Crossword five = new Crossword(Integer.MAX_VALUE-4,5,Integer.MAX_VALUE,"ZaKuPiiZaZoVMiLoJaSMiReNKuKaWeiToNaC","NO","0", "sr");
//            Crossword six = new Crossword(Integer.MAX_VALUE-5,6,Integer.MAX_VALUE,"PaPKaRoTRoViTRePeTKoMiTioPoJaNPaRaNa","NO","0", "sr");
//            Crossword seven = new Crossword(Integer.MAX_VALUE-6,7,Integer.MAX_VALUE,"BoRaKSoToLiTKaMaRaaKaDeMSaNaNaaRaRaT","NO","0", "sr");
//            Crossword eight = new Crossword(Integer.MAX_VALUE-7,8,Integer.MAX_VALUE,"MaKaKiaNaTaSLiPaZaaMaNaTGuRaTiaSaSiN","NO","0", "sr");
//            Crossword nine = new Crossword(Integer.MAX_VALUE-8,9,Integer.MAX_VALUE,"aLPaKaPaRDoNDuGaRaaTaLiKJaViTiKRoNiN","NO","0", "sr");
//            Crossword ten = new Crossword(Integer.MAX_VALUE-9,10,Integer.MAX_VALUE,"KLaSaRRaRoZiaNaMiTNaGoJaKRoVaRLaNiCi","NO","0", "sr");
//
//            Crossword one_en = new Crossword(Integer.MAX_VALUE-11,1,Integer.MAX_VALUE-1,"_ReaD_LaNCeTiNRuSHSCaMPiPiGeoN_DeNT_","NO","0", "en");
//            Crossword two_en = new Crossword(Integer.MAX_VALUE-12,2,Integer.MAX_VALUE-1,"_PLuG_TRiPoDRoaDieoPiaTeTeSTeR_LeeR_","NO","0", "en");
//            Crossword three_en = new Crossword(Integer.MAX_VALUE-13,3,Integer.MAX_VALUE-1,"_SLaG_THeSiSHaSSLeuPSiDeGeeZeR_DeeD_","NO","0", "en");
//            Crossword four_en = new Crossword(Integer.MAX_VALUE-14,4,Integer.MAX_VALUE-1,"_DoSH_SeRieSaNNeaLSTaRVeHaTReD_LeaN_","NO","0", "en");
//            Crossword five_en = new Crossword(Integer.MAX_VALUE-15,5,Integer.MAX_VALUE-1,"_TiNT_LiNeuPiNHeReSHaDeSPaLLeT_TeeN_","NO","0", "en");
//            Crossword six_en = new Crossword(Integer.MAX_VALUE-16,6,Integer.MAX_VALUE-1,"_HeRS_CaViaRoRioLeMaNTiSaSCeNT_SeRe_","NO","0", "en");
//            Crossword seven_en = new Crossword(Integer.MAX_VALUE-17,7,Integer.MAX_VALUE-1,"_aLSo_SNiPPYeNTReeReTiNaFaLTeR_LeeR_","NO","0", "en");
//            Crossword eight_en = new Crossword(Integer.MAX_VALUE-18,8,Integer.MAX_VALUE-1,"_PLuS_TRiPuPRoaDieoPiaTeTeSTeR_ReeD_","NO","0", "en");
//            Crossword nine_en = new Crossword(Integer.MAX_VALUE-19,9,Integer.MAX_VALUE-1,"_aPSe_GRouNDaRNiCaLeCToRaSHoRe_ToRe_","NO","0", "en");
//            Crossword ten_en = new Crossword(Integer.MAX_VALUE-20,10,Integer.MAX_VALUE-1,"_LiSP_WaMPuMaMPeReDeiCeReNSiLe_THeY_","NO","0", "en");

            Crossword one = new Crossword(Integer.MAX_VALUE,1,Integer.MAX_VALUE,"MAKABIISIDORLAKEJIAMANETNAROVIARAMIS","NO","0", "sr");
            Crossword two = new Crossword(Integer.MAX_VALUE-1,2,Integer.MAX_VALUE,"KLASIKRASINAAVATARTOMATAARARATKIRINE","NO","0", "sr");
            Crossword three = new Crossword(Integer.MAX_VALUE-2,3,Integer.MAX_VALUE,"POSKOKORLINATMURANKALITIORANACPIJESA","NO","0", "sr");
            Crossword four = new Crossword(Integer.MAX_VALUE-3,4,Integer.MAX_VALUE,"VASKRSEMERITNAJAVASTAMENKOTARAILIRAC","NO","0", "sr");
            Crossword five = new Crossword(Integer.MAX_VALUE-4,5,Integer.MAX_VALUE,"ZAKUPIIZAZOVMILOJASMIRENKUKAWEITONAC","NO","0", "sr");
            Crossword six = new Crossword(Integer.MAX_VALUE-5,6,Integer.MAX_VALUE,"PAPKAROTROVITREPETKOMITIOPOJANPARANA","NO","0", "sr");
            Crossword seven = new Crossword(Integer.MAX_VALUE-6,7,Integer.MAX_VALUE,"BORAKSOTOLITKAMARAAKADEMSANANAARARAT","NO","0", "sr");
            Crossword eight = new Crossword(Integer.MAX_VALUE-7,8,Integer.MAX_VALUE,"MAKAKIANATASLIPAZAAMANATGURATIASASIN","NO","0", "sr");
            Crossword nine = new Crossword(Integer.MAX_VALUE-8,9,Integer.MAX_VALUE,"ALPAKAPARDONDUGARAATALIKJAVITIKRONIN","NO","0", "sr");
            Crossword ten = new Crossword(Integer.MAX_VALUE-9,10,Integer.MAX_VALUE,"KLASARRAROZIANAMITNAGOJAKROVARLANICI","NO","0", "sr");

            Crossword one_en = new Crossword(Integer.MAX_VALUE-11,1,Integer.MAX_VALUE-1,"_READ_LANCETINRUSHSCAMPIPIGEON_DENT_","NO","0", "en");
            Crossword two_en = new Crossword(Integer.MAX_VALUE-12,2,Integer.MAX_VALUE-1,"_PLUG_TRIPODROADIEOPIATETESTER_LEER_","NO","0", "en");
            Crossword three_en = new Crossword(Integer.MAX_VALUE-13,3,Integer.MAX_VALUE-1,"_SLAG_THESISHASSLEUPSIDEGEEZER_DEED_","NO","0", "en");
            Crossword four_en = new Crossword(Integer.MAX_VALUE-14,4,Integer.MAX_VALUE-1,"_DOSH_SERIESANNEALSTARVEHATRED_LEAN_","NO","0", "en");
            Crossword five_en = new Crossword(Integer.MAX_VALUE-15,5,Integer.MAX_VALUE-1,"_TINT_LINEUPINHERESHADESPALLET_TEEN_","NO","0", "en");
            Crossword six_en = new Crossword(Integer.MAX_VALUE-16,6,Integer.MAX_VALUE-1,"_HERS_CAVIARORIOLEMANTISASCENT_SERE_","NO","0", "en");
            Crossword seven_en = new Crossword(Integer.MAX_VALUE-17,7,Integer.MAX_VALUE-1,"_ALSO_SNIPPYENTREERETINAFALTER_LEER_","NO","0", "en");
            Crossword eight_en = new Crossword(Integer.MAX_VALUE-18,8,Integer.MAX_VALUE-1,"_PLUS_TRIPUPROADIEOPIATETESTER_REED_","NO","0", "en");
            Crossword nine_en = new Crossword(Integer.MAX_VALUE-19,9,Integer.MAX_VALUE-1,"_APSE_GROUNDARNICALECTORASHORE_TORE_","NO","0", "en");
            Crossword ten_en = new Crossword(Integer.MAX_VALUE-20,10,Integer.MAX_VALUE-1,"_LISP_WAMPUMAMPEREDEICERENSILE_THEY_","NO","0", "en");

    //        UNCOMMENT TO DELETE DATABASE RECORDS
    //        CrosswordDbHelper dbh = new CrosswordDbHelper(this);
    //        SQLiteDatabase db = dbh.getWritableDatabase();
    //        for (int i = 1; i <=20; i++){
    //            Log.d("DELETE", String.valueOf(i));
    //            db.delete(CrosswordContract.CrosswordEntry.TABLE_NAME, CrosswordContract.CrosswordEntry.COLUMN_NAME_ID + " = ?", new String[] {String.valueOf(i)});
    //        }
    //        db.close();

            //Adding the serbian enigma/crosswords
            Package p1 = new Package(Integer.MAX_VALUE,"Besplatni srpski","sr","2016-02-29",1,10);
            mDbHelper.addPackage(p1);
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
            Package p2 = new Package(Integer.MAX_VALUE-1,"Free english","en","2016-02-29",1,10);
            mDbHelper.addPackage(p2);
            mDbHelper.addCrossword(one_en);
            mDbHelper.addCrossword(two_en);
            mDbHelper.addCrossword(three_en);
            mDbHelper.addCrossword(four_en);
            mDbHelper.addCrossword(five_en);
            mDbHelper.addCrossword(six_en);
            mDbHelper.addCrossword(seven_en);
            mDbHelper.addCrossword(eight_en);
            mDbHelper.addCrossword(nine_en);
            mDbHelper.addCrossword(ten_en);
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
