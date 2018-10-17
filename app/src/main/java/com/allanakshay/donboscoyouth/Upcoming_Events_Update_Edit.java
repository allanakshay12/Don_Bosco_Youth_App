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

import java.util.ArrayList;

public class Upcoming_Events_Update_Edit extends AppCompatActivity {

    private ArrayList<String> list = new ArrayList<>();
    private DatabaseReference ref;
    private FirebaseDatabase fbref;
    String key;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        list = UpcomingEventsUpdateAdapter.editstuff;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming__events__update__edit);
        String[] months =new String[]{"January","February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] days =new String[]{"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

        final EditText event = (EditText)findViewById(R.id.admin_upcoming_mom_edit);

        final Spinner month = (Spinner)findViewById(R.id.admin_upcoming_month_edit);
        final Spinner date = (Spinner)findViewById(R.id.admin_upcoming_day_edit);


        event.setText(list.get(3).toString());
        ArrayAdapter<String> arrayAdapterdate = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, days);
        date.setAdapter(arrayAdapterdate);
        ArrayAdapter<String> arrayAdaptermonth = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, months);
        month.setAdapter(arrayAdaptermonth);

        date.setSelection(Integer.parseInt(list.get(2).toString())-1);


        if(list.get(1).toString().equals("January"))
            month.setSelection(0);
        else if(list.get(1).toString().equals("February"))
            month.setSelection(1);
        else if(list.get(1).toString().equals("March"))
            month.setSelection(2);
        else if(list.get(1).toString().equals("April"))
            month.setSelection(3);
        else if(list.get(1).toString().equals("May"))
            month.setSelection(4);
        else if(list.get(1).toString().equals("June"))
            month.setSelection(5);
        else if(list.get(1).toString().equals("July"))
            month.setSelection(6);
        else if(list.get(1).toString().equals("August"))
            month.setSelection(7);
        else if(list.get(1).toString().equals("September"))
            month.setSelection(8);
        else if(list.get(1).toString().equals("October"))
            month.setSelection(9);
        else if(list.get(1).toString().equals("November"))
            month.setSelection(10);
        else if(list.get(1).toString().equals("December"))
            month.setSelection(11);



        Button update = (Button)findViewById(R.id.admin_upcoming_realupdate_edit);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(event.getText().toString().isEmpty())) {
                    ref = fbref.getInstance().getReference().child("Upcoming_Events");

                    ref.child(list.get(0).toString()).child("Description").setValue(event.getText().toString());
                    ref.child(list.get(0).toString()).child("Month").setValue(month.getItemAtPosition(month.getSelectedItemPosition()).toString());
                    ref.child(list.get(0).toString()).child("Date").setValue(date.getItemAtPosition(date.getSelectedItemPosition()).toString());
                    Toast.makeText(getApplicationContext(), "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Upcoming_Events_Update_Edit.this, Admin_Update_Events.class);
                    startActivity(intent);
                    UpcomingEventsUpdateAdapter.editstuff.clear();
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
        intent = new Intent(Upcoming_Events_Update_Edit.this, Admin_Update_Events.class);
        startActivity(intent);
        UpcomingEventsUpdateAdapter.editstuff.clear();
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
