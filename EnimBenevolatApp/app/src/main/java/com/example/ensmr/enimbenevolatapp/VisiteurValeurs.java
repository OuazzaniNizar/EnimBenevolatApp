package com.example.ensmr.enimbenevolatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VisiteurValeurs extends AppCompatActivity {
    int nombre_photo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visiteur_valeurs);


        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle = (TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.portail_esprit);
        ImageView myimage = (ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);
    }

    public void buttonClick(View view) {
        switch (view.getId()){
            case R.id.esprit_equipe:
                /*LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view_image = inflater.inflate(R.layout.row_view_full_image, null, false);
                final ImageView imageView = (ImageView) view_image.findViewById(R.id.imageViewFull);
                final Button button_left = (Button) view_image.findViewById(R.id.button_left);
                button_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nombre_photo++;
                        switch (nombre_photo) {
                            case 1:
                                imageView.setImageResource(R.drawable.photo1);
                                break;
                            case 2:
                                imageView.setImageResource(R.drawable.photo2);
                                break;
                            case 3:
                                imageView.setImageResource(R.drawable.photo3);
                                break;
                            case 4:
                                imageView.setImageResource(R.drawable.photo4);
                                break;
                            case 5:
                                imageView.setImageResource(R.drawable.photo5);
                                break;
                            case 6:
                                imageView.setImageResource(R.drawable.photo6);
                                break;
                            case 7:
                                imageView.setImageResource(R.drawable.photo7);
                                break;
                            case 8:
                                imageView.setImageResource(R.drawable.photo8);
                                break;
                            case 9:
                                imageView.setImageResource(R.drawable.photo9);
                                break;
                            case 10:
                                imageView.setImageResource(R.drawable.photo10);
                                nombre_photo = 0;
                                break;

                        }
                    }
                });
                new AlertDialog.Builder(VisiteurValeurs.this).setView(view_image)
                        .show();
                */
                Intent intent=new Intent(VisiteurValeurs.this,ViewFullImage.class);
                intent.putExtra("provenance","valeurs");
                startActivity(intent);
                break;

        }
    }
}
