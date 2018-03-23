package com.example.ensmr.enimbenevolatapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by bbbbb on 19/09/2016.
 */
public class RespoConsulterMembresList extends ArrayAdapter{
    private String[] id_membre;
    private String[] nom_membre;
    private String[] filiere_membre;
    private String[] telephone_membre;
    private String[] taches_membre;
    private String[] presence_membre;


        private Activity context;

        public RespoConsulterMembresList(Activity context, String[] id_membre, String[] nom_membre, String[] filiere_membre, String[] telephone_membre
                , String[] taches_membre, String[] presence_membre) {
            super(context, R.layout.row_view_membre, nom_membre);
            this.context = context;
            this.id_membre = id_membre;
            this.nom_membre = nom_membre;
            this.filiere_membre = filiere_membre;
            this.telephone_membre = telephone_membre;
            this.taches_membre = taches_membre;
            this.presence_membre = presence_membre;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.row_view_membre, null, true);
            TextView textView_id = (TextView) listViewItem.findViewById(R.id.id_membre);
            TextView textView_nom_membre = (TextView) listViewItem.findViewById(R.id.nom_membre);
            TextView textView_filiere_membre = (TextView) listViewItem.findViewById(R.id.filiere_membre);
            TextView textView_telephone_membre = (TextView) listViewItem.findViewById(R.id.telephone_membre);
            TextView textView_tache_membre = (TextView) listViewItem.findViewById(R.id.taches_membre);
            TextView textView_presence_membre = (TextView) listViewItem.findViewById(R.id.presence_membre);

            textView_id.setText(id_membre[position]);
            textView_nom_membre.setText(nom_membre[position]);
            textView_filiere_membre.setText(filiere_membre[position]);
            textView_telephone_membre.setText(telephone_membre[position]);
            textView_tache_membre.setText(taches_membre[position]);
            textView_presence_membre.setText(presence_membre[position]);

            float tache = Float.parseFloat(textView_tache_membre.getText().toString());
            float presence = Float.parseFloat(textView_presence_membre.getText().toString());
            if((tache>3 && tache<=6) || (presence>3 && presence<=6)){
                textView_tache_membre.setTextColor(Color.rgb(255,65,130));
                textView_presence_membre.setTextColor(Color.rgb(255,65,130));
            }
            if((tache>6 && tache<=9) || (presence>6 && presence<=9)){
                textView_tache_membre.setTextColor(Color.rgb(255,165,130));
                textView_presence_membre.setTextColor(Color.rgb(255,165,130));
            }
            if(tache>9 || presence>9){
                textView_tache_membre.setTextColor(Color.rgb(255,20,130));
                textView_presence_membre.setTextColor(Color.rgb(255,20,130));
            }
            return listViewItem;
        }
    }
