package com.allanakshay.donboscoyouth;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Finance extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Intent fin;
    private ListView listview;
    private ListFinanceAdapter adapter;
    private List<FinanceProduct> mProductList;
    private DatabaseReference ref;
    private FirebaseDatabase mFirebaseDatabase;
    public int finance_balance;
    TextView balance;
    private FinanceProduct product = null;
    private List<FinanceProduct> productList = new ArrayList<>();
    private ArrayList<String> arrayamt = new ArrayList<>();
    private ArrayList<String> arraytype = new ArrayList<>();
    public static AlertDialog.Builder financebuilder;
    private int income_builder = 0;
    private int expense_builder = 0;
    private TextView income;
    private TextView expense;
    private TextView balance_builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        financebuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.finance_builder, null);
        income = (TextView)dialog.findViewById(R.id.builder_income);
        expense = (TextView)dialog.findViewById(R.id.builder_expense);
        balance_builder = (TextView)dialog.findViewById(R.id.builder_balance);
        financebuilder.setView(dialog);
        financebuilder.setCancelable(true);




        ref = mFirebaseDatabase.getInstance().getReference().child("Finance_List");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        arraytype.add(snapshot.child("Type").getValue().toString());
                        arrayamt.add(snapshot.child("Amount").getValue().toString());
                    }
                    finance_balance = 0;


                    for (int i = 0; i < arraytype.size(); i++) {
                        if (arraytype.get(i).equals("Income")) {
                            finance_balance = finance_balance + Integer.parseInt(arrayamt.get(i).trim());
                            income_builder = income_builder + Integer.parseInt(arrayamt.get(i).trim());
                        }
                        else {
                            finance_balance = finance_balance - Integer.parseInt(arrayamt.get(i).trim());
                            expense_builder = expense_builder + Integer.parseInt(arrayamt.get(i).trim());
                        }
                    }

                    balance = (TextView) findViewById(R.id.finance_balance);
                    income.setText("Income : " + Integer.toString(income_builder));
                    expense.setText("Expenditure : " + Integer.toString(expense_builder));
                    balance_builder.setText("Balance : " + Integer.toString(finance_balance));
                    balance.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(dialog.getParent()!=null)
                            {
                                ((ViewGroup)dialog.getParent()).removeView(dialog);
                            }
                            financebuilder.show();
                        }
                    });
                    if (finance_balance >= 0)
                        balance.setTextColor(Color.parseColor("#179100"));
                    else
                        balance.setTextColor(Color.parseColor("#e00000"));
                    balance.setText(balance.getText().toString() + Integer.toString(finance_balance));
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
                    balance.startAnimation(animation);
                }catch (Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarfinance);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutfinance);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewfinance);
        navigationView.setNavigationItemSelectedListener(this);


        listview = (ListView)findViewById(R.id.finance_listview);
        //Get product list in db when db exists
        mProductList = getListProduct();
        //Init adapter
        adapter = new ListFinanceAdapter(this, mProductList);
        //Set adapter for listview
        listview.setAdapter(adapter);

        registerForContextMenu(listview);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutfinance);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private List<FinanceProduct> getListProduct() {
        product = null;
        productList = new ArrayList<>();
        Query query = ref.orderByChild("Key Value");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    int i = 1;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        product = new FinanceProduct(i, snapshot.child("Month").getValue().toString(), snapshot.child("Date").getValue().toString(), snapshot.child("Amount").getValue().toString(), snapshot.child("Type").getValue().toString(), snapshot.child("Description").getValue().toString());
                        productList.add(product);
                        i++;
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return productList;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settingsfinance) {
            fin = new Intent(Finance.this, AboutUs.class);
            startActivity(fin);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.upcomingeventsfinance) {
            fin = new Intent(Finance.this, UpcomingEvents.class);
            startActivity(fin);
            finish();
        } else if (id == R.id.knowyourgroupfinance) {

            fin = new Intent(Finance.this, KnowGroup.class);
            startActivity(fin);
            finish();

        } else if (id == R.id.attendancefinance) {
            fin = new Intent(Finance.this, Attendance.class);
            startActivity(fin);
            finish();


        } else if (id == R.id.photogalleryfinance) {
            fin = new Intent(Finance.this, PhotoGallery.class);
            startActivity(fin);
            finish();

        }  else if (id == R.id.joinfinance) {
            fin = new Intent(Finance.this, JoinUs.class);
            startActivity(fin);
            finish();

        }   else if (id == R.id.feedbackfinance) {
            fin = new Intent(Finance.this, Feedback.class);
            startActivity(fin);

        }   else if (id == R.id.financefinance) {

        }
        else if (id == R.id.momfinance) {
            fin = new Intent(Finance.this, MOM.class);
            startActivity(fin);
            finish();
        } else if (id == R.id.myprofile_drawerfinance) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                fin = new Intent(Finance.this, My_Profile.class);
                startActivity(fin);
                finish();
            } else
                Toast.makeText(Finance.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutfinance);
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



