package com.allanakshay.donboscoyouth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Admin_Update_Finance extends AppCompatActivity {

    private DatabaseReference ref;
    private ArrayList<String> arraytype = new ArrayList<>();
    private ArrayList<String> arrayamount = new ArrayList<>();
    private ArrayList<String> arraydesc = new ArrayList<>();
    private FirebaseDatabase mfirebasedb;
    public static Query maintainquery;
    public static ValueEventListener myevent;
    private FinanceUpdateAdapter arrayAdapter;
    public static AlertDialog.Builder builder;
    private ArrayList<String> arraydate = new ArrayList<>();
    private ArrayList<String> arraymonth = new ArrayList<>();
    private ArrayList<String> keydata = new ArrayList<>();
    private String[] months =new String[]{"January","February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    Intent intent;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__update__finance);
        Button admin_add = (Button)findViewById(R.id.financeupdate);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
        admin_add.startAnimation(animation1);
        admin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Update_Finance.this, Admin_Finance.class);
                startActivity(intent);
                maintainquery.removeEventListener(myevent);
                try {
                    FinanceUpdateAdapter.query.removeEventListener(FinanceUpdateAdapter.queryevent);
                } catch (Exception e) {
                } try{
                    FinanceUpdateAdapter.queryremove.removeEventListener(FinanceUpdateAdapter.querynewevent);
                } catch (Exception e) {
                } try {
                    FinanceUpdateAdapter.refnum.removeEventListener(FinanceUpdateAdapter.event);
                } catch (Exception e) {
                } try {
                    FinanceUpdateAdapter.queryremove.removeEventListener(FinanceUpdateAdapter.editevent);
                }
                catch (Exception e)
                {

                }
                finish();
            }
        });
        builder = new AlertDialog.Builder(this);
        ref = mfirebasedb.getInstance().getReference().child("Finance_List");

        maintainquery = ref;
        maintainquery.addValueEventListener(myevent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                arraymonth.clear();
                arraydate.clear();
                arraytype.clear();
                arraydesc.clear();
                arrayamount.clear();
                    keydata.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    arraymonth.add(snapshot.child("Month").getValue().toString());
                    arraydate.add(snapshot.child("Date").getValue().toString());
                    arraytype.add(snapshot.child("Type").getValue().toString());
                    arraydesc.add(snapshot.child("Description").getValue().toString());
                    arrayamount.add(snapshot.child("Amount").getValue().toString());

                }
                arrayAdapter.notifyDataSetChanged();
                    final String keydata[] = new String[arraydate.size()];
                        count = 0;

                            for(int j=0; j<12; j++)
                            {
                                for(int k=1; k<=31;k++)
                                {
                                    for(int i = 0; i< arraymonth.size(); i++) {
                                        if (arraymonth.get(i).equals(months[j]) && arraydate.get(i).equals(String.valueOf(k)))
                                            keydata[i] = (String.valueOf((++count)*-1));
                                    }

                                }
                            }
                            for(int i=0; i<arraymonth.size(); i++)
                            {
                                ref.child(String.valueOf(i+1)).child("Key Value").setValue(Integer.parseInt(keydata[i]));
                            }


                }catch(Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        arrayAdapter = new FinanceUpdateAdapter(arraymonth, Admin_Update_Finance.this, this, arraydate, arraydesc, arraytype, arrayamount);
        ListView listView = (ListView)findViewById(R.id.financeadmin_list);
        listView.setAdapter(arrayAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            FinanceUpdateAdapter.query.removeEventListener(FinanceUpdateAdapter.queryevent);
        } catch (Exception e) {
        } try {
            FinanceUpdateAdapter.queryremove.removeEventListener(FinanceUpdateAdapter.querynewevent);
        } catch (Exception e) {
        } try {
            FinanceUpdateAdapter.refnum.removeEventListener(FinanceUpdateAdapter.event);
        } catch (Exception e) {
        } try {
            FinanceUpdateAdapter.queryremove.removeEventListener(FinanceUpdateAdapter.editevent);
        }
        catch (Exception e)
        {

        }
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
