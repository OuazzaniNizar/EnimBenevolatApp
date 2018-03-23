package com.example.ensmr.enimbenevolatapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;

public class EspaceMembre extends AppCompatActivity {
    TextView intro_membre;
    int nb_click=0;
    LinearLayout portail_respo;
    private String file = "fichier_nom";

    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.espace_membre);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mytitle =(TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.portail_membre);
        ImageView myimage=(ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

        portail_respo=(LinearLayout)findViewById(R.id.portail_respo);



        session = new Session(EspaceMembre.this);

        if (!session.getLoggedIn()) {
            logoutUser();
        }

        intro_membre=(TextView)findViewById(R.id.intro_membre);
        intro_membre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(nb_click==4){
                 LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 View view_authentif = inflater.inflate(R.layout.row_view_userpassword, null, false);
                 final EditText password = (EditText) view_authentif.findViewById(R.id.portail_respo_authetification);

                 new AlertDialog.Builder(new ContextThemeWrapper(EspaceMembre.this, R.style.AlerDialog))
                         .setView(view_authentif)
                         .setTitle("Veuillez vous authentifier s'il vous plaît !")
                         .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                             @TargetApi(11)
                             public void onClick(DialogInterface dialog, int id) {
                                switch (password.getText().toString()){
                                    case "respo" :
                                        portail_respo.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(),R.string.devrouillage_reussi, Toast.LENGTH_SHORT).show();
                                        nb_click=0;
                                        break;
                                    case "presence" :
                                        Intent consultermemebre = new Intent(getApplicationContext(), ConsulterMembres.class);
                                        startActivity(consultermemebre);
                                        break;
                                    case "ecrire_pv" :
                                        Intent pv = new Intent(getApplicationContext(), RespoAjoutPv.class);
                                        startActivity(pv);
                                        break;
                                }
                             }
                         }).show();
                 //AlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.bleu);

             }else {
                 nb_click++;
             }
            }
        });

        try{
            FileInputStream fin = openFileInput(file);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            intro_membre.setText("Bienvenue "+temp+" au portail des membres du club ENIM-Bénévolat (:");
        }
        catch(Exception e){
        }
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
            case R.id.log_out:
                logoutUser();
                break;



            case R.id.portail_ajout_transaction:
                Intent RespoAjoutTransaction = new Intent(this, RespoAjoutTransaction.class);
                startActivity(RespoAjoutTransaction);
                break;
            case R.id.portail_ajout_stock:
                Intent RespoAjoutPv = new Intent(this, RespoAjoutStock.class);
                startActivity(RespoAjoutPv);
                break;
            case R.id.portail_ajout_contact:
                Intent RespoAjoutContact = new Intent(this, RespoAjoutContact.class);
                startActivity(RespoAjoutContact);
                break;
            case R.id.portail_respo:
                Intent respo = new Intent(this, EspaceResponsable.class);
                startActivity(respo);
                break;

            case R.id.portail_pv:
                Intent intent3 = new Intent(this, RespoConsulterPV.class);
                intent3.putExtra("provenance","membre");
                startActivity(intent3);
                break;
        }
    }

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(EspaceMembre.this, EspaceVisiteur.class);
        startActivity(intent);
        finish();
    }
}
