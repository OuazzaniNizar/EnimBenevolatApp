package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RespoModifContact extends AppCompatActivity {

    private EditText editTextNomcontact;
    private EditText editTextTelephone;
    private EditText editTextRenseignements;

    private Button buttonAjouter;
    MaterialSpinner spinner1;
    int position;
    String domaine="Association",edit,id_edit="",nomcontact_edit="",telephone_edit="",domaine_edit="",renseignements_edit="";
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/modifier_contact.php";
    private AlertDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_ajout_contact);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        Intent intent = getIntent();
        id_edit = intent.getStringExtra("ID");
        nomcontact_edit = intent.getStringExtra("NOM_CONTACT");
        domaine_edit  = intent.getStringExtra("DOMAINE");
        telephone_edit = intent.getStringExtra("PHONE");
        renseignements_edit = intent.getStringExtra("RENSEIGNEMENTS");
        edit = intent.getStringExtra("EDIT");

        editTextNomcontact = (EditText) findViewById(R.id.nom_contact);
        editTextTelephone = (EditText) findViewById(R.id.telephone_contact);
        editTextRenseignements = (EditText) findViewById(R.id.renseignements);

        editTextNomcontact.setText(nomcontact_edit);
        editTextTelephone.setText(telephone_edit);
        editTextRenseignements.setText(renseignements_edit);

        mytitle.setText(edit);
        buttonAjouter = (Button) findViewById(R.id.enregister_contact);
        buttonAjouter.setText(edit);
        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection() == true) {
                    if (editTextNomcontact.getText().toString().equals("") || editTextTelephone.getText().toString().equals("") || (position<0)) {
                        Toast.makeText(getApplicationContext(), R.string.erreur_vide, Toast.LENGTH_SHORT).show();

                    } else {
                        registerUser();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });


        spinner1 = (MaterialSpinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
               position=arg2;
                if(arg2>0){
                    domaine= arg0.getItemAtPosition(arg2).toString().trim().toLowerCase();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }


    private void registerUser() {
        String id=id_edit;
        String nomcontact = editTextNomcontact.getText().toString().trim().toLowerCase();
        String telephone = editTextTelephone.getText().toString().trim().toLowerCase();
        String renseignements = editTextRenseignements.getText().toString().trim().toLowerCase();

        register(id,nomcontact,domaine,telephone,renseignements);
    }

    private void register(String id,String nomcontact, String domaine, String telephone, String renseignements) {

        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("id",params[0]);
                data.put("nomcontact",params[1]);
                data.put("domaine",params[2]);
                data.put("telephone",params[3]);
                data.put("renseignements",params[4]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(id,nomcontact,domaine,telephone,renseignements);
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}