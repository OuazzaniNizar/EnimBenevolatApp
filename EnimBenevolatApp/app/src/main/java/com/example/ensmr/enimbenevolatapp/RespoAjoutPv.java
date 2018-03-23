package com.example.ensmr.enimbenevolatapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RespoAjoutPv extends AppCompatActivity {

    private EditText nombre_presents,num_reunion,contexte_pv,detail_pv,dist_tache,ajoute_par;
    private Button register_button,date_reunion,ajout_photo;
    String date,rapport;
    List<String> listRapport = new ArrayList<String>();
    MaterialSpinner type_reunion;
    String type_reun;


    private ImageView photo_reunion;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private static final String REGISTER_URL = "http://clubelm.net/EnimBenevolatApp/ajouter_pv.php";
    private AlertDialog progressDialog;
    private String file = "fichier_nom",file_nb_presents = "fichier_nb_presents", file_presents = "fichier_presents";
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_ajout_pv);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.ajout_pv);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        ajoute_par=(EditText) findViewById(R.id.nom);
        nombre_presents=(EditText) findViewById(R.id.nombre_presents);
        num_reunion=(EditText) findViewById(R.id.num_reunion);
        contexte_pv=(EditText) findViewById(R.id.contexte_pv);
        detail_pv=(EditText) findViewById(R.id.detail_pv);
        dist_tache=(EditText) findViewById(R.id.dist_tache);
        register_button=(Button) findViewById(R.id.register_button);
        date_reunion=(Button) findViewById(R.id.date_reunion);
        ajout_photo=(Button) findViewById(R.id.ajout_photo_reunion);
        photo_reunion=(ImageView) findViewById(R.id.photo_reunion);



        try{
            FileInputStream fin = openFileInput(file);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            ajoute_par.setText(temp);
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),R.string.erreur_absence_fichier,Toast.LENGTH_LONG).show();
        }


        try{
            FileInputStream fin = openFileInput(file_nb_presents);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            nombre_presents.setText(temp);
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),R.string.erreur_absence_fichier,Toast.LENGTH_LONG).show();
        }

        try{
            FileInputStream fin = openFileInput(file_presents);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            detail_pv.setText("\n\n"+temp);
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),R.string.erreur_absence_fichier,Toast.LENGTH_LONG).show();
        }

        type_reunion = (MaterialSpinner) findViewById(R.id.type_reunion);
        type_reunion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                type_reun=arg0.getItemAtPosition(arg2).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        date_reunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view_date = inflater.inflate(R.layout.row_date_picker, null, false);
                final DatePicker myDatePicker = (DatePicker) view_date.findViewById(R.id.myDatePicker);
                final EditText  lieu= (EditText) view_date.findViewById(R.id.lieu);
                myDatePicker.setCalendarViewShown(false);
                new AlertDialog.Builder(RespoAjoutPv.this).setView(view_date)
                        .setTitle("Date et lieu de la réunion")
                        .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                int month = myDatePicker.getMonth() + 1;
                                int day = myDatePicker.getDayOfMonth();
                                int year = myDatePicker.getYear();
                                date=month + "-" + day + "-" + year;
                                dialog.cancel();
                                listRapport.add("Date de la réunion : "+date+"\nLieu de la réunion :"+lieu.getText().toString());
                                date_reunion.setText("Fait le : "+date+", à : "+lieu.getText().toString());
                            }
                        }).show();
            }
        });


        /*ajout_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });*/




        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection() == true) {
                    if (nombre_presents.getText().toString().equals("") || contexte_pv.getText().toString().equals("")|| dist_tache.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), R.string.erreur_vide, Toast.LENGTH_SHORT).show();
                    } else {
                        listRapport.add("\n-PV de la réunion N°: "+num_reunion.getText().toString());
                        listRapport.add("\n-Ordre du jour : "+contexte_pv.getText().toString());
                        listRapport.add("\n-Détails du PV:\n"+detail_pv.getText().toString());
                        listRapport.add("\n-Distribution des tâches:\n"+dist_tache.getText().toString());
                        rapport= TextUtils.join("", listRapport);
                        register(nombre_presents.getText().toString(),rapport,type_reun);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.erreur_connexion, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photo_reunion.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }




    private void register(String nombre_presents, String rapport, String type_reun) {

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
                data.put("nombre_presents",params[0]);
                data.put("rapport",params[1]);
                data.put("ajoute_par",params[2]);
                data.put("type_reunion",params[3]);
                //String uploadImage = getStringImage(bitmap);
                //data.put("image", uploadImage);
                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(nombre_presents,rapport,ajoute_par.getText().toString(),type_reun);
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
