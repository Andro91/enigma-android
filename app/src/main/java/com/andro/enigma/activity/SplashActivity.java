package com.andro.enigma.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.andro.enigma.R;
import com.andro.enigma.database.Crossword;
import com.andro.enigma.database.CrosswordDbHelper;

import java.util.Locale;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Thread thread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized (this){
                        wait(2000);
                    }
                }
                catch(InterruptedException ex){
                    ex.getMessage();
                }
                finally {
                    setLocale();
                    Intent i = new Intent(SplashActivity.this, MainMenuActivity.class);
                    startActivity(i);
                }
            }
        };
        Context context = getApplicationContext();
        CrosswordDbHelper mDbHelper = new CrosswordDbHelper(context);

        Crossword one = new Crossword(1,1,"MaKaBiiSiDoRLaKeJiaMaNeTNaRoViaRaMiS","NO","0", "sr");
        Crossword two = new Crossword(2,2,"KLaSiKRaSiNaaVaTaRToMaTaaRaRaTKiRiNe","NO","0", "sr");
        Crossword three = new Crossword(3,3,"PoSKoKoRLiNaTMuRaNKaLiTioRaNaCPiJeSa","NO","0", "sr");
        Crossword four = new Crossword(4,4,"VaSKRSeMeRiTNaJaVaSTaMeNKoTaRaiLiRaC","NO","0", "sr");
        Crossword five = new Crossword(5,5,"ZaKuPiiZaZoVMiLoJaSMiReNKuKaWeiToNaC","NO","0", "sr");
        Crossword six = new Crossword(6,6,"PaPKaRoTRoViTRePeTKoMiTioPoJaNPaRaNa","NO","0", "sr");
        Crossword seven = new Crossword(7,7,"BoRaKSoToLiTKaMaRaaKaDeMSaNaNaaRaRaT","NO","0", "sr");
        Crossword eight = new Crossword(8,8,"MaKaKiaNaTaSLiPaZaaMaNaTGuRaTiaSaSiN","NO","0", "sr");
        Crossword nine = new Crossword(9,9,"aLPaKaPaRDoNDuGaRaaTaLiKJaViTiKRoNiN","NO","0", "sr");
        Crossword ten = new Crossword(10,10,"KLaSaRRaRoZiaNaMiTNaGoJaKRoVaRLaNiCi","NO","0", "sr");

        Crossword one_en = new Crossword(11,1,"_ReaD_LaNCeTiNRuSHSCaMPiPiGeoN_DeNT_","NO","0", "en");
        Crossword two_en = new Crossword(12,2,"_PLuG_TRiPoDRoaDieoPiaTeTeSTeR_LeeR_","NO","0", "en");
        Crossword three_en = new Crossword(13,3,"_SLaG_THeSiSHaSSLeuPSiDeGeeZeR_DeeD_","NO","0", "en");
        Crossword four_en = new Crossword(14,4,"_DoSH_SeRieSaNNeaLSTaRVeHaTReD_LeaN_","NO","0", "en");
        Crossword five_en = new Crossword(15,5,"_TiNT_LiNeuPiNHeReSHaDeSPaLLeT_TeeN_","NO","0", "en");
        Crossword six_en = new Crossword(16,6,"_HeRS_CaViaRoRioLeMaNTiSaSCeNT_SeRe_","NO","0", "en");
        Crossword seven_en = new Crossword(17,7,"_aLSo_SNiPPYeNTReeReTiNaFaLTeR_LeeR_","NO","0", "en");
        Crossword eight_en = new Crossword(18,8,"_PLuS_TRiPuPRoaDieoPiaTeTeSTeR_ReeD_","NO","0", "en");
        Crossword nine_en = new Crossword(19,9,"_aPSe_GRouNDaRNiCaLeCToRaSHoRe_ToRe_","NO","0", "en");
        Crossword ten_en = new Crossword(20,10,"_LiSP_WaMPuMaMPeReDeiCeReNSiLe_THeY_","NO","0", "en");

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

        thread.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    public void setLocale() {
        SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(this);
        String languageToLoad  = shpref.getString("listPref", "sr_RS");
        Log.d("MYTAG", languageToLoad);
        Locale locale = new Locale(languageToLoad.split("_",2)[0],languageToLoad.split("_",2)[1]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
