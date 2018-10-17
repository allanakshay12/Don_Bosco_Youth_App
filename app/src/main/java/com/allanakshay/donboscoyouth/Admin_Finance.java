package com.allanakshay.donboscoyouth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Finance extends AppCompatActivity {
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference ref;
    private Spinner editmonth;
    private Spinner editday;
    private Spinner spinner;
    private EditText editdesc;
    private EditText editamt;
    private String key;
    private DatabaseReference newref;
    private String newnewpos;
    int j;
    Query sortquery[] = new Query[12];
   public int keyval;
    int keydata = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageSet.setimage = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__finance);
        context = this;
        editday = (Spinner)findViewById(R.id.admin_finance_day);
        editdesc = (EditText)findViewById(R.id.admin_finance_desc);
        editamt = (EditText)findViewById(R.id.admin_finance_amt);
        editmonth = (Spinner)findViewById(R.id.admin_finance_month);
        spinner = (Spinner)findViewById(R.id.admin_finance_type);
        String[] types =new String[]{"Income","Expense"};
        String[] months =new String[]{"January","February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] days =new String[]{"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        ArrayAdapter<String> arrayAdaptertype = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, types);
        ArrayAdapter<String> arrayAdaptermonth = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, months);
        ArrayAdapter<String> arrayAdapterdays = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, days);
        spinner.setAdapter(arrayAdaptertype);
        editday.setAdapter(arrayAdapterdays);
        editmonth.setAdapter(arrayAdaptermonth);
        ref = mFireBaseDatabase.getInstance().getReference().child("Finance_List");



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
        Query newquery = newref.orderByKey().limitToLast(1);
        newquery.addValueEventListener(new ValueEventListener() {
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


        Button update = (Button)findViewById(R.id.finance_realupdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = editday.getItemAtPosition(editday.getSelectedItemPosition()).toString();
                String month = editmonth.getItemAtPosition(editmonth.getSelectedItemPosition()).toString();
                String type = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                String desc = editdesc.getText().toString();
                String amt = editamt.getText().toString();
                if(!(desc.isEmpty() || amt.isEmpty()))
                {
                    keyval++;
                    ref.child(String.valueOf(keyval)).child("Amount").setValue(amt);
                    ref.child(String.valueOf(keyval)).child("Month").setValue(month);
                    ref.child(String.valueOf(keyval)).child("Date").setValue(day);
                    ref.child(String.valueOf(keyval)).child("Type").setValue(type);
                    ref.child(String.valueOf(keyval)).child("Description").setValue(desc);
                    Toast.makeText(getApplicationContext(), "Data Successfully Added", Toast.LENGTH_SHORT).show();
                    for (int i = 1; i <= Integer.parseInt(newnewpos); i++) {
                        newref.child(String.valueOf(i)).child("Notification_Viewed_Finance").setValue("No");
                    }
                    Intent intent = new Intent(Admin_Finance.this, Admin_Update_Finance.class);
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
        Intent intent = new Intent(this, Admin_Update_Finance.class);
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
