package com.example.ensmr.enimbenevolatapp;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class MembreRegistration extends AppCompatActivity {

    private TextView tvLogin;
    private EditText fullName, password_to_register, telephone_to_register,authentifiant_register;
    private String text_profession,text_promo;
    private Button registerButton;
    private AlertDialog progressDialogue;


    MaterialSpinner spinner1;
    MaterialSpinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membre_registration);

        progressDialogue = new SpotsDialog(this, R.style.Custom);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mytitle = (TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.title_activity_registration);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        registerButton = (Button) findViewById(R.id.register_button);

        fullName = (EditText) findViewById(R.id.fullname_register);
        password_to_register = (EditText) findViewById(R.id.password_register);
        telephone_to_register = (EditText) findViewById(R.id.telephone_register);
        authentifiant_register = (EditText) findViewById(R.id.Authentifiant_register);
        tvLogin = (TextView) findViewById(R.id.tv_signin);

        spinner1 = (MaterialSpinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                text_profession=arg0.getItemAtPosition(arg2).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner2 = (MaterialSpinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                text_promo=arg0.getItemAtPosition(arg2).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                String password = password_to_register.getText().toString();
                String telephone = telephone_to_register.getText().toString();

                if (!name.isEmpty() &&  !password.isEmpty() && !telephone.isEmpty()
                        && (authentifiant_register.getText().toString().equals(""))) {
                    registerUser(name,password , telephone,"Promotion "+text_promo+" - "+text_profession);
                    }else {
                                Toast.makeText(getApplicationContext(), R.string.erreur_authentification, Toast.LENGTH_SHORT).show();
                            }

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MembreRegistration.this,
                        MembreLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(final String name, final String password, final String telephone, final String profession) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialogue.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialogue.dismiss();

                try {
                    Log.i("tagconvertstr", "[" + response + "]");
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), R.string.creation_compte, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(
                                MembreRegistration.this,
                                MembreLogin.class);
                        startActivity(intent);
                        finish();
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
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("name", name);
                params.put("password", password);
                params.put("telephone", telephone);
                params.put("profession", profession);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}



//Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
//Matcher m = p.matcher(etLoginSub.getEditableText());
