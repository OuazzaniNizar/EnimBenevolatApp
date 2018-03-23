package com.example.ensmr.enimbenevolatapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONpv {
    public static String[] id;
    public static String[] nombre_presents;
    public static String[] ajoute_par;
    public static String[] detail_pv;
    public static String[] url_photo;


    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id";
    public static final String KEY_AJOUT = "ajoute_par";
    public static final String KEY_NOMBRE_PRESENTS = "nombre_presents";
    public static final String KEY_DETAIL = "detail_pv";
    public static final String KEY_URL = "url_photo";

    private JSONArray users = null;

    private String json;

    public ParseJSONpv(String json){
        this.json = json;
    }

    public void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            id = new String[users.length()];
            nombre_presents = new String[users.length()];
            ajoute_par = new String[users.length()];
            detail_pv = new String[users.length()];
            url_photo = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                id[i] = jo.getString(KEY_ID);
                ajoute_par[i] = jo.getString(KEY_AJOUT);
                nombre_presents[i] = jo.getString(KEY_NOMBRE_PRESENTS);
                detail_pv[i] = jo.getString(KEY_DETAIL);
                url_photo[i] = jo.getString(KEY_URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
