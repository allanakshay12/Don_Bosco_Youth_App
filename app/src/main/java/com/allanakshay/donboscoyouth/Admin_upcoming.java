package com.allanakshay.donboscoyouth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Admin_upcoming extends AppCompatActivity {

    private FirebaseDatabase mFireBaseDatabase;
    private Button update;
    private Spinner month;
    private Spinner day;
    private EditText MOM_edit;
    private DatabaseReference ref;
    private String date;
    private String monthup;
    private  String MOM;
    public int keyval;
    public String key;
    private DatabaseReference newref;
    private String newnewpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upcoming);


        update = (Button)findViewById(R.id.admin_upcoming_realupdate);
        month = (Spinner)findViewById(R.id.admin_upcoming_month);
        day = (Spinner)findViewById(R.id.admin_upcoming_day);
        MOM_edit = (EditText)findViewById(R.id.admin_upcoming_mom);
        String[] months =new String[]{"January","February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] days =new String[]{"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        ArrayAdapter<String> arrayAdaptermonth = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, months);
        ArrayAdapter<String> arrayAdapterday = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, days);
        month.setAdapter(arrayAdaptermonth);
        day.setAdapter(arrayAdapterday);
        ref = mFireBaseDatabase.getInstance().getReference().child("Upcoming_Events");

        Query query = ref.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    key=child.getKey();
                    keyval = Integer.parseInt(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        newref = mFireBaseDatabase.getInstance().getReference().child("Youth_List");
        Query newnewquery = newref.orderByKey().limitToLast(1);
        newnewquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        newnewpos = snapshot.getKey();
                } catch (Exception e) {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = day.getItemAtPosition(day.getSelectedItemPosition()).toString();
                monthup = month.getItemAtPosition(month.getSelectedItemPosition()).toString();
                MOM = MOM_edit.getText().toString();
                if(!MOM.isEmpty())
                {
                    keyval++;
                    ref.child(String.valueOf(keyval)).child("Month").setValue(monthup);
                    ref.child(String.valueOf(keyval)).child("Date").setValue(date);
                    ref.child(String.valueOf(keyval)).child("Description").setValue(MOM);
                    Toast.makeText(getApplicationContext(), "Data Successfully Added", Toast.LENGTH_SHORT).show();
                    for (int i = 1; i <= Integer.parseInt(newnewpos); i++) {
                        newref.child(String.valueOf(i)).child("Notification_Viewed_Event").setValue("No");
                    }
                    Intent intent = new Intent(Admin_upcoming.this, Admin_Update_Events.class);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please Enter all the Fields", Toast.LENGTH_LONG).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Admin_Update_Events.class);
        startActivity(intent);
        finish();
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
