package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class RespoConsulterHistorique extends AppCompatActivity {
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/consulter_historique.php";


    private AlertDialog progressDialog;
    private ListView listViewHistorique;
    private Button consulter_historique;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_consulter_historique);


        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle = (TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.portail_historique);
        ImageView myimage = (ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);
        listViewHistorique = (ListView) findViewById(R.id.listViewHistorique);


        consulter_historique = (Button) findViewById(R.id.consulter_historique);
        consulter_historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetworkConnection()==true){
                    sendRequest();
                    for (int i = 0; i < listViewHistorique.getCount(); i++) {
                        final View v_child = getViewByPosition(i, listViewHistorique);
                        ImageView image_action=(ImageView) v_child.findViewById(R.id.image_action);
                        TextView i_url=(TextView) v_child.findViewById(R.id.url_photo);
                        PicassoClient.downloadImage(getApplicationContext(),i_url.getText().toString(),image_action);

                    }
                    consulter_historique.setText("ACTIONS DU CLUB"+" ("+listViewHistorique.getCount()+")");
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
                        Toast.makeText(RespoConsulterHistorique.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json){
        ParseJSONHistorique pj = new ParseJSONHistorique(json);
        pj.parseJSON();
        RespoConsulterHistoriqueList cl = new RespoConsulterHistoriqueList(this,ParseJSONHistorique.id,ParseJSONHistorique.programme_action,ParseJSONHistorique.name_action,
                ParseJSONHistorique.details_action,ParseJSONHistorique.url_photo);
        listViewHistorique.setAdapter(cl);
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

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
