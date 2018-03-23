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
public class RespoConsulterPVList extends ArrayAdapter{
    private String[] id;
    private String[] ajoute_par;
    private String[] nombre_presents;
    private String[] detail_pv;
    private String[] url_photo;

        private Activity context;

        public RespoConsulterPVList(Activity context, String[] id,String[] ajoute_par, String[] nombre_presents, String[] detail_pv, String[] url_photo) {
            super(context, R.layout.row_view_pv, nombre_presents);
            this.context = context;
            this.id = id;
            this.ajoute_par = ajoute_par;
            this.nombre_presents = nombre_presents;
            this.detail_pv = detail_pv;
            this.url_photo = url_photo;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.row_view_pv, null, true);
            TextView textView_id_pv = (TextView) listViewItem.findViewById(R.id.id_pv);
            TextView textView_ajoute_par = (TextView) listViewItem.findViewById(R.id.ajoute_par);
            TextView textView_nombre_presents = (TextView) listViewItem.findViewById(R.id.nombre_presents);
            TextView textView_detail_pv = (TextView) listViewItem.findViewById(R.id.detail_pv);
            TextView textView_url_phot = (TextView) listViewItem.findViewById(R.id.url_photo);

            textView_id_pv.setText(id[position]);
            textView_ajoute_par.setText(ajoute_par[position]);
            textView_nombre_presents.setText(nombre_presents[position]);
            textView_detail_pv.setText(detail_pv[position]);
            textView_url_phot.setText(url_photo[position]);

            return listViewItem;
        }
    }
