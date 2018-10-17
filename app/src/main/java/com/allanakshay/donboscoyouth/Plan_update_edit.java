package com.allanakshay.donboscoyouth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Plan_update_edit extends AppCompatActivity {

    private ArrayList<String> list = new ArrayList<>();
    private DatabaseReference ref;
    private FirebaseDatabase fbref;
    String key;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_update_edit);
        list = PlanAdapter.editstuff;
        final EditText planname = (EditText)findViewById(R.id.admin_plan_edit);
        planname.setText(list.get(1).toString());

        Button update = (Button)findViewById(R.id.plan_realupdate_edit);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(planname.getText().toString().isEmpty())) {
                    ref = fbref.getInstance().getReference().child("Upcoming_Plans");


                    ref.child(list.get(0).toString()).child("Plan").setValue(planname.getText().toString());
                    Toast.makeText(getApplicationContext(), "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Plan_update_edit.this, Admin_Update_Plans.class);
                    startActivity(intent);
                    PlanAdapter.editstuff.clear();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please Enter all the Fields", Toast.LENGTH_SHORT).show();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(Plan_update_edit.this, Admin_Update_Plans.class);
        startActivity(intent);
        PlanAdapter.editstuff.clear();
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
