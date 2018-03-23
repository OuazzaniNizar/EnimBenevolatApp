package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
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

import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class RespoConsulterStock extends AppCompatActivity {
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/consulter_stock.php";

    private AlertDialog progressDialog;
    private ListView listviewStock;
    private Button consulter_stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_consulter_stock);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.consulter_stock);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        listviewStock = (ListView) findViewById(R.id.listviewStock);


        consulter_stock = (Button) findViewById(R.id.consulter_stock);
        consulter_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetworkConnection()==true){
                    sendRequest();
                    consulter_stock.setText("CONSULTER LES ELEMENTS DU STOCK"+" ("+listviewStock.getCount()+")");
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        listviewStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final View v_child = getViewByPosition(i, listviewStock);
                TextView txtid_url_photo_stock = (TextView) v_child.findViewById(R.id.url_photo_stock);

                Intent intent = new Intent(RespoConsulterStock.this, ViewFullImage.class);
                String url_photo = txtid_url_photo_stock.getText().toString();
                intent.putExtra("provenance","stock");
                intent.putExtra("URL", url_photo);
                startActivity(intent);

                /*LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view_image = inflater.inflate(R.layout.row_view_full_image, null, false);
                final ImageView imageView = (ImageView) view_image.findViewById(R.id.imageViewFull);
                final Button button_left = (Button) view_image.findViewById(R.id.button_left);
                button_left.setVisibility(View.GONE);
                PicassoClient.downloadImage(getApplicationContext(),txtid_url_photo_stock.getText().toString(),imageView);
                new AlertDialog.Builder(RespoConsulterStock.this).setView(view_image)
                        .show();*/
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
                        Toast.makeText(RespoConsulterStock.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json){
        ParseJSONStock pj = new ParseJSONStock(json);
        pj.parseJSON();
        RespoConsulterStockList cl = new RespoConsulterStockList(this,ParseJSONStock.id_stock,ParseJSONStock.ajoute_par_stock, ParseJSONStock.rapport_stock,ParseJSONStock.date_stock,
                ParseJSONStock.url_photo_stock);
        listviewStock.setAdapter(cl);
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
