package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Context;
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

import java.io.FileInputStream;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RespoAjoutContact extends AppCompatActivity {


    private EditText editTextNom;
    private EditText editTextNomcontact;
    private EditText editTextTelephone;
    private EditText editTextRenseignements;

    private Button buttonAjouter;
    MaterialSpinner spinner1;
    String domaine;
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/ajouter_contact.php";
    private AlertDialog progressDialog;
    private String file = "fichier_nom";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_ajout_contact);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.ajout_contact);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        editTextNom=(EditText) findViewById(R.id.nom);
        editTextNomcontact = (EditText) findViewById(R.id.nom_contact);
        editTextTelephone = (EditText) findViewById(R.id.telephone_contact);
        editTextRenseignements = (EditText) findViewById(R.id.renseignements);


        try{
            FileInputStream fin = openFileInput(file);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            editTextNom.setText(temp);
        }
        catch(Exception e){
        }



        buttonAjouter = (Button) findViewById(R.id.enregister_contact);
        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection() == true) {
                    if (editTextNomcontact.getText().toString().equals("") || editTextTelephone.getText().toString().equals("")) {
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
                domaine= arg0.getItemAtPosition(arg2).toString().trim().toLowerCase();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }


    private void registerUser() {
        String nom = editTextNom.getText().toString().trim().toLowerCase();
        String nomcontact = editTextNomcontact.getText().toString().trim().toLowerCase();
        String telephone = editTextTelephone.getText().toString().trim().toLowerCase();
        String renseignements = editTextRenseignements.getText().toString().trim().toLowerCase();

        register(nom,nomcontact,domaine,telephone,renseignements);
    }

    private void register(String nom,String nomcontact, String domaine, String telephone, String renseignements) {

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
                data.put("nom",params[0]);
                data.put("nomcontact",params[1]);
                data.put("domaine",params[2]);
                data.put("telephone",params[3]);
                data.put("renseignements",params[4]);
                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(nom,nomcontact,domaine,telephone,renseignements);
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