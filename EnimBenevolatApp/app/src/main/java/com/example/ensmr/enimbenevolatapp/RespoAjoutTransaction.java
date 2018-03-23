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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RespoAjoutTransaction extends AppCompatActivity {
    private EditText fullname,details_transaction, somme_transaction;
    private Button registerButton;
    private AlertDialog progressDialog;
    String type_trans;
    MaterialSpinner type_transaction;
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/ajouter_transaction.php";
    private String file = "fichier_nom";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_ajout_transaction);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle = (TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.crediter_debiter);
        ImageView myimage = (ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);


        registerButton = (Button) findViewById(R.id.register_button);

        fullname = (EditText) findViewById(R.id.fullname_register);
        somme_transaction = (EditText) findViewById(R.id.somme_transaction);
        details_transaction = (EditText) findViewById(R.id.details_transaction);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String date_ajout = sdf.format(c.getTime());


        try{
            FileInputStream fin = openFileInput(file);
            int ch;
            String temp="";

            while( (ch = fin.read()) != -1){
                temp = temp + Character.toString((char)ch);
            }
            fullname.setText(temp);
        }
        catch(Exception e){
        }


        type_transaction = (MaterialSpinner) findViewById(R.id.type_transaction);
        type_transaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                type_trans=arg0.getItemAtPosition(arg2).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection() == true) {
                    if (fullname.getText().toString().equals("") || somme_transaction.getText().toString().equals("") || details_transaction.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), R.string.erreur_vide, Toast.LENGTH_SHORT).show();
                    } else {
                        if(type_trans.equals("Crédit (-)")){
                            String rapoort=
                                    "-Date d'ajout de la transaction: " + date_ajout
                                            +"\nAjoutée par : "+fullname.getText().toString()
                                            +"\nType de la transaction : "+type_trans
                                            +"\nSomme de la transaction : -"+somme_transaction.getText().toString()
                                            +"\nDétails de la transaction : "+"\n"+details_transaction.getText().toString();
                            float negatif=-Float.parseFloat(somme_transaction.getText().toString());
                            String somme_tran=""+negatif;
                            registerTransaction(somme_tran,type_trans,rapoort);

                        }else{
                            String rapoort=
                                    "-Date d'ajout de la transaction: " + date_ajout
                                            +"\nAjoutée par : "+fullname.getText().toString()
                                            +"\nType de la transaction : "+type_trans
                                            +"\nSomme de la transaction : "+somme_transaction.getText().toString()
                                            +"\nDétails de la transaction : "+"\n"+details_transaction.getText().toString();
                            registerTransaction(somme_transaction.getText().toString(),type_trans,rapoort);

                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void registerTransaction(String type, String somme, String rapport) {

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
                data.put("somme_transaction",params[0]);
                data.put("type_transaction",params[1]);
                data.put("details_transaction",params[2]);
                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(type,somme,rapport);
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
