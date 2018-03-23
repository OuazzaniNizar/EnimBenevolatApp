package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class RespoConsulterTresorerie extends AppCompatActivity {
    private static String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/consulter_transaction.php";
    ;
    private AlertDialog progressDialog;
    float somme_personnalisee = 0, somme_debit = 0, somme_credit = 0, somme_total = 0;

    private ListView listViewTresorerie;
    private Button consulter_tresorerie,tresorerie_graphe;
    private TextView tresorerie_total, tresorerie_debit, tresorerie_credit, tresorerie_personnalisee;
    TextView somme_transaction, type_transaction;
    ArrayAdapter<String> adapter;
    RespoConsulterTresorerieListe myadapter;


    List<String> trans_selec = new ArrayList<String>();
    String [] transactions;
    float []cumullist;
    float cumul=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_consulter_tresorerie);



        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle = (TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.consulter_tresorerie);
        ImageView myimage = (ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        final BarChart barChart = (BarChart) findViewById(R.id.chart);

        final ArrayList<BarEntry> group1 = new ArrayList<>();
        final ArrayList<BarEntry> group2 = new ArrayList<>();
        final ArrayList<BarEntry> group3 = new ArrayList<>();


        tresorerie_total = (TextView) findViewById(R.id.tresorerie_total);
        tresorerie_debit = (TextView) findViewById(R.id.tresorerie_debit);
        tresorerie_credit = (TextView) findViewById(R.id.tresorerie_credit);
        tresorerie_personnalisee = (TextView) findViewById(R.id.tresorerie_personnalisee);


        consulter_tresorerie = (Button) findViewById(R.id.consulter_tresorerie);
        consulter_tresorerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection() == true) {
                    sendRequest();
                    barChart.setVisibility(View.GONE);
                    transactions=new String[listViewTresorerie.getCount()];
                    cumullist=new float[listViewTresorerie.getCount()];

                    for (int i = listViewTresorerie.getCount()-1; i >=0 ; i--) {
                        View v = getViewByPosition(i, listViewTresorerie);
                        type_transaction = (TextView) v.findViewById(R.id.type_transaction);
                        somme_transaction = (TextView) v.findViewById(R.id.somme_transaction);

                        transactions[i]=somme_transaction.getText().toString();
                        float somme= Float.parseFloat(transactions[i]);
                        cumul=cumul+somme;
                        cumullist[i]=cumul;
                        group3.add(new BarEntry(cumullist[i], listViewTresorerie.getCount()-1-i));
                        if(somme>=0){
                            group1.add(new BarEntry(somme, listViewTresorerie.getCount()-1-i));
                        }else {
                            group2.add(new BarEntry(somme, listViewTresorerie.getCount()-1-i));
                        }

                        BarDataSet barDataSet1 = new BarDataSet(group1, "Débit");
                        barDataSet1.setColor(Color.rgb(255, 65, 130));
                        //barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

                        BarDataSet barDataSet2 = new BarDataSet(group2, "Crédit");
                        barDataSet2.setColor(Color.rgb(0, 160, 230));
                        //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

                        BarDataSet barDataSet3 = new BarDataSet(group3, "Cumul");
                        barDataSet3.setColor(Color.rgb(160, 160, 160));

                        ArrayList<BarDataSet> dataSets = new ArrayList<>();
                        dataSets.add(barDataSet1);
                        dataSets.add(barDataSet2);
                        dataSets.add(barDataSet3);


                        BarData data = new BarData(getXAxisValues(), dataSets);
                        barChart.setData(data);
                        barChart.setDescription("Liste des transactions");
                        barChart.animateXY(2000, 2000);
                        barChart.invalidate();

                        if (type_transaction.getText().toString().equals("Crédit (-)")) {
                            somme_credit = somme_credit + Float.parseFloat(somme_transaction.getText().toString());
                        }
                        if (type_transaction.getText().toString().equals("Débit (+)")) {
                            somme_debit = somme_debit + Float.parseFloat(somme_transaction.getText().toString());
                        }
                    }
                    consulter_tresorerie.setText("NOMBRE DE TRANSACTIONS" + " (" + listViewTresorerie.getCount() + ")");
                    tresorerie_debit.setText("Debité: " + somme_debit);
                    tresorerie_credit.setText("Crédité: " + somme_credit);
                    somme_total = somme_debit + somme_credit;
                    tresorerie_total.setText("Total: " + somme_total);
                    somme_credit = 0;
                    somme_debit = 0;
                    somme_total = 0;
                } else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });



        tresorerie_graphe = (Button) findViewById(R.id.tresorerie_graphe);
        barChart.setVisibility(View.GONE);
        tresorerie_graphe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barChart.setVisibility(View.VISIBLE);
            }
        });





        listViewTresorerie = (ListView) findViewById(R.id.listViewTresorerie);
        listViewTresorerie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final View v_child = getViewByPosition(i, listViewTresorerie);
                if (v_child != null) {
                    somme_transaction = (TextView) v_child.findViewById(R.id.somme_transaction);
                    float somme=Float.parseFloat(somme_transaction.getText().toString());
                    if(somme>0){
                        trans_selec.add("+"+somme);
                        tresorerie_personnalisee.setTextColor(Color.rgb(0,160,227));
                    }else {
                        trans_selec.add(""+somme);
                        tresorerie_personnalisee.setTextColor(Color.rgb(255,65,130));
                    }
                    somme_personnalisee = somme_personnalisee + Float.parseFloat(somme_transaction.getText().toString());
                    tresorerie_personnalisee.setText(TextUtils.join("", trans_selec)+"="+ somme_personnalisee);
                } else {
                    Toast.makeText(getApplicationContext(), "fin liste ! (:", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewTresorerie.setAdapter(myadapter);




    }


    private ArrayList<String> getXAxisValues() {
        ArrayList<String> labels = new ArrayList<>();
        for (int j=1;j<listViewTresorerie.getCount()+1;j++){
           String num_trans=""+j;
            labels.add("Trans : "+num_trans);
        }
        return labels;
    }


    private void sendRequest() {
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
                        Toast.makeText(RespoConsulterTresorerie.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) {
        ParseJSONTransaction pj = new ParseJSONTransaction(json);
        pj.parseJSON();
        RespoConsulterTresorerieListe cl = new RespoConsulterTresorerieListe(this, ParseJSONTransaction.type_transaction, ParseJSONTransaction.somme_transaction, ParseJSONTransaction.detail_transaction);
        listViewTresorerie.setAdapter(cl);
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
