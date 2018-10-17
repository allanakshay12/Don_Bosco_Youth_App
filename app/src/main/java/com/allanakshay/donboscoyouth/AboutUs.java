package com.allanakshay.donboscoyouth;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageSet.setimage = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        TextView tv1 = (TextView)findViewById(R.id.textView2);
        tv1.setMovementMethod(new ScrollingMovementMethod());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaraboutus);
        setSupportActionBar(toolbar);

        CardView card = (CardView) findViewById(R.id.aboutus_card);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
        card.startAnimation(animation);

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
