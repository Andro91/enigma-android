package com.andro.enigma.helper;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.HashMap;

/**
 * Created by Internet on 28-Jan-16.
 */
public class Helper {

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
