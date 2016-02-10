package com.andro.enigma.helper;

import android.content.Context;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.HashMap;

/**
 * Created by Internet on 28-Jan-16.
 */
public class Helper {

    public static final String HOME_URL = "http://www.develop.enigma-aeiou.net";

    //PayPal configuration

    public static final String CONFIG_CLIENT_ID = "AT99fIs7li4ccLVrKjPCRxy3AJrbIi8jIZK9aDSCJb8pGwWPKsVbFEDnUkPDtUbsW6hZb5Rlz6T92g34";
    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration()
        .environment(CONFIG_ENVIRONMENT)
        .clientId(CONFIG_CLIENT_ID)
        .merchantName("Enigma Store")
        .merchantPrivacyPolicyUri(
                Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));



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


}
