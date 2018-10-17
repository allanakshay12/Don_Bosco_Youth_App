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

public class Admin_Plan extends AppCompatActivity {

    private DatabaseReference ref;
    private FirebaseDatabase mFirebaseDatabase;
    private String key;
    private int keyval;
    private int newkeyval;
    private EditText plans;
    private String strplans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__plan);
        ref = mFirebaseDatabase.getInstance().getReference().child("Upcoming_Plans");

        Query newquery = ref.orderByKey().limitToLast(1);
        newquery.addValueEventListener(new ValueEventListener() {
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

        Button memberup = (Button)findViewById(R.id.plan_realupdate);
        plans = (EditText)findViewById(R.id.admin_plan);

        memberup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strplans = plans.getText().toString();

                if(!(strplans.isEmpty()))
                {
                    ref = mFirebaseDatabase.getInstance().getReference().child("Upcoming_Plans");

                    keyval++;
                    newkeyval++;
                    ref.child(String.valueOf(keyval)).child("Plan").setValue(strplans);
                    Toast.makeText(getApplicationContext(), "Data Successfully Added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Admin_Plan.this, Admin_Update_Plans.class);
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
        Intent intent = new Intent(this, Admin_Update_Plans.class);
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
