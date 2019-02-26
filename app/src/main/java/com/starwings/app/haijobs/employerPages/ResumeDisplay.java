package com.starwings.app.haijobs.employerPages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.starwings.app.haijobs.HaiJobsActivity;
import com.starwings.app.haijobs.R;

public class ResumeDisplay extends HaiJobsActivity {
    String link;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_viewer);

        link = getIntent().getStringExtra("fileLink");

        WebView urlWebView = (WebView) findViewById(R.id.resumeviewer);
        urlWebView.setWebViewClient(new AppWebViewClients());
        urlWebView.getSettings().setJavaScriptEnabled(true);
        urlWebView.getSettings().setUseWideViewPort(true);
        urlWebView.getSettings().setPluginState(WebSettings.PluginState.ON);


        String pdfURL = "http://dl.dropboxusercontent.com/u/37098169/Course%20Brochures/AND101.pdf";
        urlWebView.loadUrl(
                "http://docs.google.com/gview?embedded=true&url=" + link);


    }
    public class AppWebViewClients extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }
}
