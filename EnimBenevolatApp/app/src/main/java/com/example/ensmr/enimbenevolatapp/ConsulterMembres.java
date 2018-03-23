package com.example.ensmr.enimbenevolatapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

import static com.example.ensmr.enimbenevolatapp.ParseJSONMembre.KEY_DOMAINE;
import static com.example.ensmr.enimbenevolatapp.ParseJSONMembre.KEY_ID;
import static com.example.ensmr.enimbenevolatapp.ParseJSONMembre.KEY_NOMCONTACT;
import static com.example.ensmr.enimbenevolatapp.ParseJSONMembre.KEY_PRESENCE;
import static com.example.ensmr.enimbenevolatapp.ParseJSONMembre.KEY_TACHES;
import static com.example.ensmr.enimbenevolatapp.ParseJSONMembre.KEY_TELEPHONE;
import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;

public class ConsulterMembres extends AppCompatActivity {
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/consulter_membres.php";
    private static final String EDITT_URL = "http://clubelm.net/EnimBenevolatApp/incrementer_membre.php";
    private static final String DELETE_URL = "http://clubelm.net/EnimBenevolatApp/supprimer_membre.php";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private AlertDialog progressDialog;
    private ListView listViewMembres;
    private Button consulter_membre,decompte_presence;
    ArrayAdapter<String> adapter;
    List<String> num_tel_selec = new ArrayList<String>();
    MaterialSpinner spinner_membres;
    boolean mode_decompte=false;
    String selectedPosition="Envoyer message au membre",tranche="1-50";
    int nb_presents=0;
    private String file = "fichier_nb_presents";
    List<String> listPresents = new ArrayList<String>();
    private String file_presents = "fichier_presents";
    private String query=null;
    public static String[] id_membre;
    public static String[] nom_membre;
    public static String[] filiere_membre;
    public static String[] telephone_membre;
    public static String[] taches_membre;
    public static String[] presence_membre;
    public static String[] user_password;
    public static String[] _tc;
    public static String[] salt;
    private JSONArray users = null;
    public static final String JSON_ARRAY = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulter_membres);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.liste_membres);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);


        listViewMembres = (ListView) findViewById(R.id.listViewMembres);

        progressDialog = new SpotsDialog(this, R.style.Custom);


        spinner_membres = (MaterialSpinner) findViewById(R.id.spinner_membres);
        spinner_membres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                tranche=arg0.getItemAtPosition(arg2).toString();
                switch (tranche){
                    case "1-50":
                        envoieSms(1,50,listViewMembres);
                        break;
                    case "51-100":
                        envoieSms(51,100,listViewMembres);
                        break;
                    case "101-150":
                        envoieSms(101,150,listViewMembres);
                        break;
                    case "151-fin":
                        envoieSms(151,200,listViewMembres);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listViewMembres.setAdapter(adapter);
        consulter_membre = (Button) findViewById(R.id.consulter_membre);
        spinner_membres.setVisibility(View.GONE);
        consulter_membre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(haveNetworkConnection()==true){
                    consulter_membre.setText("LISTE DES MEMBRES"+" ("+listViewMembres.getCount()+")");
                    spinner_membres.setVisibility(View.VISIBLE);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view_authentif = inflater.inflate(R.layout.row_view_userpassword, null, false);
                    final EditText search_query = (EditText) view_authentif.findViewById(R.id.portail_respo_authetification);
                    search_query.setHint("Exemple : Jamal Iâazzaouihda,\n Jamal, Iâazzaouihda,\n O648355531...");
                    search_query.setInputType(InputType.TYPE_CLASS_TEXT);
                    new AlertDialog.Builder(new ContextThemeWrapper(ConsulterMembres.this, R.style.AlerDialog))
                            .setView(view_authentif)
                            .setTitle("Veuilez Indiquer le Num, Nom ou Prénom que vous recherchez... !")
                            .setPositiveButton("Rechercher", new DialogInterface.OnClickListener() {
                                @TargetApi(11)
                                public void onClick(DialogInterface dialog, int id) {
                                    if(search_query.length()>0) {
                                        query = search_query.getText().toString();
                                        new AsyncFetch("%" + query + "%").execute();
                                    }else {
                                        sendRequest();
                                    }

                                }
                            }).show();

                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        decompte_presence = (Button) findViewById(R.id.decompte_presence);
        decompte_presence.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //checkrespo();
                //if(checkrespo()==true){
                    mode_decompte=true;
                    decompte_presence.setBackground(getDrawable(R.drawable.contour_rose));
                    decompte_presence.setText("Désactiver Mode décompte");

                    decompte_presence.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View view) {
                            decompte_presence.setBackground(getDrawable(R.drawable.contour_gris));
                            mode_decompte=false;
                            decompte_presence.setText("Nombre de présents : "+nb_presents+"\nEnregistrer ?");

                            decompte_presence.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    decompte_presence.setBackground(getDrawable(R.drawable.contour));
                                    decompte_presence.setText("Fichier enregistré (:");
                                    try {
                                        FileOutputStream fOut = openFileOutput(file,MODE_WORLD_READABLE);
                                        String nb_pr=""+nb_presents;
                                        fOut.write(nb_pr.getBytes());
                                        fOut.close();
                                    }

                                    catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }


                                    try {
                                        FileOutputStream fOut = openFileOutput(file_presents,MODE_WORLD_READABLE);
                                        fOut.write(TextUtils.join("",listPresents).getBytes());
                                        fOut.close();
                                    }

                                    catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    });
                //}
                //if(checkrespo()==false){
                //    Toast.makeText(getApplicationContext(),R.string.erreur_authentification,Toast.LENGTH_LONG).show();
                //}
            }
        });

        final String[] choix_action = getResources().getStringArray(R.array.choix_membre);
        listViewMembres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final View v_child = getViewByPosition(i, listViewMembres);
                final AlertDialog.Builder builder_choix = new AlertDialog.Builder(ConsulterMembres.this);
                builder_choix
                        .setTitle("ACTION")
                        .setSingleChoiceItems(R.array.choix_membre, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                selectedPosition =choix_action[arg1];
                            }
                        })
                        .setPositiveButton("Executer l'action",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (selectedPosition){
                                    case "Envoyer message au membre":
                                        TextView num_phone = (TextView) v_child.findViewById(R.id.telephone_membre);
                                        num_phone.setTextColor(Color.rgb(255,65,130));
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", num_phone.getText().toString(), null)));
                                        break;
                                    case "Appeler le membre":
                                        num_phone = (TextView) v_child.findViewById(R.id.telephone_membre);
                                        num_phone.setTextColor(Color.rgb(255,65,130));
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:"+num_phone.getText().toString()));
                                        if (ActivityCompat.checkSelfPermission(ConsulterMembres.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            return;
                                        }
                                        startActivity(callIntent);
                                        break;

                                    case "Incrémenter le nombre de présence":
                                        if(mode_decompte=false){
                                            Toast.makeText(getApplicationContext(),"Veuillez activer le mode Décompte de présence !",Toast.LENGTH_LONG).show();
                                        }else {
                                            if(mode_decompte=true){
                                            TextView txtid_membre = (TextView) v_child.findViewById(R.id.id_membre);
                                            TextView txtnom_membre = (TextView) v_child.findViewById(R.id.nom_membre);
                                            TextView txtpresence_membre = (TextView) v_child.findViewById(R.id.presence_membre);
                                            TextView txttaches_membre = (TextView) v_child.findViewById(R.id.taches_membre);
                                            float present = Float.parseFloat(txtpresence_membre.getText().toString())+1;
                                            String presence=""+present;
                                            listPresents.add(txtnom_membre.getText().toString()+" - ");
                                            txtpresence_membre.setText(presence);
                                            register(txtid_membre.getText().toString(),presence,txttaches_membre.getText().toString());
                                            nb_presents++;
                                            Toast.makeText(getApplicationContext(),"Merci "+txtnom_membre.getText().toString()+" (:", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        break;

                                    case "Incrémenter le nombre de tâches réalisées":
                                        TextView txtid_membre = (TextView) v_child.findViewById(R.id.id_membre);
                                        TextView txtpresence_membre = (TextView) v_child.findViewById(R.id.presence_membre);
                                        TextView txttaches_membre = (TextView) v_child.findViewById(R.id.taches_membre);
                                        float tache = Float.parseFloat(txttaches_membre.getText().toString())+1;
                                        final String taches=""+tache;
                                        txttaches_membre.setText(taches);
                                        final String id_membre=txtid_membre.getText().toString();
                                        final String presence_membre=txtpresence_membre.getText().toString();

                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View view_authentif = inflater.inflate(R.layout.row_view_userpassword, null, false);
                                        final EditText password = (EditText) view_authentif.findViewById(R.id.portail_respo_authetification);

                                        new AlertDialog.Builder(new ContextThemeWrapper(ConsulterMembres.this, R.style.AlerDialog))
                                                .setView(view_authentif)
                                                .setTitle("Veuillez vous authentifier s'il vous plaît !")
                                                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                                                    @TargetApi(11)
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        if(password.getText().toString().equals("respo")){
                                                        }
                                                    }
                                                }).show();
                                        break;

                                    case "Supprimer le compte du membre":
                                        txtid_membre = (TextView) v_child.findViewById(R.id.id_membre);
                                        final String id_membre_supp=txtid_membre.getText().toString();

                                        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        view_authentif = inflater.inflate(R.layout.row_view_userpassword, null, false);
                                        final EditText password_supp = (EditText) view_authentif.findViewById(R.id.portail_respo_authetification);

                                        new AlertDialog.Builder(new ContextThemeWrapper(ConsulterMembres.this, R.style.AlerDialog))
                                                .setView(view_authentif)
                                                .setTitle("Veuillez vous authentifier s'il vous plaît !")
                                                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                                                    @TargetApi(11)
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        if(password_supp.getText().toString().equals("respo")){
                                                            delete(id_membre_supp);
                                                        }
                                                    }
                                                }).show();
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

    }

    private void sendRequest(){
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Done ! (:", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Oups, something went wrong ! :(", Toast.LENGTH_SHORT).show();                            }
                        }, 3000);
                        Toast.makeText(ConsulterMembres.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void showJSON(String json){
        ParseJSONMembre pj = new ParseJSONMembre(json);
        pj.parseJSON();
        RespoConsulterMembresList cl = new RespoConsulterMembresList(this, ParseJSONMembre.id_membre,ParseJSONMembre.nom_membre,
                ParseJSONMembre.filiere_membre,ParseJSONMembre.telephone_membre,ParseJSONMembre.taches_membre,ParseJSONMembre.presence_membre);
        listViewMembres.setAdapter(cl);
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



    private void register(String id_membre,String nombre_presence, String nombre_taches) {

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
                data.put("id_membre",params[0]);
                data.put("nombre_presence",params[1]);
                data.put("nombre_taches",params[2]);

                String result = ruc.sendPostRequest(EDITT_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(id_membre,nombre_presence,nombre_taches);
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
                String result = ruc.sendPostRequest(DELETE_URL,data);
                return  result;
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(id);
    }

    private void envoieSms (int debut, int fin, ListView list){
        int fin_liste;
        if (fin>list.getCount()){
            fin_liste=list.getCount();
        }else {
            fin_liste=fin;
        }

        for (int i = debut; i <fin_liste; i++) {
            final View v_child = getViewByPosition(i, list);
            TextView num_tel = (TextView) v_child.findViewById(R.id.telephone_membre);
            num_tel_selec.add(num_tel.getText().toString());

        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",TextUtils.join(",", num_tel_selec), null)));
    }


    private boolean checkrespo() {
        boolean authent_respo = false;
        final int[] login = {0};
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view_authentif = inflater.inflate(R.layout.row_view_userpassword, null, false);
        final EditText password = (EditText) view_authentif.findViewById(R.id.portail_respo_authetification);

        new AlertDialog.Builder(new ContextThemeWrapper(ConsulterMembres.this, R.style.AlerDialog))
                .setView(view_authentif)
                .setTitle("Veuillez vous authentifier s'il vous plaît !")
                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        if(password.getText().toString().equals(AppURLs.authentifiant)){
                         login[0] =1;
                        }
                    }
                }).show();

        if(login== new int[]{1}){
            authent_respo = true;
        }

        return authent_respo;
    }












    // Create class AsyncFetch
    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(ConsulterMembres.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

    public AsyncFetch(String searchQuery){
        this.searchQuery=searchQuery;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

    }

    @Override
    protected String doInBackground(String... params) {
        try {

            // Enter URL address where your php file resides
            url = new URL("http://clubelm.net/EnimBenevolatApp/search_membre.php");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.toString();
        }
        try {

            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput to true as we send and recieve data
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // add parameter to our above url
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return e1.toString();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Pass data to onPostExecute method
                return (result.toString());

            } else {
                return("Erreur de Connexion !");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            conn.disconnect();
        }


    }

    @Override
    protected void onPostExecute(String result) {

        //this method will be running on UI thread
        pdLoading.dismiss();
        //List<ConsulterMembresData> data=new ArrayList<>();

        pdLoading.dismiss();
        if(result.equals("no rows")) {
            Toast.makeText(ConsulterMembres.this, "Aucun résultat n'a été trouvé pour le mot clé saisi !", Toast.LENGTH_LONG).show();
        }else{
            JSONObject jsonObject=null;

            try {

                JSONArray jArray = new JSONArray(result);
                //jsonObject = new JSONObject(json);
                //users = jsonObject.getJSONArray(JSON_ARRAY);
                id_membre = new String[jArray.length()];
                nom_membre = new String[jArray.length()];
                filiere_membre = new String[jArray.length()];
                telephone_membre = new String[jArray.length()];
                taches_membre = new String[jArray.length()];
                presence_membre = new String[jArray.length()];
                user_password = new String[jArray.length()];
                _tc = new String[jArray.length()];
                salt = new String[jArray.length()];

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    //ConsulterMembresData fishData = new ConsulterMembresData();
                    id_membre[i] = json_data.getString("user_id");
                    nom_membre[i] = json_data.getString("user_name");
                    filiere_membre[i] = json_data.getString("user_profession");
                    telephone_membre[i] = json_data.getString("user_telephone");
                    user_password[i] = json_data.getString("user_password");
                    salt[i] = json_data.getString("salt");
                    _tc[i] = json_data.getString("_tc");
                    taches_membre[i] = json_data.getString("nombre_taches");
                    presence_membre[i] = json_data.getString("nombre_presence");
                    //data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                listViewMembres=(ListView) findViewById(R.id.listViewMembres);
                adapter=new RespoConsulterMembresList(ConsulterMembres.this,id_membre,nom_membre,filiere_membre,telephone_membre,taches_membre,presence_membre);
                listViewMembres.setAdapter(adapter);

            } catch (JSONException e) {
                // You to understand what actually error is and handle it appropriately
                Toast.makeText(ConsulterMembres.this, e.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(ConsulterMembres.this, result.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

}

}
