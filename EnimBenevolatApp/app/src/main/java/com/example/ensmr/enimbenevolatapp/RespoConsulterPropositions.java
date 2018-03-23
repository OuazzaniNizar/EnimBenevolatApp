package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import dmax.dialog.SpotsDialog;

public class RespoConsulterPropositions extends AppCompatActivity {
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/consulter_propositions.php";

    private AlertDialog progressDialog;
    private ListView listviewPropositions;
    private Button consulter_propositions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_consulter_propositions);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.consulter_proposition);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        listviewPropositions = (ListView) findViewById(R.id.listViewPropositions);

        consulter_propositions = (Button) findViewById(R.id.consulter_propositions);
        consulter_propositions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetworkConnection()==true){
                    sendRequest();
                    consulter_propositions.setText("CONSULTER LES PROPOSITIONS ET RECLAMATIONS"+" ("+listviewPropositions.getCount()+")");
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


    private void sendRequest(){
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "C'est fait ! (:", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Oups, l'opération a échoué ! :(", Toast.LENGTH_SHORT).show();
                            }
                        }, 3000);
                        Toast.makeText(RespoConsulterPropositions.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json){
        ParseJSONPropositions pj = new ParseJSONPropositions(json);
        pj.parseJSON();
        RespoConsulterPropositionsList cl = new RespoConsulterPropositionsList(this,ParseJSONPropositions.propositions);
        listviewPropositions.setAdapter(cl);
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
