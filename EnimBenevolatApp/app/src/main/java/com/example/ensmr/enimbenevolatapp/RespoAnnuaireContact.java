package com.example.ensmr.enimbenevolatapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class RespoAnnuaireContact extends AppCompatActivity {
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/consulter_annuaire.php";
    private static final String REGISTER_URL_DELETE = "http://clubelm.net/EnimBenevolatApp/supprimer_contact.php";


    private AlertDialog progressDialog;
    private ListView listViewAnnuaire;
    private Button consulter_annuaire;

    ArrayAdapter<String> adapter,adapter_search;
    String nomcontact,telephone,domaine,renseignements,id;
    String selectedPosition="Modifier le contact";
    TextView et;
    float s=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_annuaire_contact);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.annuaire_contacts);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        consulter_annuaire = (Button) findViewById(R.id.consulter_annuaire);
        consulter_annuaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float total=0;
                if(haveNetworkConnection()==true){
                    sendRequest();
                    for (int i = 0; i < listViewAnnuaire.getCount(); i++) {
                        final View v_child = getViewByPosition(i, listViewAnnuaire);
                        TextView textView_nomcontact = (TextView) v_child.findViewById(R.id.nom_contact);

                    }
                    consulter_annuaire.setText("ANNUAIRE DES CONTACTS"+" ("+listViewAnnuaire.getCount()+")");
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        final String[] choix_action = getResources().getStringArray(R.array.choix_contact);
        listViewAnnuaire = (ListView) findViewById(R.id.listViewAnnuaire);
        listViewAnnuaire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final View v_child = getViewByPosition(i, listViewAnnuaire);
                final AlertDialog.Builder builder_choix = new AlertDialog.Builder(RespoAnnuaireContact.this);
                builder_choix
                        .setTitle("ACTION")
                        .setSingleChoiceItems(R.array.choix_contact, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                selectedPosition =choix_action[arg1];
                            }
                        })
                    .setPositiveButton("Executer l'action",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (selectedPosition){
                                    case "Modifier le contact":
                                        TextView textView_id = (TextView) v_child.findViewById(R.id.id_contact);
                                        id=textView_id.getText().toString();
                                        TextView textView_nomcontact = (TextView) v_child.findViewById(R.id.nom_contact);
                                        nomcontact=textView_nomcontact.getText().toString();
                                        TextView textView_telephone = (TextView) v_child.findViewById(R.id.telephone_contact);
                                        telephone=textView_telephone.getText().toString();
                                        TextView textView_domaine = (TextView) v_child.findViewById(R.id.organisme);
                                        domaine=textView_domaine.getText().toString();
                                        TextView textView_renseignements = (TextView) v_child.findViewById(R.id.renseignements);
                                        renseignements=textView_renseignements.getText().toString();
                                        Intent intent=new Intent(RespoAnnuaireContact.this,RespoModifContact.class);
                                        intent.putExtra("ID",id);
                                        intent.putExtra("NOM_CONTACT",nomcontact);
                                        intent.putExtra("PHONE",telephone);
                                        intent.putExtra("DOMAINE",domaine);
                                        intent.putExtra("RENSEIGNEMENTS",renseignements);
                                        intent.putExtra("EDIT","METTRE A JOUR LES INFORMATIONS");
                                        startActivity(intent);
                                        break;

                                    case "Appeler le contact":
                                        TextView num_phone = (TextView) v_child.findViewById(R.id.telephone_contact);
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:"+num_phone.getText().toString()));
                                        if (ActivityCompat.checkSelfPermission(RespoAnnuaireContact.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            return;
                                        }
                                        startActivity(callIntent);
                                        break;

                                    case "Supprimer le contact (irréversible)":
                                        TextView id_contact = (TextView) v_child.findViewById(R.id.id_contact);
                                        delete(id_contact.getText().toString());
                                        break;
                                }

                            }
                        })
                        .setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        })
                        .show();
            }
        });
        listViewAnnuaire.setAdapter(adapter);



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
                        Toast.makeText(RespoAnnuaireContact.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json){
        ParseJSONContact pj = new ParseJSONContact(json);
        pj.parseJSON();
        RespoAnnuaireContactList cl = new RespoAnnuaireContactList(this,ParseJSONContact.id,ParseJSONContact.nomcontact,ParseJSONContact.domaine,
                ParseJSONContact.telephone,ParseJSONContact.renseignements);
        listViewAnnuaire.setAdapter(cl);
    }





    private void delete(String id) {
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
                String result = ruc.sendPostRequest(REGISTER_URL_DELETE,data);
                return  result;
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(id);
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
