package com.example.ensmr.enimbenevolatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class VisiteurDocuments extends AppCompatActivity {
    String LinkToPDFreglement="http://clubelm.net//EnimBenevolatApp/documents/Loi%20AEENIM.pdf";
    String LinkToPDFrapport2014_2015="http://clubelm.net//EnimBenevolatApp/documents/Loi%20AEENIM.pdf";
    String LinkToPDFcaravane4="http://clubelm.net//EnimBenevolatApp/documents/Rapport_Activité_CaravaneAl Khayr4.pdf";
    String LinkToPDFteambuilding2="http://clubelm.net//EnimBenevolatApp/documents/Rapport_Activité_TeamBuilding2.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visiteur_documents);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        TextView mytitle = (TextView) findViewById(R.id.toolbar_title);
        mytitle.setText(R.string.portail_documents);
        ImageView myimage = (ImageView) findViewById(R.id.toolbar_logo);
        myimage.setImageResource(R.drawable.ic_logo);

    }


    public void buttonClick(View view) {
        switch (view.getId()){
            case R.id.loi_aeenim:
                WebView mWebView=new WebView(VisiteurDocuments.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+LinkToPDFreglement);
                setContentView(mWebView);
                break;
            case R.id.rapport2014_2015:
                mWebView=new WebView(VisiteurDocuments.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+LinkToPDFrapport2014_2015);
                setContentView(mWebView);
                break;
            case R.id.teambuilding2:
                mWebView=new WebView(VisiteurDocuments.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+LinkToPDFteambuilding2);
                setContentView(mWebView);
                break;
            case R.id.caravane4:
                mWebView=new WebView(VisiteurDocuments.this);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+LinkToPDFcaravane4);
                setContentView(mWebView);
                break;
        }
    }
}


