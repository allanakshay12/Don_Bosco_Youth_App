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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Update_Members extends AppCompatActivity {

    private DatabaseReference ref;
    private ArrayList<String> arrayname = new ArrayList<>();
    private FirebaseDatabase mfirebasedb;
    public static Query maintainquery;
    public static ValueEventListener myevent;
    private YouthMemberAdapter arrayAdapter;
    public static AlertDialog.Builder builder;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__update__members);
        Button admin_add = (Button)findViewById(R.id.memberupdate);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_from_top);
        admin_add.startAnimation(animation1);
        admin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Update_Members.this, Admin_member.class);
                startActivity(intent);
                maintainquery.removeEventListener(myevent);
                try {
                    YouthMemberAdapter.query.removeEventListener(YouthMemberAdapter.queryevent);
                } catch (Exception e) {
                } try {
                    YouthMemberAdapter.queryremove.removeEventListener(YouthMemberAdapter.querynewevent);
                } catch (Exception e) {
                } try {
                    YouthMemberAdapter.refnum.removeEventListener(YouthMemberAdapter.event);
                } catch (Exception e) {
                } try {
                    YouthMemberAdapter.queryremove.removeEventListener(YouthMemberAdapter.editevent);
                }
                catch (Exception e)
                {

                }
                finish();
            }
        });
        builder = new AlertDialog.Builder(this);
        ref = mfirebasedb.getInstance().getReference().child("Youth_List");

        maintainquery = ref.orderByChild("Name");
        maintainquery.addValueEventListener(myevent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                arrayname.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    arrayname.add(snapshot.child("Name").getValue().toString());

                }
                arrayAdapter.notifyDataSetChanged();
                }catch(Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        arrayAdapter = new YouthMemberAdapter(arrayname, Admin_Update_Members.this, this);
        ListView listView = (ListView)findViewById(R.id.youthmember_list);
        listView.setAdapter(arrayAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        maintainquery.removeEventListener(myevent);
        try {
            YouthMemberAdapter.query.removeEventListener(YouthMemberAdapter.queryevent);
        } catch (Exception e) {
        } try {
            YouthMemberAdapter.queryremove.removeEventListener(YouthMemberAdapter.querynewevent);
        } catch (Exception e) {
        } try {
            YouthMemberAdapter.refnum.removeEventListener(YouthMemberAdapter.event);
        } catch (Exception e) {
        } try {
            YouthMemberAdapter.queryremove.removeEventListener(YouthMemberAdapter.editevent);
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
