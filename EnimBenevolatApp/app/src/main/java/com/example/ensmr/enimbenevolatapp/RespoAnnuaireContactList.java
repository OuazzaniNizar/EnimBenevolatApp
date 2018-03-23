package com.example.ensmr.enimbenevolatapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by bbbbb on 19/09/2016.
 */
public class RespoAnnuaireContactList extends ArrayAdapter{
    private String[] id;
    private String[] nomcontact;
    private String[] domaine;
    private String[] telephone;
    private String[] renseignements;

        private Activity context;

        public RespoAnnuaireContactList(Activity context,String[] id, String[] nomcontact, String[] domaine, String[] telephone, String[] renseignements) {
            super(context, R.layout.row_view_annuaire, nomcontact);
            this.context = context;
            this.id = id;
            this.nomcontact = nomcontact;
            this.domaine = domaine;
            this.telephone = telephone;
            this.renseignements = renseignements;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.row_view_annuaire, null, true);
            TextView textView_id = (TextView) listViewItem.findViewById(R.id.id_contact);
            TextView textView_nomcontact = (TextView) listViewItem.findViewById(R.id.nom_contact);
            TextView textView_organisme = (TextView) listViewItem.findViewById(R.id.organisme);
            TextView textView_telephone = (TextView) listViewItem.findViewById(R.id.telephone_contact);
            TextView textView_renseignements = (TextView) listViewItem.findViewById(R.id.renseignements);

            textView_id.setText(id[position]);
            textView_nomcontact.setText(nomcontact[position]);
            textView_organisme.setText(domaine[position]);
            textView_telephone.setText(telephone[position]);
            textView_renseignements.setText(renseignements[position]);

            return listViewItem;
        }
    }
