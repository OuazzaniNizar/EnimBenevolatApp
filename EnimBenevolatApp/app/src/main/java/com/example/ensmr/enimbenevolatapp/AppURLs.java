package com.example.ensmr.enimbenevolatapp;

import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bbbbb on 10/09/2016.
 */
public class AppURLs {
    public static String URL = "http://clubelm.net/EnimBenevolatApp/index.php";
    public static String URL_check_membre = "http://clubelm.net/EnimBenevolatApp/index.php";
    public static String authentifiant="respo";
    public static String authentifiant_membre="membre";
    public static String authentifiant_pv="ecrire_pv";


    public String[] checkAuthentification(final String authentification) {
        String tag_string_req = "req_login";
        final String[] state = {"nop"};

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL_check_membre, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);

                    if (response.equals("OK")) {
                    state[0] ="yup";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("name", authentification);

                return params;
            }

        };

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    return state;
    }
}
