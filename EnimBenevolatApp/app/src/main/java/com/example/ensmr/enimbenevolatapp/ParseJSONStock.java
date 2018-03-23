package com.example.ensmr.enimbenevolatapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONStock {
    public static String[] id_stock;
    public static String[] ajoute_par_stock;
    public static String[] rapport_stock;
    public static String[] date_stock;
    public static String[] url_photo_stock;

    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id_stock";
    public static final String KEY_AJOUT = "ajoute_par_stock";
    public static final String KEY_DETAIL = "rapport_stock";
    public static final String KEY_DATE = "date_stock";
    public static final String KEY_URL = "url_photo_stock";

    private JSONArray users = null;

    private String json;

    public ParseJSONStock(String json){
        this.json = json;
    }

    public void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            id_stock = new String[users.length()];
            ajoute_par_stock = new String[users.length()];
            rapport_stock = new String[users.length()];
            date_stock = new String[users.length()];
            url_photo_stock = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                id_stock[i] = jo.getString(KEY_ID);
                ajoute_par_stock[i] = jo.getString(KEY_AJOUT);
                rapport_stock[i] = jo.getString(KEY_DETAIL);
                date_stock[i] = jo.getString(KEY_DATE);
                url_photo_stock[i] = jo.getString(KEY_URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
