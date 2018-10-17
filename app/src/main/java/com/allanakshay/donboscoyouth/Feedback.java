package com.allanakshay.donboscoyouth;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wang.avi.AVLoadingIndicatorView;

public class Feedback extends AppCompatActivity {
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        final AVLoadingIndicatorView avi = (AVLoadingIndicatorView)findViewById(R.id.avi_feedback);
        WebView webView = (WebView)findViewById(R.id.feedbackweb);
        String url = "https://docs.google.com/forms/d/e/1FAIpQLSfXXB-Nr2YXlYWXoZV0bRtrXLx9MLrzdt4wuRupF33fOZHLXw/viewform?usp=sf_link";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                avi.smoothToShow();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                avi.smoothToHide();

            }

        });
        webView.loadUrl(url);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarfeed);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("id", LoginPage.userid);
        outState.putString("connection", CoverPage.connectedstate);
        outState.putString("fb_id", LoginPage.fb_user_id);
        outState.putString("image", ImageSet.setimage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        LoginPage.userid = savedInstanceState.getString("id");
        CoverPage.connectedstate = savedInstanceState.getString("connection");
        LoginPage.fb_user_id = savedInstanceState.getString("fb_id");
        ImageSet.setimage = savedInstanceState.getString("image");
    }

}
