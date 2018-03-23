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
public class RespoConsulterHistoriqueList extends ArrayAdapter{
    private String[] id;
    private String[] programme_action;
    private String[] name_action;
    private String[] details_action;
    private String[] url_photo;

        private Activity context;

        public RespoConsulterHistoriqueList(Activity context, String[] id, String[] programme_action, String[] name_action, String[] details_action, String[] url_photo) {
            super(context, R.layout.row_view_pv, programme_action);
            this.context = context;
            this.id = id;
            this.programme_action = programme_action;
            this.name_action = name_action;
            this.details_action = details_action;
            this.url_photo = url_photo;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.row_view_pv, null, true);
            TextView textView_id_historique = (TextView) listViewItem.findViewById(R.id.id_historique);
            TextView textView_programme_action = (TextView) listViewItem.findViewById(R.id.programme_action);
            TextView textView_name_action = (TextView) listViewItem.findViewById(R.id.name_action);
            TextView textView_details_action = (TextView) listViewItem.findViewById(R.id.details_action);
            TextView textView_url_phot = (TextView) listViewItem.findViewById(R.id.url_photo);

            textView_id_historique.setText(id[position]);
            textView_programme_action.setText(programme_action[position]);
            textView_name_action.setText(name_action[position]);
            textView_details_action.setText(details_action[position]);
            textView_url_phot.setText(url_photo[position]);

            return listViewItem;
        }
    }
