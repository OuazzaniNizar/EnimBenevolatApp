package com.example.ensmr.enimbenevolatapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONContact {
    public static String[] id;
    public static String[] nomcontact;
    public static String[] domaine;
    public static String[] telephone;
    public static String[] renseignements;


    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id";
    public static final String KEY_NOMCONTACT = "nomcontact";
    public static final String KEY_DOMAINE = "domaine";
    public static final String KEY_TELEPHONE = "telephone";
    public static final String KEY_RENSEIGNEMENTS = "renseignements";

    private JSONArray users = null;

    private String json;

    public ParseJSONContact(String json){
        this.json = json;
    }

    public void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            id = new String[users.length()];
            nomcontact = new String[users.length()];
            domaine = new String[users.length()];
            telephone = new String[users.length()];
            renseignements = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                id[i] = jo.getString(KEY_ID);
                nomcontact[i] = jo.getString(KEY_NOMCONTACT);
                domaine[i] = jo.getString(KEY_DOMAINE);
                telephone[i] = jo.getString(KEY_TELEPHONE);
                renseignements[i] = jo.getString(KEY_RENSEIGNEMENTS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
