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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.io.IOException;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RespoAjoutHistorique extends AppCompatActivity {
    EditText action_name,action_details;
    private Button date_action,register_button,ajout_photo_action;
    private AlertDialog progressDialog;
    MaterialSpinner spinner_type_benefice;
    String programme,date;
    private static final String REGISTER_URL = "http://clubelm.net/AlRayaneApp/ajouter_action.php";

    private int PICK_IMAGE_REQUEST = 1;
    private ImageView photo_action1;
    private Bitmap bitmap1;
    private Uri filePath;
    private int nombre_photo=0;
    String uploadImage1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respo_ajout_historique);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.ajout_historique);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        action_name =(EditText) findViewById(R.id.action_name);
        action_details =(EditText) findViewById(R.id.action_details);

        spinner_type_benefice = (MaterialSpinner) findViewById(R.id.spinner1);
        spinner_type_benefice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                programme=arg0.getItemAtPosition(arg2).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        date_action =(Button)findViewById(R.id.date_action);
        date_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view_date = inflater.inflate(R.layout.row_date_picker, null, false);
                final DatePicker myDatePicker = (DatePicker) view_date.findViewById(R.id.myDatePicker);
                myDatePicker.setCalendarViewShown(false);
                new AlertDialog.Builder(RespoAjoutHistorique.this).setView(view_date)
                        .setTitle("Date de l'action")
                        .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                int month = myDatePicker.getMonth() + 1;
                                int day = myDatePicker.getDayOfMonth();
                                int year = myDatePicker.getYear();
                                date=month + "-" + day + "-" + year;
                                dialog.cancel();
                                date_action.setText("Date de l'action : "+date);
                            }
                        }).show();
            }
        });



        photo_action1 = (ImageView) findViewById(R.id.photo_action1);
        ajout_photo_action =(Button)findViewById(R.id.ajout_photo_action);
        ajout_photo_action.setText("Ajouter une "+(nombre_photo+1)+"ère photo");
        ajout_photo_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //ajout_photo_action.setText("Ajouter une "+(nombre_photo+1)+"ème photo");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });



        register_button =(Button)findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection() == true) {
                    if (action_name.getText().toString().equals("") || action_details.getText().toString().equals("") || date.equals("")) {
                        Toast.makeText(getApplicationContext(), R.string.erreur_vide, Toast.LENGTH_SHORT).show();
                    } else {
                        registerUser();
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
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photo_action1.setImageBitmap(bitmap1);
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


    private void registerUser() {
        String name_action = action_name.getText().toString().trim().toLowerCase();
        String details_action = action_details.getText().toString().trim().toLowerCase();
        String programme_action =programme+" - Le "+date ;

        register(programme_action,name_action,details_action);
    }

    private void register(String programme_action, String name_action, String details_action) {

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
                uploadImage1 = getStringImage(bitmap1);
                data.put("programme_action",params[0]);
                data.put("name_action",params[1]);
                data.put("details_action",params[2]);
                data.put("image", uploadImage1);
                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(programme_action,name_action,details_action);
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
