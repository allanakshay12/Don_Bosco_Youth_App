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

public class Admin_member extends AppCompatActivity {
    private DatabaseReference ref;
    private FirebaseDatabase mFirebaseDatabase;
    private String key;
    private int keyval;
    private int newkeyval;
    private String nameedits;
    private String phoneedits;
    private String appuseredits;
    private String passwordedits;
    private DatabaseReference newref;
    private String newnewpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageSet.setimage = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_member);
        ref = mFirebaseDatabase.getInstance().getReference().child("Upcoming_Events");

        Query query = ref.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    key=child.getKey();
                    newkeyval = Integer.parseInt(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        newref = mFirebaseDatabase.getInstance().getReference().child("Youth_List");
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

        ref = mFirebaseDatabase.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

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
        String[] occupation = new String[]{"Studying","Working"};
        String[] months =new String[]{"January","February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] days =new String[]{"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        ArrayAdapter<String> arrayAdapteroccu = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, occupation);
        final Spinner occu = (Spinner)findViewById(R.id.admin_member_occu);
        occu.setAdapter(arrayAdapteroccu);
        ArrayAdapter<String> arrayAdaptermonth = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, months);
        final Spinner month = (Spinner)findViewById(R.id.admin_member_month);
        month.setAdapter(arrayAdaptermonth);
        ArrayAdapter<String> arrayAdapterdate = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, days);
        final Spinner date= (Spinner)findViewById(R.id.admin_member_date);
        date.setAdapter(arrayAdapterdate);
        Button memberup = (Button)findViewById(R.id.member_realupdate);
        final EditText nameedit = (EditText)findViewById(R.id.admin_member_name);
        final EditText phoneedit = (EditText)findViewById(R.id.admin_member_number);
        final EditText appuseredit = (EditText)findViewById(R.id.admin_member_username);
        final EditText passwordedit = (EditText)findViewById(R.id.admin_member_password);

        memberup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameedits = nameedit.getText().toString();
                phoneedits = phoneedit.getText().toString();
                appuseredits = appuseredit.getText().toString();
                passwordedits = passwordedit.getText().toString();

                if(!(nameedits.isEmpty() || phoneedits.isEmpty() || appuseredits.isEmpty() || passwordedits.isEmpty()))
                {
                    ref = mFirebaseDatabase.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

                    keyval++;
                    newkeyval++;
                    ref.child(String.valueOf(keyval)).child("Name").setValue(nameedits);
                    ref.child(String.valueOf(keyval)).child("Phone Number").setValue(phoneedits);
                    ref.child(String.valueOf(keyval)).child("Occupation").setValue(occu.getItemAtPosition(occu.getSelectedItemPosition()).toString());
                    ref.child(String.valueOf(keyval)).child("Username").setValue(appuseredits);
                    ref.child(String.valueOf(keyval)).child("Password").setValue(passwordedits);
                    ref.child(String.valueOf(keyval)).child("Attendance").setValue("0");
                    ref.child(String.valueOf(keyval)).child("Notification_Viewed_Group").setValue("No");
                    ref.child(String.valueOf(keyval)).child("Notification_Viewed_Event").setValue("No");
                    ref.child(String.valueOf(keyval)).child("Notification_Viewed_Finance").setValue("No");
                    ref.child(String.valueOf(keyval)).child("Notification_Viewed_MOM").setValue("No");
                    ref.child(String.valueOf(keyval)).child("Facebook_UserID").setValue(" ");
                    ref.child(String.valueOf(keyval)).child("Imageurl").setValue("");
                    ref.child(String.valueOf(keyval)).child("Designation").setValue("Normal");
                    ref.child(String.valueOf(keyval)).child("DOB Month").setValue(month.getItemAtPosition(month.getSelectedItemPosition()));
                    ref.child(String.valueOf(keyval)).child("DOB Date").setValue(date.getItemAtPosition(date.getSelectedItemPosition()));
                    ref = mFirebaseDatabase.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Upcoming_Events");
                    ref.child(String.valueOf(newkeyval)).child("Month").setValue(month.getItemAtPosition(month.getSelectedItemPosition()));
                    ref.child(String.valueOf(newkeyval)).child("Date").setValue(date.getItemAtPosition(date.getSelectedItemPosition()));
                    ref.child(String.valueOf(newkeyval)).child("Description").setValue(nameedits + "'s Birthday");
                    Toast.makeText(getApplicationContext(), "Data Successfully Added", Toast.LENGTH_LONG).show();
                    for (int i = 1; i <= Integer.parseInt(newnewpos); i++) {
                        newref.child(String.valueOf(i)).child("Notification_Viewed_Group").setValue("No");
                    }
                    Intent intent = new Intent(Admin_member.this, Admin_Update_Members.class);
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
        Intent intent = new Intent(this, Admin_Update_Members.class);
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
