package com.allanakshay.donboscoyouth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Update_Events extends AppCompatActivity {

    private DatabaseReference ref;
    private ArrayList<String> arrayname = new ArrayList<>();
    private FirebaseDatabase mfirebasedb;
    public static Query maintainquery;
    public static ValueEventListener myevent;
    private UpcomingEventsUpdateAdapter arrayAdapter;
    public static AlertDialog.Builder builder;
    private ArrayList<String> arraydate = new ArrayList<>();
    private ArrayList<String> arrayevent = new ArrayList<>();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageSet.setimage = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__update__events);
        Button admin_add = (Button)findViewById(R.id.eventadminupdate);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
        admin_add.startAnimation(animation1);
        admin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Update_Events.this, Admin_upcoming.class);
                startActivity(intent);
                maintainquery.removeEventListener(myevent);
                try {
                    UpcomingEventsUpdateAdapter.query.removeEventListener(UpcomingEventsUpdateAdapter.queryevent);
                } catch (Exception e) {
                } try {
                    UpcomingEventsUpdateAdapter.queryremove.removeEventListener(UpcomingEventsUpdateAdapter.querynewevent);
                } catch (Exception e) {
                } try {
                    UpcomingEventsUpdateAdapter.refnum.removeEventListener(UpcomingEventsUpdateAdapter.event);
                } catch (Exception e) {
                } try {
                    UpcomingEventsUpdateAdapter.queryremove.removeEventListener(UpcomingEventsUpdateAdapter.editevent);
                }
                catch (Exception e)
                {

                }
                finish();
            }
        });
        builder = new AlertDialog.Builder(this);
        ref = mfirebasedb.getInstance().getReference().child("Upcoming_Events");

        maintainquery = ref;
        maintainquery.addValueEventListener(myevent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                arrayname.clear();
                arraydate.clear();
                arrayevent.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    arrayname.add(snapshot.child("Month").getValue().toString());
                    arraydate.add(snapshot.child("Date").getValue().toString());
                    arrayevent.add(snapshot.child("Description").getValue().toString());

                }
                arrayAdapter.notifyDataSetChanged();
                }catch(Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        arrayAdapter = new UpcomingEventsUpdateAdapter(arrayname, Admin_Update_Events.this, this, arraydate, arrayevent);
        ListView listView = (ListView)findViewById(R.id.eventadmin_list);
        listView.setAdapter(arrayAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        maintainquery.removeEventListener(myevent);
        try {
            UpcomingEventsUpdateAdapter.query.removeEventListener(YouthMemberAdapter.queryevent);
        } catch (Exception e) {
        } try {
            UpcomingEventsUpdateAdapter.queryremove.removeEventListener(YouthMemberAdapter.querynewevent);
        } catch (Exception e) {
        } try {
            UpcomingEventsUpdateAdapter.refnum.removeEventListener(YouthMemberAdapter.event);
        } catch (Exception e) {
        } try {
            UpcomingEventsUpdateAdapter.queryremove.removeEventListener(YouthMemberAdapter.editevent);
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
