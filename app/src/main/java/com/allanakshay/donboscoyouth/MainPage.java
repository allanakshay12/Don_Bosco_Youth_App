package com.allanakshay.donboscoyouth;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Intent intentknwgrp;

        private DatabaseReference ref;
    private FirebaseDatabase fbref;
    private String designation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //Start service on app created
        Intent intent1 = new Intent(this, NotificationServiceDBY.class);
        startService(intent1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button admin = (Button)findViewById(R.id.adminpage);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        onButtonClick();
        Button update = (Button)findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentknwgrp = new Intent(MainPage.this, Update.class);
                startActivity(intentknwgrp);
            }
        });
        Button important = (Button)findViewById(R.id.importantnotice);
        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!LoginPage.userid.equals("Just a Guest")) {
                    intentknwgrp = new Intent(MainPage.this, ImportantNotice.class);
                    startActivity(intentknwgrp);
                } else
                    Toast.makeText(MainPage.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
            }
        });
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_from_bottom);
        update.startAnimation(animation);
        important.startAnimation(animation);
        Button finance = (Button)findViewById(R.id.finance);
        finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!LoginPage.userid.equals("Just a Guest")) {
                    intentknwgrp = new Intent(MainPage.this, Finance.class);
                    startActivity(intentknwgrp);
                } else
                    Toast.makeText(MainPage.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
            }
        });
        finance.startAnimation(animation);
        Button mom = (Button)findViewById(R.id.mom);
        mom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    intentknwgrp = new Intent(MainPage.this, MOM.class);
                    startActivity(intentknwgrp);

            }
        });
        mom.startAnimation(animation);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginPage.userid.equals("Just a Guest")) {
                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    if (CoverPage.connectedstate.equals("Connected") && (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                            || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
                        ref = fbref.getInstance().getReference().child("Youth_List");
                        Query query = ref.orderByChild("Username").equalTo(LoginPage.userid);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        designation = snapshot.child("Designation").getValue().toString();
                                        if (designation.equals("Leader")) {
                                            intentknwgrp = new Intent(MainPage.this, Admin_Page.class);
                                            startActivity(intentknwgrp);
                                        } else
                                            Toast.makeText(getApplicationContext(), "I'm Sorry. You don't have the permissions to access this page.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else if (CoverPage.connectedstate.equals("Disconnected")) {

                        Toast.makeText(getApplicationContext(), "Admin Page can't be accessed when user opened the app while offline.", Toast.LENGTH_SHORT).show();
                    } else {
                        CoverPage.connectedstate.equals("Disconnected");
                        Toast.makeText(getApplicationContext(), "Admin Page can't be accessed when user is offline.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(MainPage.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
            }
        });
        admin.startAnimation(animation);
    }

    public void onButtonClick()
    {
        Button atten = (Button)findViewById(R.id.attendance);
        Button upev = (Button)findViewById(R.id.upcomingevents);
        Button phga = (Button)findViewById(R.id.photogallery);
        Button knwgrp = (Button)findViewById(R.id.knowyourgroup);
        atten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!LoginPage.userid.equals("Just a Guest")) {
                    intentknwgrp = new Intent(MainPage.this, Attendance.class);
                    startActivity(intentknwgrp);
                } else
                    Toast.makeText(MainPage.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
            }
        });
        knwgrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentknwgrp = new Intent(MainPage.this, KnowGroup.class);
                startActivity(intentknwgrp);
            }
        });
        upev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentknwgrp = new Intent(MainPage.this, UpcomingEvents.class);
                startActivity(intentknwgrp);
            }
        });
        phga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!LoginPage.userid.equals("Just a Guest")) {
                    intentknwgrp = new Intent(MainPage.this, PhotoGallery.class);
                    startActivity(intentknwgrp);
                } else
                    Toast.makeText(MainPage.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
            }
        });

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_from_bottom);
        atten.startAnimation(animation);

        knwgrp.startAnimation(animation);

        upev.startAnimation(animation);

        phga.startAnimation(animation);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            intentknwgrp = new Intent(MainPage.this, LoginPage.class);
            startActivity(intentknwgrp);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            intentknwgrp = new Intent(MainPage.this, AboutUs.class);
            startActivity(intentknwgrp);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.upcomingevents1) {
            intentknwgrp = new Intent(MainPage.this, UpcomingEvents.class);
            startActivity(intentknwgrp);
        } else if (id == R.id.knowyourgroup1) {
            intentknwgrp = new Intent(MainPage.this, KnowGroup.class);
            startActivity(intentknwgrp);

        } else if (id == R.id.attendance1) {

            if(!LoginPage.userid.equals("Just a Guest")) {
                intentknwgrp = new Intent(MainPage.this, Attendance.class);
                startActivity(intentknwgrp);
            } else
                Toast.makeText(MainPage.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.photogallery1) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentknwgrp = new Intent(MainPage.this, PhotoGallery.class);
                startActivity(intentknwgrp);
            }
                else
                Toast.makeText(MainPage.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();

        }  else if (id == R.id.join1) {
            intentknwgrp = new Intent(MainPage.this, JoinUs.class);
            startActivity(intentknwgrp);

        }  else if (id == R.id.feedback1) {
            intentknwgrp = new Intent(MainPage.this, Feedback.class);
            startActivity(intentknwgrp);

        }   else if (id == R.id.finance1) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentknwgrp = new Intent(MainPage.this, Finance.class);
                startActivity(intentknwgrp);
            } else
                Toast.makeText(MainPage.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }   else if (id == R.id.mom1) {
            intentknwgrp = new Intent(MainPage.this, MOM.class);
            startActivity(intentknwgrp);
        }   else if (id == R.id.myprofile_drawer1) {
            if(!LoginPage.userid.equals("Just a Guest")) {
            intentknwgrp = new Intent(MainPage.this, My_Profile.class);
            startActivity(intentknwgrp);
            } else
                Toast.makeText(MainPage.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
