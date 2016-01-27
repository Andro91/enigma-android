package com.andro.enigma.parser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrija on 27-Jan-16.
 */
public class Parser {

    public static List<String> parseItems(String url) {

        List<String> dataItemList = new ArrayList<String>();

        JSONArray datItemJsonArray = null;

        JsonParser jParser = new JsonParser();

        // getting JSON Object from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        {

            try {
                // Getting Array of Contacts
                datItemJsonArray = json.getJSONArray("data");

                // looping through All Contacts
                for (int i = 0; i < datItemJsonArray.length(); i++) {

                    JSONObject pom = datItemJsonArray.getJSONObject(i);

                    // Storing each json item in variable
                    // Set parsing variable into news item and add it in list

                    String dataItem = new String();

//                    if (pom.has("title")) {
//                        dataItem.setTitle(pom.getString("title"));
//                    }

                    dataItemList.add(dataItem);
                }
            } catch (JSONException e) {
            }

            return dataItemList;
        }
    }

}
