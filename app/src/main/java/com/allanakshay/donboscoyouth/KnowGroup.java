package com.allanakshay.donboscoyouth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.provider.ContactsContract;
import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class KnowGroup extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ListView listview;
    private ListProductAdapter adapter;
    private List<Product> mProductList;

    Intent knwgrp1;
    private DatabaseReference ref;
    private FirebaseDatabase fbref;
    private Product product;
    private List<Product> productList;
    private ArrayList<String> image = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageSet.setimage = "Image";
        setContentView(R.layout.activity_know_group);
        listview = (ListView)findViewById(R.id.namelist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        ref= fbref.getInstance().getReference().child("Youth_List");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);


        //Get product list in db when db exists
        mProductList = getListProduct();
        //Init adapter
        adapter = new ListProductAdapter(this, mProductList, image);
        //Set adapter for listview
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMessage(((TextView) view.findViewById(R.id.tv_name)).getText().toString(), ((TextView) view.findViewById(R.id.tv_ph_no)).getText().toString(), "Are you sure you want to add this contact?");
            }
        });
        registerForContextMenu(listview);

        Toast.makeText(getApplicationContext(), "Tap on any contact to add them to your Contact List", Toast.LENGTH_LONG).show();
    }

    private List<Product> getListProduct() {
        product = null;
        productList = new ArrayList<>();
        Query query = ref.orderByChild("Name");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    int i = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        product = new Product(i, snapshot.child("Name").getValue().toString(), snapshot.child("Phone Number").getValue().toString(), snapshot.child("Occupation").getValue().toString());
                        image.add(snapshot.child("Imageurl").getValue().toString());
                        productList.add(product);
                        i++;
                    }
                    adapter.notifyDataSetChanged();
                } catch(Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return productList;
    }




    public void showMessage(final String title, final String phone, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, title)
                        .putExtra(ContactsContract.Intents.Insert.PHONE, phone);

                startActivityForResult(contactIntent, 1);

        }
        }).setNegativeButton("Just Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri number = Uri.parse("tel:"+phone);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.know_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings1) {
            knwgrp1 = new Intent(KnowGroup.this, AboutUs.class);
            startActivity(knwgrp1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.upcomingevents2) {
            knwgrp1 = new Intent(KnowGroup.this, UpcomingEvents.class);
            startActivity(knwgrp1);
            finish();
        } else if (id == R.id.knowyourgroup2) {

        } else if (id == R.id.attendance2) {

            if(!LoginPage.userid.equals("Just a Guest")) {
                knwgrp1 = new Intent(KnowGroup.this, Attendance.class);
                startActivity(knwgrp1);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Please login to gain access to this content", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.photogallery2) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                knwgrp1 = new Intent(KnowGroup.this, PhotoGallery.class);
                startActivity(knwgrp1);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Please login to gain access to this content", Toast.LENGTH_SHORT).show();

        }  else if (id == R.id.join2) {
            knwgrp1 = new Intent(KnowGroup.this, JoinUs.class);
            startActivity(knwgrp1);
            finish();

        }   else if (id == R.id.feedback2) {
            knwgrp1 = new Intent(KnowGroup.this, Feedback.class);
            startActivity(knwgrp1);

        }   else if (id == R.id.finance2) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                knwgrp1 = new Intent(KnowGroup.this, Finance.class);
                startActivity(knwgrp1);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.mom2) {
            knwgrp1 = new Intent(KnowGroup.this, MOM.class);
            startActivity(knwgrp1);
            finish();
        }   else if (id == R.id.myprofile_drawer2) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                knwgrp1 = new Intent(KnowGroup.this, My_Profile.class);
                startActivity(knwgrp1);
                finish();
            } else
                Toast.makeText(KnowGroup.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
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
