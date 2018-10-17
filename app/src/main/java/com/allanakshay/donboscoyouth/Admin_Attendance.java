package com.allanakshay.donboscoyouth;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Attendance extends AppCompatActivity {
    private static DatabaseReference maintainref;
    private DatabaseReference totalref;
    private FirebaseDatabase mFireBaseDatabase;
    private List<Product> mProductList;
    private String key;
    private int keyval;
    public Product product;
    private ArrayList<String> arrayname = new ArrayList<>();
    private ArrayList<String> arraynumber = new ArrayList<>();
    private MyCustomAdapter arrayAdapter;
    private String totals;
    private TextView total;
    private static Query maintainquery;
    ValueEventListener myevent;
    ValueEventListener myeventtotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__attendance);

        ImageSet.setimage = "";
        maintainref = mFireBaseDatabase.getInstance().getReference().child("Youth_List");

        maintainquery = maintainref.orderByChild("Name");
        maintainquery.addValueEventListener(myevent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    arrayname.add(snapshot.child("Name").getValue().toString());
                    arraynumber.add(snapshot.child("Attendance").getValue().toString());
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        arrayAdapter = new MyCustomAdapter(arrayname, arraynumber, this);
        ListView listView = (ListView)findViewById(R.id.attendance_list);
        listView.setAdapter(arrayAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        total = (TextView)findViewById(R.id.attendance_total);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
        total.startAnimation(animation1);
        totalref = mFireBaseDatabase.getInstance().getReference().child("Total_Attendance");

        totalref.addValueEventListener(myeventtotal = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    totals = snapshot.getValue().toString();
                    total.setText("Total Attendance : " + totals);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button total_add = (Button)findViewById(R.id.attendance_total_add);
        total_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalref.child("1").setValue(String.valueOf(Integer.parseInt(totals) + 1));
               Toast.makeText(getApplicationContext(), "Total Attendance Successfully Updated", Toast.LENGTH_SHORT).show();
            }
        });
        total_add.startAnimation(animation1);

        Button total_sub = (Button)findViewById(R.id.attendance_total_subtract);
        total_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalref.child("1").setValue(String.valueOf(Integer.parseInt(totals) - 1));
                Toast.makeText(getApplicationContext(), "Total Attendance Successfully Updated", Toast.LENGTH_SHORT).show();
            }
        });
        total_sub.startAnimation(animation1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        maintainquery.removeEventListener(myevent);
        totalref.removeEventListener(myeventtotal);
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
