package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MembreLogin extends AppCompatActivity {

    private Session session;

    private Button registrationButton, loginButton;
    private EditText name_to_login, password_to_login,authentification;
    private AlertDialog progressDialogue;
    private String file = "fichier_nom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membre_login);

        progressDialogue = new SpotsDialog(this, R.style.Custom);
        session = new Session(MembreLogin.this);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.title_activity_login);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);


        registrationButton = (Button) findViewById(R.id.registration_button);
        loginButton = (Button) findViewById(R.id.signin_button);
        name_to_login = (EditText) findViewById(R.id.name_to_login);
        password_to_login = (EditText) findViewById(R.id.password_to_login);
        authentification = (EditText) findViewById(R.id.authentification);


        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        MembreRegistration.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_to_login.getText().toString();
                String password = password_to_login.getText().toString();

                if (name.trim().length() > 0 && password.trim().length() > 0) {
                    try {
                        FileOutputStream fOut = openFileOutput(file,MODE_WORLD_READABLE);
                        fOut.write(name.getBytes());
                        fOut.close();
                    }

                    catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    checkLogin(name, password);

                } else {
                    Toast.makeText(getApplicationContext(),R.string.erreur_vide, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkLogin(final String name, final String password) {
        String tag_string_req = "req_login";
        progressDialogue.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialogue.dismiss();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String userId = jObj.getString("user_id");

                    if (userId != "null") {
                        if(authentification.getText().toString().equals(AppURLs.authentifiant_membre)) {
                                session.setLogin(true);
                                Intent intentMembre = new Intent(MembreLogin.this,
                                        EspaceMembre.class);
                                startActivity(intentMembre);
                                finish();
                        }else {
                            Toast.makeText(getApplicationContext(),R.string.erreur_authentification, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialogue.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("name", name);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}