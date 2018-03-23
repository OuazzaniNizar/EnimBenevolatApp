package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RespoAjoutStock extends AppCompatActivity {

    private EditText nom,provenance,inventaires;
    private Button register_button,ajout_photo;
    String type_action,date,rapport;
    List<String> listRapport = new ArrayList<String>();
    MaterialSpinner spinner_action;



    private ImageView photo_stock;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/ajouter_stock.php";
    private AlertDialog progressDialog;
    private String file = "fichier_nom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_ajout_stock);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.portail_ajout_stock);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        nom=(EditText) findViewById(R.id.nom_stock);
        provenance=(EditText) findViewById(R.id.stock_provenance);
        inventaires=(EditText) findViewById(R.id.detail_composant);

        register_button=(Button) findViewById(R.id.register_button);
        ajout_photo=(Button) findViewById(R.id.ajout_photo_stock);

        photo_stock=(ImageView) findViewById(R.id.photo_stock);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        date = sdf.format(c.getTime());


        try{
            FileInputStream fin = openFileInput(file);
            int ch;
            String temp="";

            while( (ch = fin.read()) != -1){
                temp = temp + Character.toString((char)ch);
            }
            nom.setText(temp);
        }
        catch(Exception e){
        }

        ajout_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        spinner_action = (MaterialSpinner) findViewById(R.id.spinner_action);
        spinner_action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                type_action=arg0.getItemAtPosition(arg2).toString();
                switch (type_action){
                    case "Ajouté au stock":
                        listRapport.add("<<    AJOUT AU STOCK   >>");

                        break;
                    case "Retiré du stock":
                        listRapport.add("<<    RETRAIT DU STOCK    >>");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection() == true) {
                    if (nom.getText().toString().equals("") || provenance.getText().toString().equals("")|| inventaires.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), R.string.erreur_vide, Toast.LENGTH_SHORT).show();
                    } else {
                        listRapport.add("\nEn provenance de: "+provenance.getText().toString());
                        listRapport.add("\nInventaire des éléments ajoutés au stock:\n"+inventaires.getText().toString());
                        rapport= TextUtils.join("", listRapport);

                        register(nom.getText().toString(),rapport);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photo_stock.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }




    private void register(String nom, String rapport) {

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
                data.put("ajoute_par",params[0]);
                data.put("rapport",params[1]);
                String uploadImage = getStringImage(bitmap);
                data.put("image", uploadImage);
                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(nom,rapport);
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
