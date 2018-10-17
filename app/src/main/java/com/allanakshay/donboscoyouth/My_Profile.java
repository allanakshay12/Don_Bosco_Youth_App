package com.allanakshay.donboscoyouth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wang.avi.AVLoadingIndicatorView;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_Profile extends AppCompatActivity {

    private EditText name;
    private EditText number;
    private EditText username;
    private EditText password;
    private CircleImageView image;
    private TextView change_pic;
    private Button update;
    private Spinner monthspin;
    private Spinner datespin;
    private Spinner occuspin;
    private DatabaseReference ref;
    private FirebaseDatabase fbdb;
    private String key;
    private String newkey;
    private AVLoadingIndicatorView avi;
    private StorageReference storageReference;
    private DatabaseReference newref;
    private String oldname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        name = (EditText) findViewById(R.id.myprofile_member_name);
        number = (EditText) findViewById(R.id.myprofile_member_number);
        username = (EditText) findViewById(R.id.myprofile_member_username);
        password = (EditText) findViewById(R.id.myprofile_member_password);
        image = (CircleImageView) findViewById(R.id.profile_pic_myprofile);
        change_pic = (TextView)findViewById(R.id.change_myprofile);
        update = (Button) findViewById(R.id.myprofile_updatedetails);
        monthspin = (Spinner)findViewById(R.id.myprofile_member_month);
        datespin = (Spinner)findViewById(R.id.myprofile_member_date);
        occuspin = (Spinner)findViewById(R.id.myprofile_member_occu);
        avi = (AVLoadingIndicatorView)findViewById(R.id.avi_myprofile);
        avi.smoothToShow();
        storageReference = FirebaseStorage.getInstance().getReference();
        String[] occupationstring = new String[]{"Studying","Working"};
        String[] months =new String[]{"January","February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] days =new String[]{"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        ArrayAdapter<String> arrayAdapterdate = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, days);
        datespin.setAdapter(arrayAdapterdate);
        ArrayAdapter<String> arrayAdaptermonth = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, months);
        monthspin.setAdapter(arrayAdaptermonth);
        ArrayAdapter<String> arrayAdapteroccu = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, occupationstring);
        occuspin.setAdapter(arrayAdapteroccu);

        ref = fbdb.getInstance().getReference().child("Youth_List");
        Query query = ref.orderByChild("Username").equalTo(LoginPage.userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        key = snapshot.getKey();

                    name.setText(dataSnapshot.child(key).child("Name").getValue().toString());
                    oldname = dataSnapshot.child(key).child("Name").getValue().toString();
                    number.setText(dataSnapshot.child(key).child("Phone Number").getValue().toString());
                    password.setText(dataSnapshot.child(key).child("Password").getValue().toString());
                    username.setText(dataSnapshot.child(key).child("Username").getValue().toString());
                    if(dataSnapshot.child(key).child("Occupation").getValue().toString().equals("Studying"))
                        occuspin.setSelection(0);
                    else
                        occuspin.setSelection(1);

                    if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("January"))
                        monthspin.setSelection(0);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("February"))
                        monthspin.setSelection(1);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("March"))
                        monthspin.setSelection(2);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("April"))
                        monthspin.setSelection(3);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("May"))
                        monthspin.setSelection(4);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("June"))
                        monthspin.setSelection(5);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("July"))
                        monthspin.setSelection(6);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("August"))
                        monthspin.setSelection(7);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("September"))
                        monthspin.setSelection(8);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("October"))
                        monthspin.setSelection(9);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("November"))
                        monthspin.setSelection(10);
                    else if(dataSnapshot.child(key).child("DOB Month").getValue().toString().equals("December"))
                        monthspin.setSelection(11);

                    datespin.setSelection(Integer.parseInt(dataSnapshot.child(key).child("DOB Date").getValue().toString())-1);

                datespin.setEnabled(false);
                datespin.setClickable(false);
                monthspin.setEnabled(false);
                monthspin.setClickable(false);
                occuspin.setEnabled(false);
                occuspin.setClickable(false);

                    if(!dataSnapshot.child(key).child("Imageurl").getValue().toString().equals("")) {
                        StorageReference display = storageReference.child(dataSnapshot.child(key).child("Imageurl").getValue().toString());
                        Glide.with(My_Profile.this)
                                .using(new FirebaseImageLoader())
                                .load(display)
                                .into(image);
                    }

                    avi.smoothToHide();

                } catch (Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Profile.this, Profile_Pic.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(update.getText().toString().equals("Edit Profile"))
                {
                    name.setEnabled(true);
                    number.setEnabled(true);
                    username.setEnabled(true);
                    password.setEnabled(true);
                    occuspin.setEnabled(true);
                    monthspin.setEnabled(true);
                    datespin.setEnabled(true);
                    occuspin.setClickable(true);
                    monthspin.setClickable(true);
                    datespin.setClickable(true);
                    update.setText("Apply Changes");
                }
                else if(update.getText().toString().equals("Apply Changes"))
                {
                    if(!(name.getText().toString().isEmpty() || number.getText().toString().isEmpty() || password.getText().toString().isEmpty() || username.getText().toString().isEmpty()))
                    {
                        avi.smoothToShow();
                        newref = fbdb.getInstance().getReference().child("Upcoming_Events");

                        Query newquery = newref.orderByChild("Description").equalTo(oldname + "'s Birthday" );
                        newquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                        newkey = snapshot.getKey();
                                    newref.child(newkey).child("Description").setValue(name.getText().toString() + "'s Birthday");
                                    newref.child(newkey).child("Date").setValue(datespin.getItemAtPosition(datespin.getSelectedItemPosition()).toString());
                                    newref.child(newkey).child("Month").setValue(monthspin.getItemAtPosition(monthspin.getSelectedItemPosition()).toString());
                                } catch(Exception e) {}

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        ref.child(key).child("Name").setValue(name.getText().toString());
                        ref.child(key).child("Username").setValue(username.getText().toString());
                        ref.child(key).child("Password").setValue(password.getText().toString());
                        ref.child(key).child("Phone Number").setValue(number.getText().toString());
                        ref.child(key).child("Occupation").setValue(occuspin.getItemAtPosition(occuspin.getSelectedItemPosition()).toString());
                        ref.child(key).child("DOB Date").setValue(datespin.getItemAtPosition(datespin.getSelectedItemPosition()).toString());
                        ref.child(key).child("DOB Month").setValue(monthspin.getItemAtPosition(monthspin.getSelectedItemPosition()).toString());



                        Toast.makeText(getApplicationContext(), "Profile Successfully Updated", Toast.LENGTH_SHORT).show();
                        name.setEnabled(false);
                        number.setEnabled(false);
                        username.setEnabled(false);
                        password.setEnabled(false);
                        occuspin.setEnabled(false);
                        monthspin.setEnabled(false);
                        datespin.setEnabled(false);
                        occuspin.setClickable(false);
                        monthspin.setClickable(false);
                        datespin.setClickable(false);
                        LoginPage.userid = username.getText().toString();
                        avi.smoothToHide();
                        update.setText("Edit Profile");
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter all the Fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
