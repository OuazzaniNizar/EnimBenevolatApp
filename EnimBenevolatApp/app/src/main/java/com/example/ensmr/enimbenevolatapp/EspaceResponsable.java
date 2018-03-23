package com.example.ensmr.enimbenevolatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;

public class EspaceResponsable extends AppCompatActivity {

    private String file = "fichier_nom";
    TextView intro_respo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.espace_responsable);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.portail_respo);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        intro_respo=(TextView)findViewById(R.id.intro_respo);
        try{
            FileInputStream fin = openFileInput(file);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            intro_respo.setText("Bienvenue "+temp+" au portail des responsables du club ENIM-Bénévolat (:");
        }
        catch(Exception e){
        }
    }

    public void buttonClick(View view) {
        switch (view.getId()){
            case R.id.portail_historique:
                Intent Proposition = new Intent(this, RespoConsulterHistorique.class);
                startActivity(Proposition);
                break;
            case R.id.portail_tresorerie:
                Intent intent1 = new Intent(this, RespoConsulterTresorerie.class);
                startActivity(intent1);
                break;
            case R.id.portail_annuaire:
                Intent intent2 = new Intent(this, RespoAnnuaireContact.class);
                startActivity(intent2);
                break;
            case R.id.portail_pv:
                Intent intent3 = new Intent(this, RespoConsulterPV.class);
                intent3.putExtra("provenance","respo");
                startActivity(intent3);
                break;
            case R.id.portail_membres:
                Intent intent4 = new Intent(this, ConsulterMembres.class);
                startActivity(intent4);
                break;
            case R.id.portail_stock:
                Intent intent5 = new Intent(this, RespoConsulterStock.class);
                startActivity(intent5);
                break;
            case R.id.portail_propositions:
                Intent intent6 = new Intent(this, RespoConsulterPropositions.class);
                startActivity(intent6);
                break;


            case R.id.portail_ajout_historique:
                Intent RespoAjoutHistorique = new Intent(this, RespoAjoutHistorique.class);
                startActivity(RespoAjoutHistorique);
                break;
            case R.id.portail_ajout_transaction:
                Intent RespoAjoutTransaction = new Intent(this, RespoAjoutTransaction.class);
                startActivity(RespoAjoutTransaction);
                break;
            case R.id.portail_ajout_pv:
                Intent RespoAjoutPv = new Intent(this, RespoAjoutPv.class);
                startActivity(RespoAjoutPv);
                break;
            case R.id.portail_ajout_contact:
                Intent RespoAjoutContact = new Intent(this, RespoAjoutContact.class);
                startActivity(RespoAjoutContact);
                break;
            case R.id.portail_ajout_stock:
                Intent RespoAjoutStock = new Intent(this, RespoAjoutStock.class);
                startActivity(RespoAjoutStock);
                break;
        }
    }
}
