package com.example.ensmr.enimbenevolatapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONPropositions {
    public static String[] propositions;


    public static final String JSON_ARRAY = "result";
    public static final String KEY_PROPO = "propositions";

    private JSONArray users = null;

    private String json;

    public ParseJSONPropositions(String json){
        this.json = json;
    }

    public void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            propositions = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                propositions[i] = jo.getString(KEY_PROPO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
