package com.example.ensmr.enimbenevolatapp;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by bbbbb on 19/09/2016.
 */
public class RespoConsulterTresorerieListe extends ArrayAdapter{
    private String[] type_transaction;
    private String[] somme_transaction;
    private String[] detail_transaction;

        private Activity context;
    public class ViewHolder {
        TextView txt_type_transaction, txt_somme_transaction,txt_detail_transaction,somme_row,flux_row;

    }
        public RespoConsulterTresorerieListe(Activity context, String[] type_transaction, String[] somme_transaction, String[] detail_transaction) {
            super(context, R.layout.row_view_transaction, type_transaction);
            this.context = context;
            this.type_transaction = type_transaction;
            this.somme_transaction = somme_transaction;
            this.detail_transaction = detail_transaction;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.row_view_transaction, null, true);
            TextView txt_type_transaction = (TextView) listViewItem.findViewById(R.id.type_transaction);
            final TextView txt_somme_transaction = (TextView) listViewItem.findViewById(R.id.somme_transaction);
            TextView txt_detail_transaction = (TextView) listViewItem.findViewById(R.id.detail_transaction);

            txt_type_transaction.setText(type_transaction[position]);
            txt_somme_transaction.setText(somme_transaction[position]);
            txt_detail_transaction.setText(detail_transaction[position]);


            return listViewItem;*/


            View rowView = convertView;
           ViewHolder viewHolder;

            if (rowView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                rowView = inflater.inflate(R.layout.row_view_transaction, null);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.txt_type_transaction = (TextView) rowView.findViewById(R.id.type_transaction);
                viewHolder.txt_somme_transaction = (TextView) rowView.findViewById(R.id.somme_transaction);
                viewHolder.txt_detail_transaction = (TextView) rowView.findViewById(R.id.detail_transaction);
                viewHolder.somme_row = (TextView) rowView.findViewById(R.id.somme_row);
                viewHolder.flux_row = (TextView) rowView.findViewById(R.id.flux_row);


                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txt_type_transaction.setText(type_transaction[position]);
            viewHolder.txt_somme_transaction.setText(somme_transaction[position]);
            viewHolder.txt_detail_transaction.setText(detail_transaction[position]);
            float somme=Float.parseFloat(viewHolder.txt_somme_transaction.getText().toString());
            if(somme>0){
                viewHolder.somme_row.setTextColor(Color.rgb(0,160,227));
                viewHolder.flux_row.setTextColor(Color.rgb(0,160,227));
            }else {
                viewHolder.somme_row.setTextColor(Color.rgb(255,65,130));
                viewHolder.flux_row.setTextColor(Color.rgb(255,65,130));
            }
            return rowView;

        }


    }
