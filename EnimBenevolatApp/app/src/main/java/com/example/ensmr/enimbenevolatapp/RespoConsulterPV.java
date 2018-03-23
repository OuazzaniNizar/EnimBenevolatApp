package com.example.ensmr.enimbenevolatapp;

import android.Manifest;
import android.app.AlertDialog;
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
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class RespoConsulterPV extends AppCompatActivity {

    private static String REGISTER_URL;
    private static final String REGISTER_URL_DELETE = "http://clubelm.net/EnimBenevolatApp/supprimer_pv.php";


    private AlertDialog progressDialog;
    private ListView listViewPV;
    private Button consulter_pv,consulter_pv_cellule,consulter_pv_autres;
    ArrayAdapter<String> adapter;
    String nombre_presents,details_pv,url_photo,id;
    int selectedPosition=-1;
    TextView et;
    float s=0;
    float [] evol_presence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_consulter_pv);


        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.consulter_pv);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);


        final LineChart lineChart = (LineChart) findViewById(R.id.chart);
        final ArrayList<Entry> group1 = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<String>();


        consulter_pv_autres = (Button) findViewById(R.id.consulter_pv_autres);
        Intent provenance = getIntent();
        String prov=provenance.getStringExtra("provenance");
        if(prov.equals("membre")){
            consulter_pv_autres.setVisibility(View.GONE);
        }

        consulter_pv_autres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetworkConnection()==true){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        consulter_pv_cellule.setBackground(getDrawable(R.drawable.contour));
                        consulter_pv.setBackground(getDrawable(R.drawable.contour));
                        consulter_pv_autres.setBackground(getDrawable(R.drawable.contour_rose));
                    }

                    REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/consulter_pv_autres.php";
                    sendRequest();

                    evol_presence=new float[listViewPV.getCount()];

                    for (int i = 0; i < listViewPV.getCount(); i++) {
                        final View v_child = getViewByPosition(i, listViewPV);

                        TextView nombre_presents = (TextView) v_child.findViewById(R.id.nombre_presents);
                        float nb_presence= Integer.parseInt(nombre_presents.getText().toString());
                        evol_presence[i]=nb_presence;
                        //group1.add(new Entry(1f, listViewPV.getCount()-1-i));
                        // group1.add(new Entry(1f,i));

                    }

                    ArrayList<Entry> entries = new ArrayList<>();
                    for (int i = listViewPV.getCount()-1; i >=0 ; i--) {
                        entries.add(new Entry(evol_presence[i], listViewPV.getCount()-1-i));
                    }
                    LineDataSet dataset = new LineDataSet(entries, "Nombre de présents");
                    for (int j=1;j<listViewPV.getCount()+1;j++) {
                        String num_reunion = "" + j;
                        labels.add("R : " + num_reunion);
                    }

                    LineData data = new LineData(labels, dataset);
                    dataset.setColor(Color.rgb(255, 65, 130)); //
                    dataset.setDrawCubic(true);
                    dataset.setDrawFilled(true);

                    lineChart.setData(data);
                    lineChart.animateY(5000);
                    lineChart.setDescription("Evolution de présence");
                    consulter_pv_autres.setText("Autres "+" ("+listViewPV.getCount()+")");
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });


        consulter_pv_cellule = (Button) findViewById(R.id.consulter_pv_cellule);
        consulter_pv_cellule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetworkConnection()==true){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        consulter_pv_autres.setBackground(getDrawable(R.drawable.contour));
                        consulter_pv_cellule.setBackground(getDrawable(R.drawable.contour_rose));
                        consulter_pv.setBackground(getDrawable(R.drawable.contour));
                    }

                    REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/consulter_pv_cellules.php";
                    sendRequest();

                    evol_presence=new float[listViewPV.getCount()];

                    for (int i = 0; i < listViewPV.getCount(); i++) {
                        final View v_child = getViewByPosition(i, listViewPV);

                        TextView nombre_presents = (TextView) v_child.findViewById(R.id.nombre_presents);
                        float nb_presence= Integer.parseInt(nombre_presents.getText().toString());
                        evol_presence[i]=nb_presence;
                        //group1.add(new Entry(1f, listViewPV.getCount()-1-i));
                        // group1.add(new Entry(1f,i));

                    }

                    ArrayList<Entry> entries = new ArrayList<>();
                    for (int i = listViewPV.getCount()-1; i >=0 ; i--) {
                        entries.add(new Entry(evol_presence[i], listViewPV.getCount()-1-i));
                    }
                    LineDataSet dataset = new LineDataSet(entries, "Nombre de présents");
                    for (int j=1;j<listViewPV.getCount()+1;j++) {
                        String num_reunion = "" + j;
                        labels.add("R : " + num_reunion);
                    }

                    LineData data = new LineData(labels, dataset);
                    dataset.setColor(Color.rgb(255, 65, 130)); //
                    dataset.setDrawCubic(true);
                    dataset.setDrawFilled(true);

                    lineChart.setData(data);
                    lineChart.animateY(5000);
                    lineChart.setDescription("Evolution de présence");
                    consulter_pv_cellule.setText("R. Cellules "+" ("+listViewPV.getCount()+")");
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        consulter_pv = (Button) findViewById(R.id.consulter_pv);
        consulter_pv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float total=0;
                if(haveNetworkConnection()==true){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        consulter_pv_autres.setBackground(getDrawable(R.drawable.contour));
                        consulter_pv.setBackground(getDrawable(R.drawable.contour_rose));
                        consulter_pv_cellule.setBackground(getDrawable(R.drawable.contour));
                    }
                    REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/consulter_pv.php";
                    sendRequest();
                    evol_presence=new float[listViewPV.getCount()];

                    for (int i = 0; i < listViewPV.getCount(); i++) {
                        final View v_child = getViewByPosition(i, listViewPV);

                        TextView nombre_presents = (TextView) v_child.findViewById(R.id.nombre_presents);
                        float nb_presence= Integer.parseInt(nombre_presents.getText().toString());
                        evol_presence[i]=nb_presence;
                        //group1.add(new Entry(1f, listViewPV.getCount()-1-i));
                       // group1.add(new Entry(1f,i));

                    }

                    ArrayList<Entry> entries = new ArrayList<>();
                    for (int i = listViewPV.getCount()-1; i >=0 ; i--) {
                        entries.add(new Entry(evol_presence[i], listViewPV.getCount()-1-i));
                    }
                    LineDataSet dataset = new LineDataSet(entries, "Nombre de présents");
                    for (int j=1;j<listViewPV.getCount()+1;j++) {
                        String num_reunion = "" + j;
                        labels.add("R : " + num_reunion);
                    }

                    LineData data = new LineData(labels, dataset);
                    dataset.setColor(Color.rgb(255, 65, 130)); //
                    dataset.setDrawCubic(true);
                    dataset.setDrawFilled(true);

                    lineChart.setData(data);
                    lineChart.animateY(5000);
                    lineChart.setDescription("Evolution de présence");


                    consulter_pv.setText("R. Générale"+" ("+listViewPV.getCount()+")");
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

       /* LineDataSet dataset = new LineDataSet(group1, "Nombre de présents");
        LineData data = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setData(data);
        lineChart.animateY(5000);*/

        listViewPV = (ListView) findViewById(R.id.listViewPV);
        listViewPV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final View v_child = getViewByPosition(i, listViewPV);

                TextView txturl_photo = (TextView) v_child.findViewById(R.id.url_photo);

                Intent intent = new Intent(RespoConsulterPV.this, ViewFullImage.class);
                String url_photo = txturl_photo.getText().toString();
                intent.putExtra("provenance","pv");
                intent.putExtra("URL", url_photo);
                startActivity(intent);
            }
        });
        listViewPV.setAdapter(adapter);


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
                        Toast.makeText(RespoConsulterPV.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json){
        ParseJSONpv pj = new ParseJSONpv(json);
        pj.parseJSON();
        RespoConsulterPVList cl = new RespoConsulterPVList(this,ParseJSONpv.id,ParseJSONpv.ajoute_par, ParseJSONpv.nombre_presents,ParseJSONpv.detail_pv,
                ParseJSONpv.url_photo);
        listViewPV.setAdapter(cl);
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
