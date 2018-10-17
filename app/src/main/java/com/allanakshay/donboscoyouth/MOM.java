package com.allanakshay.donboscoyouth;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MOM extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listview;
    private ListProductAdapter adapter;
    private List<Product> mProductList;
    private DatabaseReference ref;
    private FirebaseDatabase mFire;
    Intent intentmom;
    private Product product;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageSet.setimage = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mom);
        listview = (ListView)findViewById(R.id.momlist);

        ref = mFire.getInstance().getReference().child("MOM");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmom);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutmom);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewmom);
        navigationView.setNavigationItemSelectedListener(this);
        //Get product list in db when db exists
        mProductList = getListProduct();
        //Init adapter
        adapter = new ListProductAdapter(this, mProductList, null);
        //Set adapter for listview
        listview.setAdapter(adapter);
    }

    private List<Product> getListProduct() {
        product = null;
        productList = new ArrayList<>();
        Query query = ref.orderByChild("Key Value");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                try {
                    int i = 1;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        product = new Product(i, snapshot.child("Month").getValue().toString(), snapshot.child("Date").getValue().toString(), snapshot.child("Description").getValue().toString());
                        productList.add(product);
                        i++;
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return productList;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutmom);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settingsmom) {
            intentmom = new Intent(MOM.this, AboutUs.class);
            startActivity(intentmom);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.upcomingeventsmom) {
            intentmom = new Intent(MOM.this, UpcomingEvents.class);
            startActivity(intentmom);
            finish();
        } else if (id == R.id.knowyourgroupmom) {
            intentmom = new Intent(MOM.this, KnowGroup.class);
            startActivity(intentmom);
            finish();

        } else if (id == R.id.attendancemom) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentmom = new Intent(MOM.this, Attendance.class);
                startActivity(intentmom);
                finish();
            }else
                Toast.makeText(getApplicationContext(), "Please login to gain access to this content", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.photogallerymom) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentmom = new Intent(MOM.this, PhotoGallery.class);
                startActivity(intentmom);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Please login to gain access to this content", Toast.LENGTH_SHORT).show();

        }  else if (id == R.id.joinmom) {
            intentmom = new Intent(MOM.this, JoinUs.class);
            startActivity(intentmom);

        }  else if (id == R.id.feedbackmom) {
            intentmom = new Intent(MOM.this, Feedback.class);
            startActivity(intentmom);

        }   else if (id == R.id.financemom) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentmom = new Intent(MOM.this, Finance.class);
                startActivity(intentmom);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.mommom) {

        } else if (id == R.id.myprofile_drawermom) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentmom = new Intent(MOM.this, My_Profile.class);
                startActivity(intentmom);
                finish();
            } else
                Toast.makeText(MOM.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutmom);
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
