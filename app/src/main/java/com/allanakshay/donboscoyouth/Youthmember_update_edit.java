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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Youthmember_update_edit extends AppCompatActivity {

    private ArrayList<String> list = new ArrayList<>();
    private DatabaseReference ref;
    private FirebaseDatabase fbref;
    String key;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        list = YouthMemberAdapter.editstuff;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youthmember_update_edit);
        String[] occupationstring = new String[]{"Studying","Working"};
        String[] months =new String[]{"January","February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] days =new String[]{"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        String[] designationstring = new String[]{"Normal", "Leader"};

        final EditText name = (EditText)findViewById(R.id.admin_member_name_update);
        final EditText phone_no = (EditText)findViewById(R.id.admin_member_number_update);
        final EditText user_id = (EditText)findViewById(R.id.admin_member_username_update);
        final EditText password = (EditText)findViewById(R.id.admin_member_password_update);

        final Spinner dobmonth = (Spinner)findViewById(R.id.admin_member_month_update);
        final Spinner dobdate = (Spinner)findViewById(R.id.admin_member_date_update);
        final Spinner occupation = (Spinner)findViewById(R.id.admin_member_occu_update);
        final Spinner designation = (Spinner)findViewById(R.id.admin_designation_update);


        name.setText(list.get(1).toString());
        phone_no.setText(list.get(2).toString());
        user_id.setText(list.get(3).toString());
        password.setText(list.get(4).toString());
        ArrayAdapter<String> arrayAdapterdate = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, days);
        dobdate.setAdapter(arrayAdapterdate);
        ArrayAdapter<String> arrayAdaptermonth = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, months);
        dobmonth.setAdapter(arrayAdaptermonth);
        ArrayAdapter<String> arrayAdapteroccu = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, occupationstring);
        occupation.setAdapter(arrayAdapteroccu);
        ArrayAdapter<String> arrayAdapterdesignation = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, designationstring);
        designation.setAdapter(arrayAdapterdesignation);

        dobdate.setSelection(Integer.parseInt(list.get(6).toString())-1);

        if(list.get(5).toString().equals("Studying"))
            occupation.setSelection(0);
        else
            occupation.setSelection(1);

        if(list.get(8).toString().equals("Normal"))
            designation.setSelection(0);
        else
            designation.setSelection(1);

        if(list.get(7).toString().equals("January"))
            dobmonth.setSelection(0);
        else if(list.get(7).toString().equals("February"))
            dobmonth.setSelection(1);
        else if(list.get(7).toString().equals("March"))
            dobmonth.setSelection(2);
        else if(list.get(7).toString().equals("April"))
            dobmonth.setSelection(3);
        else if(list.get(7).toString().equals("May"))
            dobmonth.setSelection(4);
        else if(list.get(7).toString().equals("June"))
            dobmonth.setSelection(5);
        else if(list.get(7).toString().equals("July"))
            dobmonth.setSelection(6);
        else if(list.get(7).toString().equals("August"))
            dobmonth.setSelection(7);
        else if(list.get(7).toString().equals("September"))
            dobmonth.setSelection(8);
        else if(list.get(7).toString().equals("October"))
            dobmonth.setSelection(9);
        else if(list.get(7).toString().equals("November"))
            dobmonth.setSelection(10);
        else if(list.get(7).toString().equals("December"))
            dobmonth.setSelection(11);



        Button update = (Button)findViewById(R.id.member_realupdate_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(name.getText().toString().isEmpty() || user_id.getText().toString().isEmpty() || password.getText().toString().isEmpty() || phone_no.getText().toString().isEmpty())) {
                    ref = fbref.getInstance().getReference().child("Youth_List");

                    ref.child(list.get(0).toString()).child("Name").setValue(name.getText().toString());
                    ref.child(list.get(0).toString()).child("Username").setValue(user_id.getText().toString());
                    ref.child(list.get(0).toString()).child("Password").setValue(password.getText().toString());
                    ref.child(list.get(0).toString()).child("Designation").setValue(designation.getItemAtPosition(designation.getSelectedItemPosition()).toString());
                    ref.child(list.get(0).toString()).child("Phone Number").setValue(phone_no.getText().toString());
                    ref.child(list.get(0).toString()).child("Occupation").setValue(occupation.getItemAtPosition(occupation.getSelectedItemPosition()).toString());
                    ref.child(list.get(0).toString()).child("DOB Date").setValue(dobdate.getItemAtPosition(dobdate.getSelectedItemPosition()).toString());
                    ref.child(list.get(0).toString()).child("DOB Month").setValue(dobmonth.getItemAtPosition(dobmonth.getSelectedItemPosition()).toString());

                    ref = fbref.getInstance().getReference().child("Upcoming_Events");

                    Query query = ref.orderByChild("Description").equalTo(list.get(1).toString() + "'s Birthday" );
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                    key = snapshot.getKey();
                                ref.child(key).child("Description").setValue(name.getText().toString() + "'s Birthday");
                                ref.child(key).child("Date").setValue(dobdate.getItemAtPosition(dobdate.getSelectedItemPosition()).toString());
                                ref.child(key).child("Month").setValue(dobmonth.getItemAtPosition(dobmonth.getSelectedItemPosition()).toString());
                            } catch(Exception e) {}

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(getApplicationContext(), "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Youthmember_update_edit.this, Admin_Update_Members.class);
                    startActivity(intent);
                    YouthMemberAdapter.editstuff.clear();
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
        intent = new Intent(Youthmember_update_edit.this, Admin_Update_Members.class);
        startActivity(intent);
        YouthMemberAdapter.editstuff.clear();
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
