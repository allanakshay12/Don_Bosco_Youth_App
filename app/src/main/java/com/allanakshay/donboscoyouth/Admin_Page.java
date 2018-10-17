package com.allanakshay.donboscoyouth;

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

public class Admin_Page extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__page);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_from_bottom);




        Button admin_attend = (Button)findViewById(R.id.attendanceupdate);
        admin_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Page.this, Admin_Attendance.class);
                startActivity(intent);
            }
        });
        admin_attend.startAnimation(animation);
        Button admin_update_member = (Button)findViewById(R.id.admin_update_members);
        admin_update_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Page.this, Admin_Update_Members.class);
                startActivity(intent);
            }
        });
        admin_update_member.startAnimation(animation);
        Button admin_update_mom = (Button)findViewById(R.id.admin_update_MOM);
        admin_update_mom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Page.this, Admin_Update_MOM.class);
                startActivity(intent);
            }
        });
        admin_update_mom.startAnimation(animation);
        Button admin_update_finance = (Button)findViewById(R.id.admin_update_finance);
        admin_update_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Page.this, Admin_Update_Finance.class);
                startActivity(intent);
            }
        });
        admin_update_finance.startAnimation(animation);
        Button admin_update_event = (Button)findViewById(R.id.admin_update_event);
        admin_update_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Page.this, Admin_Update_Events.class);
                startActivity(intent);
            }
        });
        admin_update_event.startAnimation(animation);
        Button admin_update_plan = (Button)findViewById(R.id.admin_update_plans);
        admin_update_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Page.this, Admin_Update_Plans.class);
                startActivity(intent);
            }
        });
        admin_update_plan.startAnimation(animation);
        Button admin_noti = (Button)findViewById(R.id.pushnoti);
        admin_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Admin_Page.this, Push_Notification.class);
                startActivity(intent);
            }
        });
        admin_noti.startAnimation(animation);
        ImageSet.setimage = "";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
