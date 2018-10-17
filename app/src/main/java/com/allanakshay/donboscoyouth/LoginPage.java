package com.allanakshay.donboscoyouth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class LoginPage extends AppCompatActivity {


    private CheckBox checkbox;
    public EditText user1,pass1;
    public static String userid;
    Intent intentlog;
    public String user, password;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference ref;
    private DatabaseReference myref;
    private FirebaseDatabase mFirebaseDatabase;
    private ArrayList<String> usernames = new ArrayList<String>();
    private ArrayList<String> passwords = new ArrayList<String>();
    LoginButton loginButton;
    CallbackManager callbackManager;
    public static String fb_user_id;
    ValueEventListener myevent;
    private VideoView videoView;
    private Animation animation;
    private final String PREFERENECE = "DBY-preferenceFile-AccountID";
    private SharedPreferences preference;
    private SharedPreferences preference_prefill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login_page);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_from_bottom);
        checkbox = (CheckBox) findViewById(R.id.checkbox_login);
        checkbox.startAnimation(animation);
        loginButton = (LoginButton)findViewById(R.id.login_button_fb);
        loginButton.startAnimation(animation);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        Calendar cal = Calendar.getInstance();
        bundle.putString("Time", cal.getTime().toString());
        mFirebaseAnalytics.logEvent("App_opened", bundle);
        videoView = (VideoView) findViewById(R.id.background_video);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_video);

        videoView.setDrawingCacheEnabled(true);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        callbackManager = CallbackManager.Factory.create();

        Button guest = (Button)findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = "Just a Guest";
                Toast.makeText(getApplicationContext(), "Logged in as Guest", Toast.LENGTH_SHORT).show();
                intentlog = new Intent(LoginPage.this, MainPage.class);
                startActivity(intentlog);
                ref.removeEventListener(myevent);
                finish();
            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fb_user_id = loginResult.getAccessToken().getUserId();
                myref = mFirebaseDatabase.getInstance().getReference().child("Youth_List");

                myref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            usernames.clear();
                            passwords.clear();
                            int counter = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                usernames.add(snapshot.child("Username").getValue().toString());
                                passwords.add(snapshot.child("Password").getValue().toString());
                                if ((snapshot.child("Facebook_UserID").getValue().toString().trim()).equals(fb_user_id.trim())) {
                                    if (check((snapshot.child("Username").getValue().toString().trim()), (snapshot.child("Password").getValue().toString().trim()))) {

                                    }
                                    counter++;
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    intentlog = new Intent(LoginPage.this, MainPage.class);
                                    startActivity(intentlog);
                                    ref.removeEventListener(myevent);
                                    LoginManager.getInstance().logOut();
                                    finish();
                                }

                            }
                            if (counter == 0) {
                                Toast.makeText(getApplicationContext(), "Account not linked yet", Toast.LENGTH_SHORT).show();
                                intentlog = new Intent(LoginPage.this, FB_Link.class);
                                startActivity(intentlog);
                                ref.removeEventListener(myevent);
                                LoginManager.getInstance().logOut();
                                finish();
                            }
                        }catch(Exception e) {}

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();


            }
        });
        ref = mFirebaseDatabase.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

        ref.addListenerForSingleValueEvent(myevent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernames.clear();
                passwords.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    usernames.add(snapshot.child("Username").getValue().toString());
                    passwords.add(snapshot.child("Password").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button update = (Button)findViewById(R.id.updatelogin);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentlog = new Intent (LoginPage.this, Update.class);
                startActivity(intentlog);
            }
        });
        update.startAnimation(animation);
        Button join = (Button)findViewById(R.id.joinuslogin);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentlog = new Intent(LoginPage.this, JoinUs.class);
                startActivity(intentlog);
            }
        });
        join.startAnimation(animation);
        Button about = (Button)findViewById(R.id.aboutuslogin);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentlog = new Intent(LoginPage.this, AboutUs.class);
                startActivity(intentlog);
            }
        });
        about.startAnimation(animation);
        CardView card = (CardView) findViewById(R.id.new_card);
        card.startAnimation(animation);

        Toast.makeText(this, "Please Login to the App", Toast.LENGTH_SHORT).show();
        onclicklogin();
    }

    private boolean check(String user1, String pass1)
    {
        int found = 0;
        for(int pos = 0; pos<usernames.size(); pos++)
        {
            String user12 = usernames.get(pos);
            String pass12 = passwords.get(pos);
            userid = user12;
            if(user1.equals(user12.trim()))
            {
                if(pass1.equals(pass12.trim()))
                {
                    found = 1;
                    usernames.clear();
                    passwords.clear();
                    preference = getSharedPreferences(PREFERENECE, MODE_PRIVATE);
                    SharedPreferences.Editor editorid = preference.edit();
                    editorid.putString("ID", user12.trim());
                    if(checkbox.isChecked())
                    {
                        editorid.putString("Password", pass12.trim());
                        editorid.putBoolean("Remember_Me", true);
                    }
                    else {
                        editorid.putBoolean("Remember_Me", false);
                    }
                    editorid.apply();
                    break;
                }
            }
        }
        if(found == 0)
            return false;
        else
            return  true;
    }
    public void onclicklogin()
    {

        Button signin = (Button)findViewById(R.id.signin);
        signin.startAnimation(animation);
        user1 = (EditText)findViewById(R.id.username);
        pass1 = (EditText)findViewById(R.id.password);
        user1.startAnimation(animation);
        pass1.startAnimation(animation);
        preference_prefill = getSharedPreferences(PREFERENECE, MODE_PRIVATE);
        if(preference_prefill.getBoolean("Remember_Me", false))
        {
            checkbox.setChecked(true);
            user1.setText(preference_prefill.getString("ID", ""));
            pass1.setText(preference_prefill.getString("Password", ""));
        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = user1.getText().toString();
                password = pass1.getText().toString();
                if(check( user.trim(), password.trim())) {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    intentlog = new Intent(LoginPage.this, MainPage.class);
                    startActivity(intentlog);
                    ref.removeEventListener(myevent);
                    finish();
                }
                else {
                    Snackbar.make(v,"Invalid Username / Password", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
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
