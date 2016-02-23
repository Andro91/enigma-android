package com.andro.enigma.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.andro.enigma.R;
import com.andro.enigma.database.PackageType;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Internet on 28-Jan-16.
 */
public class Helper {

    public static final String HOME_URL = "http://www.develop.enigma-aeiou.net";
    public static final String PACKAGE_TYPES_URL = "/service/getpackagetypes?seckey=EnIgmAAEIOU";
    public static final String REGISTER_URL = "/register-user";
    public static final int REQUEST_LOGIN_CODE = 123;


    //PayPal configuration

    public static final String CONFIG_CLIENT_ID = "Adon5OEd562P342BhmJnXmStn7HTJadYXpVVwtPSH2cWVJllA-zl0JIOeltfGx_JfKRbeCjupUHRFd17";
    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration()
        .environment(CONFIG_ENVIRONMENT)
        .clientId(CONFIG_CLIENT_ID)
        .merchantName("Enigma Store")
        .merchantPrivacyPolicyUri(
                Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));


    public static List<PackageType> typeList;

    public  HashMap<String, String> charMap;

    public Helper() {
        charMap = new HashMap<>();
        charMap.put("W","NJ");
        charMap.put("Q","LJ");
        charMap.put("X","Dž");
        charMap.put("^","Č");
        charMap.put("[","Š");
        charMap.put("]","Ć");
        charMap.put("@","Ž");
        charMap.put("\\","Đ");
    }

    public static boolean contains(final int[] array, final int key) {
        for (final int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }
    public static String getLocale(Context c) {
        if (PreferenceManager.getDefaultSharedPreferences(c).getString("listPref", "sr_RS").equalsIgnoreCase("sr_RS")) {
            return "sr";
        } else {
            return "en";
        }
    }

    public static void inicActionBarDrawer(Context context, String title) {
        try{
            AppCompatActivity activity = ((AppCompatActivity) context);
            Toolbar toolbar = (Toolbar) ((AppCompatActivity) context).findViewById(R.id.toolbar);
            TextView toolbarTitle = ((TextView) toolbar.findViewById(R.id.toolbar_title));
            ActionBarDrawerToggle actionBarDrawerToggle;
            DrawerLayout drawerLayout;
            drawerLayout = (DrawerLayout) activity.findViewById(R.id.navigation_drawer);

            toolbarTitle.setText(title);
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();

            actionBarDrawerToggle = new ActionBarDrawerToggle(activity,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
            drawerLayout.setDrawerListener(actionBarDrawerToggle);

            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white);

        }catch(Exception ex){
            Log.d("MYERROR", "ActionBar error: " + ex.getMessage());
        }
    }

    public static void inicActionBarUp(Context context, String title) {
        try{
            Toolbar toolbar = null;
            if(((AppCompatActivity) context).findViewById(R.id.toolbar) != null){
                toolbar = (Toolbar) ((AppCompatActivity) context).findViewById(R.id.toolbar);
                ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText(title);
            }else{
                Log.d("MYTAG", "Null toolbar");
            }
            ((AppCompatActivity) context).setSupportActionBar(toolbar);
            ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) context).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch(Exception ex){
            Log.d("MYERROR", "ActionBar error: " + ex.getMessage());
        }
    }

    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0 || string.equals("null"))
            return true;

        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!Character.isWhitespace(string.codePointAt(i)))
                return false;
        }
        return true;
    }


    public static String md5(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean checkLogin(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("Enigma user", Context.MODE_PRIVATE);
        if(sharedpreferences.getString("userId", null) == null){
            return false;
        }else{
            return true;
        }
    }

}
