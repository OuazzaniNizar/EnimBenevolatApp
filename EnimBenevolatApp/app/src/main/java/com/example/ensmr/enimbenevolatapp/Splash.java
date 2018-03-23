package com.example.ensmr.enimbenevolatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        session = new Session(Splash.this);

        new Handler().postDelayed(new Runnable() {

            @SuppressLint("PrivateResource")
            @Override
            public void run() {
                if (session.getLoggedIn()) {
                    Intent i = new Intent(Splash.this,
                            EspaceMembre.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(Splash.this,
                            EspaceVisiteur.class);
                    startActivity(i);
                    finish();
                }

            }

        }, 3000);
    }
}
