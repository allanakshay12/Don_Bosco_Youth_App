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

public class FB_Link extends AppCompatActivity {
    private DatabaseReference ref;
    private FirebaseDatabase mFirebaseDatabase;
    private String usernameedit;
    private String passwordedit;
    private ArrayList<String> usernames = new ArrayList<String>();;
    private ArrayList<String> passwords = new ArrayList<String>();
    private Intent intentlog;
    private String newpos;
    private static int found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb__link);
        Button link = (Button)findViewById(R.id.fb_link_to_app);
        final EditText username = (EditText)findViewById(R.id.username_link);
        final EditText password = (EditText)findViewById(R.id.password_link);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ref = mFirebaseDatabase.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        usernames.add(snapshot.child("Username").getValue().toString());
                        passwords.add(snapshot.child("Password").getValue().toString());
                    }
                } catch(Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameedit = username.getText().toString();
                passwordedit = password.getText().toString();
                if (!(usernameedit.isEmpty() || passwordedit.isEmpty())) {
                    if (check(usernameedit.trim(), passwordedit.trim())) {

                        Query query = ref.orderByChild("Username").equalTo(LoginPage.userid);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                        newpos = snapshot.getKey();
                                    ref.child(newpos).child("Facebook_UserID").setValue(LoginPage.fb_user_id);
                                }catch (Exception e) {}
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getApplicationContext(), "App Account linked successfully", Toast.LENGTH_SHORT).show();
                        intentlog = new Intent(FB_Link.this, MainPage.class);
                        startActivity(intentlog);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "App Account not found", Toast.LENGTH_SHORT).show();
                        intentlog = new Intent(FB_Link.this, LoginPage.class);
                        startActivity(intentlog);
                        finish();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Please Enter all the Fields", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean check(final String user1, final String pass1) {
        found = 0;

        for (int pos = 0; pos < usernames.size(); pos++) {
            String user12 = usernames.get(pos);
            String pass12 = passwords.get(pos);
            LoginPage.userid = user12;

            if (user1.equals(user12.trim())) {
                if (pass1.equals(pass12.trim())) {
                    found = 1;
                    break;
                }
            }
        }

        if(found == 0)
            return false;
        else
            return  true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "Linking Unsuccessful", Toast.LENGTH_SHORT).show();
        intentlog = new Intent(FB_Link.this, LoginPage.class);
        startActivity(intentlog);
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
