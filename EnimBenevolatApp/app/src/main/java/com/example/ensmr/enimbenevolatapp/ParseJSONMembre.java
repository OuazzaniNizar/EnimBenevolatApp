package com.example.ensmr.enimbenevolatapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONMembre {
    public static String[] id_membre;
    public static String[] nom_membre;
    public static String[] filiere_membre;
    public static String[] telephone_membre;
    public static String[] taches_membre;
    public static String[] presence_membre;

    private JSONArray users = null;
    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id_membre";
    public static final String KEY_NOMCONTACT = "nom_membre";
    public static final String KEY_DOMAINE = "filiere_membre";
    public static final String KEY_TELEPHONE = "telephone_membre";
    public static final String KEY_TACHES = "taches_membre";
    public static final String KEY_PRESENCE = "presence_membre";

    private String json;

    public ParseJSONMembre(String json){
        this.json = json;
    }

    public void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            id_membre = new String[users.length()];
            nom_membre = new String[users.length()];
            filiere_membre = new String[users.length()];
            telephone_membre = new String[users.length()];
            taches_membre = new String[users.length()];
            presence_membre = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                id_membre[i] = jo.getString(KEY_ID);
                nom_membre[i] = jo.getString(KEY_NOMCONTACT);
                filiere_membre[i] = jo.getString(KEY_DOMAINE);
                telephone_membre[i] = jo.getString(KEY_TELEPHONE);
                taches_membre[i] = jo.getString(KEY_TACHES);
                presence_membre[i] = jo.getString(KEY_PRESENCE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
