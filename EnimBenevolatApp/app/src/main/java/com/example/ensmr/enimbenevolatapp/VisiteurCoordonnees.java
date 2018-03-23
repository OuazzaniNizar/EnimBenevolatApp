package com.example.ensmr.enimbenevolatapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class VisiteurCoordonnees extends AppCompatActivity {
    ImageView image_phone, image_email, image_adresse,image_fb,image_photo;
    //static final LatLng LOCAL_ASS = new LatLng(33.978586,-6.894059);
    //static final LatLng COMPLEXE_MANAL = new LatLng(33.976475,-6.887770);
    LinearLayout map_layout;
    int nombre_photo=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visiteur_coordonnees);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle = (TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.nous_contacter);
        ImageView myimage = (ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);


        image_email = (ImageView) findViewById(R.id.image_email);
        image_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });


        image_phone = (ImageView) findViewById(R.id.image_phone);
        image_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });


        image_fb = (ImageView) findViewById(R.id.image_fb);
        image_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "https://web.facebook.com/EnimBenevolat/");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                try
                {
                    startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                        map_layout.setVisibility(View.VISIBLE);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        Toast.makeText(getApplicationContext(), "Vous n'avez pas l'application Google Chrome installée sur votre mobile !", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        image_adresse = (ImageView) findViewById(R.id.image_adresse);
        image_adresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = 34.001168;
                double longitude = -6.854492;
                String label = "Ecole Nationale Supérieure des Mines de Rabat";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try
                {
                    startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
                        startActivity(unrestrictedIntent);
                        map_layout.setVisibility(View.VISIBLE);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        Toast.makeText(getApplicationContext(), "Vous n'avez pas l'application Google Maps installée sur votre mobile !", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        image_photo=(ImageView)findViewById(R.id.image_photo);
        image_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view_image = inflater.inflate(R.layout.row_view_full_image, null, false);
                final ImageView imageView = (ImageView) view_image.findViewById(R.id.imageViewFull);
                final Button button_left = (Button) view_image.findViewById(R.id.button_left);
                button_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            nombre_photo++;
                            switch (nombre_photo){
                                case 1:
                                    imageView.setImageResource(R.drawable.ensmr_photo1);
                                    break;
                                case 2:
                                    imageView.setImageResource(R.drawable.ensmr_photo2);
                                    break;
                                case 3:
                                    imageView.setImageResource(R.drawable.ensmr_photo3);
                                    nombre_photo=0;
                                    break;
                            }

                    }
                });
                new AlertDialog.Builder(VisiteurCoordonnees.this).setView(view_image)
                        .show();
                */
                Intent intent=new Intent(VisiteurCoordonnees.this,ViewFullImage.class);
                intent.putExtra("provenance","coordonnée");

                startActivity(intent);
            }
        });
    }


    protected void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "enimbenevolat@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(emailIntent, "Veuillez choisir votre application de messagerie"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(VisiteurCoordonnees.this, "Aucune application de messagerie n'est installée.", Toast.LENGTH_SHORT).show();
        }
    }


    private void makePhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+"0678424350"));//change the number
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        startActivity(callIntent);
    }


}
