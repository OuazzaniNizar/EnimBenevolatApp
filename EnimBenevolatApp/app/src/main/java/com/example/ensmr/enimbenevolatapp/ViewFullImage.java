package com.example.ensmr.enimbenevolatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewFullImage extends AppCompatActivity {
    private TouchImageView imageView;
    int nombre_photo=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_full_image);

        imageView = (TouchImageView) findViewById(R.id.imageViewFull);

        Intent intent_url = getIntent();


        String source=intent_url.getStringExtra("provenance");
            switch (source){
                case "coordonnée":
                    Button button_left = (Button) findViewById(R.id.button_left);
                    button_left.setVisibility(View.VISIBLE);
                    String photo_url="http://clubelm.net/EnimBenevolatApp/documents/ensmr_photo1.jpg";
                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);

                    button_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (nombre_photo){
                                case 1:
                                    String photo_url="http://clubelm.net/EnimBenevolatApp/documents/ensmr_photo1.jpg";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 2:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/ensmr_photo2.jpg";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 3:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/ensmr_photo3.jpg";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 4:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/ensmr_photo4.jpg";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    nombre_photo=0;
                                    break;
                            }
                            nombre_photo++;

                        }
                    });
                    break;

                case "stock":
                    String i_url=intent_url.getStringExtra("URL");
                    PicassoClient.downloadImage(getApplicationContext(),i_url,imageView);
                    break;

                case "pv":
                    i_url=intent_url.getStringExtra("URL");
                    PicassoClient.downloadImage(getApplicationContext(),i_url,imageView);
                    break;

                case "action":
                    i_url=intent_url.getStringExtra("URL");
                    PicassoClient.downloadImage(getApplicationContext(),i_url,imageView);
                    break;

                case "valeurs":
                    button_left = (Button) findViewById(R.id.button_left);
                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo1.JPG";
                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                    button_left.setVisibility(View.VISIBLE);
                    button_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (nombre_photo) {
                                case 1:
                                    String photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo1.JPG";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 2:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo2.JPG";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 3:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo3.JPG";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 4:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo4.JPG";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 5:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo5.jpg";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 6:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo6.JPG";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 7:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo7.JPG";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 8:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo8.jpg";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 9:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo9.JPG";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    break;
                                case 10:
                                    photo_url="http://clubelm.net/EnimBenevolatApp/documents/photo10.JPG";
                                    PicassoClient.downloadImage(getApplicationContext(),photo_url,imageView);
                                    nombre_photo = 0;
                                    break;
                            }
                            nombre_photo++;

                        }
                    });
                    break;

            }

    }
}
