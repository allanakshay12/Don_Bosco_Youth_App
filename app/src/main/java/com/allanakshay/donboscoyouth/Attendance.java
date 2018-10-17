package com.allanakshay.donboscoyouth;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class Attendance extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Intent intentatten;
    private DatabaseReference ref;
    private DatabaseReference totref;
    private FirebaseDatabase mFirebaseDatabase;
    private String total_days;
    private String dayss;
    private String names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        final TextView name = (TextView)findViewById(R.id.attendancename);
        final TextView days = (TextView)findViewById(R.id.attendancedays);
        CardView card = (CardView) findViewById(R.id.attendance_card);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
        card.startAnimation(animation);

        ref = mFirebaseDatabase.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/Youth_List");
        Query query = ref.orderByChild("Username").equalTo(LoginPage.userid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    names = snapshot.child("Name").getValue().toString();
                    dayss = snapshot.child("Attendance").getValue().toString();
                }
                name.setText(names);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        totref = mFirebaseDatabase.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Total_Attendance");

        totref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                total_days = dataSnapshot1.getValue().toString();
                days.setText(dayss + " out of " + total_days);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        name.setText(names);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings2) {
            intentatten = new Intent(Attendance.this, AboutUs.class);
            startActivity(intentatten);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.upcomingevents3) {
            intentatten = new Intent(Attendance.this, UpcomingEvents.class);
            startActivity(intentatten);
            finish();
        } else if (id == R.id.knowyourgroup3) {

            intentatten = new Intent(Attendance.this, KnowGroup.class);
            startActivity(intentatten);
            finish();

        } else if (id == R.id.attendance3) {


        } else if (id == R.id.photogallery3) {
            intentatten = new Intent(Attendance.this, PhotoGallery.class);
            startActivity(intentatten);
            finish();

        }  else if (id == R.id.join3) {
            intentatten = new Intent(Attendance.this, JoinUs.class);
            startActivity(intentatten);
            finish();

        }   else if (id == R.id.feedback3) {
            intentatten = new Intent(Attendance.this, Feedback.class);
            startActivity(intentatten);

        }   else if (id == R.id.finance3) {
            intentatten = new Intent(Attendance.this, Finance.class);
            startActivity(intentatten);
            finish();
        }
        else if (id == R.id.mom3) {
            intentatten = new Intent(Attendance.this, MOM.class);
            startActivity(intentatten);
            finish();
        } else if (id == R.id.myprofile_drawer3) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentatten = new Intent(Attendance.this, My_Profile.class);
                startActivity(intentatten);
                finish();
            } else
                Toast.makeText(Attendance.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
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
