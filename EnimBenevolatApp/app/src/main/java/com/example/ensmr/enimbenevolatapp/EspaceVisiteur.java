package com.example.ensmr.enimbenevolatapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EspaceVisiteur extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.espace_visiteur);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolBar);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.portail_visiteur);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

    }


    public void buttonClick(View view) {
        switch (view.getId()){
            case R.id.portail_proposition:
                Intent Proposition = new Intent(this, VisiteurPropositions.class);
                startActivity(Proposition);
                break;
            case R.id.portail_valeur:
                Intent VisiteurValeurs = new Intent(this, VisiteurValeurs.class);
                startActivity(VisiteurValeurs);
                break;
            case R.id.portail_documents:
                Intent VisiteurDocuments = new Intent(this, VisiteurDocuments.class);
                startActivity(VisiteurDocuments);
                break;
            case R.id.portail_historique:
                Intent VisiteurHistorique = new Intent(this, RespoConsulterHistorique.class);
                startActivity(VisiteurHistorique);
                break;
            case R.id.portail_coordonnee:
                Intent VisiteurCoordonnees = new Intent(this, VisiteurCoordonnees.class);
                startActivity(VisiteurCoordonnees);
                break;
            case R.id.portail_membres:
                Intent MembreLogin = new Intent(this, MembreLogin.class);
                startActivity(MembreLogin);
                break;
        }
    }
}
