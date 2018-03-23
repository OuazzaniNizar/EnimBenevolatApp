package com.example.ensmr.enimbenevolatapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONTransaction {
    public static String[] type_transaction;
    public static String[] somme_transaction;
    public static String[] detail_transaction;


    public static final String JSON_ARRAY = "result";
    public static final String KEY_type_transaction = "type_transaction";
    public static final String KEY_somme_transaction = "somme_transaction";
    public static final String KEY_detail_transaction = "detail_transaction";

    private JSONArray users = null;

    private String json;

    public ParseJSONTransaction(String json){
        this.json = json;
    }

    public void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            type_transaction = new String[users.length()];
            somme_transaction = new String[users.length()];
            detail_transaction = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                type_transaction[i] = jo.getString(KEY_type_transaction);
                somme_transaction[i] = jo.getString(KEY_somme_transaction);
                detail_transaction[i] = jo.getString(KEY_detail_transaction);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
