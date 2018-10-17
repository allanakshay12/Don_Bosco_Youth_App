package com.allanakshay.donboscoyouth;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.util.Calendar;

public class CoverPage extends AppCompatActivity {
    public static String connectedstate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_page);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connectedstate ="Connected";
        }
        else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
            connectedstate = "Disconnected";

        }
        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent("com.allanakshay.donboscoyouth.LoginPage");
                startActivity(intent);
                finish();
            }
        }, 2000);

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
