package com.example.ensmr.enimbenevolatapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONHistorique {
    public static String[] id;
    public static String[] programme_action;
    public static String[] name_action;
    public static String[] details_action;
    public static String[] url_photo;


    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id";
    public static final String KEY_AJOUT = "programme_action";
    public static final String KEY_NOMBRE_PRESENTS = "name_action";
    public static final String KEY_DETAIL = "details_action";
    public static final String KEY_URL = "url_photo";

    private JSONArray users = null;

    private String json;

    public ParseJSONHistorique(String json){
        this.json = json;
    }

    public void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            id = new String[users.length()];
            programme_action = new String[users.length()];
            name_action = new String[users.length()];
            details_action = new String[users.length()];
            url_photo = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                id[i] = jo.getString(KEY_ID);
                programme_action[i] = jo.getString(KEY_AJOUT);
                name_action[i] = jo.getString(KEY_NOMBRE_PRESENTS);
                details_action[i] = jo.getString(KEY_DETAIL);
                url_photo[i] = jo.getString(KEY_URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
