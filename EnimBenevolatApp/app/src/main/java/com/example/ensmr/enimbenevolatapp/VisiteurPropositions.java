package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class VisiteurPropositions extends AppCompatActivity {

    private TextView note;
    private EditText fullName, telephone;
    private Button registerButton;
    private LinearLayout nom_proposition, telephone_proposition;

    private AlertDialog progressDialog;
    List<String> listRapport = new ArrayList<String>();
    String rapport;
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/ajouter_proposition.php";

    MaterialSpinner spinner1,spinner_anonymite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visiteur_propositions);


        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.portail_proposition);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        registerButton = (Button) findViewById(R.id.register_button);

        nom_proposition=(LinearLayout)findViewById(R.id.nom_proposition);
        telephone_proposition=(LinearLayout)findViewById(R.id.telephone_proposition);

        fullName = (EditText) findViewById(R.id.fullname_register);
        telephone = (EditText) findViewById(R.id.telephone_register);
        note = (TextView) findViewById(R.id.note);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datedebut = sdf.format(c.getTime());
        listRapport.add("-Date: "+datedebut);

        spinner_anonymite = (MaterialSpinner) findViewById(R.id.spinner_anonymite);
        spinner_anonymite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getItemAtPosition(i).toString()){
                    case "Personnalisée":
                        nom_proposition.setVisibility(View.VISIBLE);
                        telephone_proposition.setVisibility(View.VISIBLE);
                        listRapport.add("\n-Note personnalisée");
                        listRapport.add("\n"+"-Nom de l'expéditeur: "+fullName.getText().toString());
                        listRapport.add("\n"+"-Téléphone de l'expéditeur: "+telephone.getText().toString());
                        break;
                    case "Anonyme":
                        listRapport.add("\n-Note anonyme");
                        nom_proposition.setVisibility(View.GONE);
                        telephone_proposition.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner1 = (MaterialSpinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (arg0.getItemAtPosition(arg2).toString()) {
                    case "Eloge":
                        listRapport.add("\n"+"-Type de la note: "+arg0.getItemAtPosition(arg2).toString());
                        break;
                    case "Suggestion":
                        listRapport.add("\n"+"-Type de la note: "+arg0.getItemAtPosition(arg2).toString());
                        break;

                    case "Réclamation":
                        listRapport.add("\n"+"-Type de la note: "+arg0.getItemAtPosition(arg2).toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRapport.add("\n"+"-Détail de la note: "+note.getText().toString());
                rapport = TextUtils.join("", listRapport);
                if (haveNetworkConnection() == true) {
                    register(rapport.toLowerCase().trim());
                }else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
                //sendEmail();
            }
        });

    }


    private void register(String rapport_note) {

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
                data.put("rapport_note",params[0]);
                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(rapport_note);
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



    protected void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","enimbenevolat@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Propositions et réclamations");
        emailIntent.putExtra(Intent.EXTRA_TEXT, rapport);
        try {
            startActivity(Intent.createChooser(emailIntent, "Veuillez choisir votre application de messagerie"));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(VisiteurPropositions.this, "Aucune application de messagerie n'est installée.", Toast.LENGTH_SHORT).show();
        }
    }

}
