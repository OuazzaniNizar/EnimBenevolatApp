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
public class RespoConsulterPropositionsList extends ArrayAdapter{
    private String[] propositions;

        private Activity context;

        public RespoConsulterPropositionsList(Activity context, String[] propositions) {
            super(context, R.layout.row_view_proposition, propositions);
            this.context = context;
            this.propositions = propositions;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.row_view_proposition, null, true);
            TextView textView_propositions = (TextView) listViewItem.findViewById(R.id.propositions);

            textView_propositions.setText(propositions[position]);

            return listViewItem;
        }
    }
