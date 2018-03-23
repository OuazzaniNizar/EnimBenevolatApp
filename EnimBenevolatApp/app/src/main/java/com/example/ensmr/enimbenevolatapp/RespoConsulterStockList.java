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
public class RespoConsulterStockList extends ArrayAdapter{
    private String[] id_stock;
    private String[] ajoute_par_stock;
    private String[] rapport_stock;
    private String[] date_stock;
    private String[] url_photo_stock;

        private Activity context;

        public RespoConsulterStockList(Activity context, String[] id_stock, String[] ajoute_par_stock, String[] rapport_stock, String[] date_stock, String[] url_photo_stock) {
            super(context, R.layout.row_view_stock, ajoute_par_stock);
            this.context = context;
            this.id_stock = id_stock;
            this.ajoute_par_stock = ajoute_par_stock;
            this.rapport_stock = rapport_stock;
            this.date_stock = date_stock;
            this.url_photo_stock = url_photo_stock;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.row_view_stock, null, true);
            TextView textView_id_stock = (TextView) listViewItem.findViewById(R.id.id_stock);
            TextView textView_ajoute_par_stock = (TextView) listViewItem.findViewById(R.id.ajoute_par_stock);
            TextView textView_rapport_stock = (TextView) listViewItem.findViewById(R.id.rapport_stock);
            TextView textView_date_stock = (TextView) listViewItem.findViewById(R.id.date_stock);
            TextView textView_url_photo_stock = (TextView) listViewItem.findViewById(R.id.url_photo_stock);

            textView_id_stock.setText(id_stock[position]);
            textView_ajoute_par_stock.setText(ajoute_par_stock[position]);
            textView_rapport_stock.setText(rapport_stock[position]);
            textView_date_stock.setText(date_stock[position]);
            textView_url_photo_stock.setText(url_photo_stock[position]);

            return listViewItem;
        }
    }
