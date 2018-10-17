package com.allanakshay.donboscoyouth;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class UpcomingEvents extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int month_no;
    public static int year;
    private ListView listview;
    private ListProductAdapter adapter;
    private List<Product> mProductList;
    Intent intentatten;
    private DatabaseReference ref;
    private FirebaseDatabase fbref;
    public ArrayList<String> arraymonth = new ArrayList<>();
    public ArrayList<String> arraydate = new ArrayList<>();
    public ArrayList<String> arraydesc = new ArrayList<>();
    private Product product;
    private List<Product> productList;
    private String datedate;
    private String finamonth;
    private String finadate;
    private String finadesc;
    public int checksize;
    private ArrayList<String> arraymonthname = new ArrayList<>();
    private ArrayList<String> arraydescname = new ArrayList<>();
    public int counterdayname;
    String dayname[] = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String currentyear;
    private String date;
    private SimpleDateFormat sdf;
    private Calendar cal;
    private CardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageSet.setimage = "Upcoming Events";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        listview = (ListView)findViewById(R.id.eventlist);

        ref = fbref.getInstance().getReference().child("Upcoming_Events");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view4);
        navigationView.setNavigationItemSelectedListener(this);

        //Get product list in db when db exists
        //Init adapter
        adapter = new ListProductAdapter(this, getListProduct(), null);
        //Set adapter for listview
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!arraydescname.get(position).isEmpty()) {
                    int i;
                    String month_name;
                    for (i = position; ; i--) {
                        month_name = arraymonthname.get(i);
                        if (!month_name.isEmpty())
                            break;
                    }
                    showMessage(((TextView) view.findViewById(R.id.tv_occu)).getText().toString(), ((TextView) view.findViewById(R.id.tv_ph_no)).getText().toString(), month_name, "Are you sure you want to add this event to your calendar?");
                }

            }
        });
        registerForContextMenu(listview);
        Toast.makeText(getApplicationContext(), "Tap on any event to add it to your Calendar", Toast.LENGTH_LONG).show();

    }




    public List<Product> getListProduct() {
        product = null;
        productList = new ArrayList<>();
        Query query = ref.orderByChild("Month");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        arraymonth.add(snapshot.child("Month").getValue().toString());
                        arraydate.add(snapshot.child("Date").getValue().toString());
                        arraydesc.add(snapshot.child("Description").getValue().toString());
                    }
                    checksize = arraydate.size();

                    cal = Calendar.getInstance();


                    sdf = new SimpleDateFormat("yyyy");
                    year = Integer.parseInt(sdf.format(cal.getTime()));
                    counterdayname = -1;
                    int counteryear = 2007;
                    for (int k = 0; k < (Integer.parseInt(currentyear = sdf.format(cal.getTime()))) - 2007; k++) {
                        if (counteryear % 4 == 0) {
                            counterdayname = counterdayname + 366;
                        } else
                            counterdayname = counterdayname + 365;
                        counteryear++;
                    }
                    sdf = new SimpleDateFormat("MMM");
                    date = sdf.format(cal.getTime());
                    sdf = new SimpleDateFormat("dd");
                    datedate = sdf.format(cal.getTime());
                    counterdayname = counterdayname + (Integer.parseInt(sdf.format(cal.getTime())));


                    int date_no = 1, date_no_day;
                    boolean month_more = true;
                    sdf = new SimpleDateFormat("yyyy");
                    if (date.equals("Jan")) {
                        date_no = 1;
                        month_more = true;

                    }
                    if (date.equals("Feb")) {
                        month_more = false;
                        date_no = 2;
                        counterdayname = counterdayname + 31;

                    }
                    if (date.equals("Mar")) {
                        month_more = true;
                        date_no = 3;
                        counterdayname = counterdayname + 31;

                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;

                    }
                    if (date.equals("Apr")) {
                        month_more = false;
                        date_no = 4;
                        counterdayname = counterdayname + 31;
                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;
                        counterdayname = counterdayname + 31;

                    }
                    if (date.equals("May")) {
                        month_more = true;
                        date_no = 5;
                        counterdayname = counterdayname + 31;
                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;

                    }
                    if (date.equals("Jun")) {
                        month_more = false;
                        date_no = 6;
                        counterdayname = counterdayname + 31;
                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;

                        counterdayname = counterdayname + 31;
                    }
                    if (date.equals("Jul")) {
                        month_more = true;
                        date_no = 7;
                        counterdayname = counterdayname + 31;
                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;

                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;
                    }
                    if (date.equals("Aug")) {
                        month_more = true;
                        date_no = 8;
                        counterdayname = counterdayname + 31;
                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;

                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;
                        counterdayname = counterdayname + 31;

                    }
                    if (date.equals("Sep")) {
                        month_more = false;
                        date_no = 9;
                        counterdayname = counterdayname + 31;
                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;

                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 31;

                    }
                    if (date.equals("Oct")) {
                        month_more = true;
                        date_no = 10;
                        counterdayname = counterdayname + 31;
                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;

                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;

                    }
                    if (date.equals("Nov")) {
                        month_more = false;
                        date_no = 11;
                        counterdayname = counterdayname + 31;
                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;

                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;
                        counterdayname = counterdayname + 31;

                    }
                    if (date.equals("Dec")) {
                        month_more = true;
                        date_no = 12;
                        counterdayname = counterdayname + 31;
                        if (Integer.parseInt(sdf.format(cal.getTime())) % 4 == 0)
                            counterdayname = counterdayname + 29;
                        else
                            counterdayname = counterdayname + 28;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;

                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;
                        counterdayname = counterdayname + 31;
                        counterdayname = counterdayname + 30;
                    }
                    sdf = new SimpleDateFormat("dd");
                    datedate = sdf.format(cal.getTime());
                    date_no_day = Integer.parseInt(datedate);

                    int i = 1;
                    int countermonth = 0;
                    while (date_no != 12 || date_no_day != 32) {
                        if (date_no == 1)
                            finamonth = "January";
                        else if (date_no == 2)
                            finamonth = "February";
                        else if (date_no == 3)
                            finamonth = "March";
                        else if (date_no == 4)
                            finamonth = "April";
                        else if (date_no == 5)
                            finamonth = "May";
                        else if (date_no == 6)
                            finamonth = "June";
                        else if (date_no == 7)
                            finamonth = "July";
                        else if (date_no == 8)
                            finamonth = "August";
                        else if (date_no == 9)
                            finamonth = "September";
                        else if (date_no == 10)
                            finamonth = "October";
                        else if (date_no == 11)
                            finamonth = "November";
                        else if (date_no == 12)
                            finamonth = "December";

                        counterdayname = counterdayname % 7;

                        if (countermonth == 0) {
                            product = new Product(0, finamonth, "", "");
                            productList.add(product);
                            arraymonthname.add(finamonth);
                            arraydescname.add("");
                            countermonth++;
                        }
                        if (date_no == 2) {
                            if ((date_no_day = date_no_day % 30) == 0) {
                                month_more = true;
                                date_no++;
                            }
                        } else if (date_no == 7) {
                            if ((date_no_day = date_no_day % 32) == 0) {
                                month_more = true;
                                date_no++;
                            }
                        } else if (date_no == 12) {

                        } else if (month_more) {
                            if ((date_no_day = date_no_day % 32) == 0) {
                                month_more = false;
                                date_no++;
                            }
                        } else {
                            if ((date_no_day = date_no_day % 31) == 0) {
                                month_more = true;
                                date_no++;
                            }
                        }

                        if (date_no == 1)
                            finamonth = "January";
                        else if (date_no == 2)
                            finamonth = "February";
                        else if (date_no == 3)
                            finamonth = "March";
                        else if (date_no == 4)
                            finamonth = "April";
                        else if (date_no == 5)
                            finamonth = "May";
                        else if (date_no == 6)
                            finamonth = "June";
                        else if (date_no == 7)
                            finamonth = "July";
                        else if (date_no == 8)
                            finamonth = "August";
                        else if (date_no == 9)
                            finamonth = "September";
                        else if (date_no == 10)
                            finamonth = "October";
                        else if (date_no == 11)
                            finamonth = "November";
                        else if (date_no == 12)
                            finamonth = "December";

                        if (date_no_day == 0) {
                            finadate = "";
                            finadesc = "";
                            product = new Product(i, finamonth, finadate, finadesc);
                            productList.add(product);
                            arraymonthname.add(finamonth);
                            arraydescname.add(finadesc);
                            i++;
                            date_no_day++;
                            continue;
                        } else {
                            finadate = String.valueOf(date_no_day);
                            if (Integer.parseInt(currentyear) % 4 != 0 && date_no == 2 && Integer.parseInt(finadate) == 29) {
                                date_no_day++;
                                i++;
                                continue;
                            } else {
                                finadesc = "";
                            }
                        }

                        int counter = 0;
                        for (int j = 0; j < checksize; j++) {
                            if (finamonth.equals(arraymonth.get(j)) && finadate.equals(arraydate.get(j))) {
                                if (counter == 0) {
                                    finadesc = arraydesc.get(j);
                                    counter++;
                                } else
                                    finadesc = finadesc + " || " + arraydesc.get(j);
                            }
                        }
                        product = new Product(i, dayname[counterdayname], finadate, finadesc);
                        productList.add(product);
                        arraymonthname.add("");
                        arraydescname.add(finadesc);
                        counterdayname++;
                        i++;
                        date_no_day++;
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return productList;
    }


    private void showMessage(final String name, final String date, final String month, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(month + "\t\t" + date);
        builder.setMessage(name + "\n\n" + Message);
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Calendar cal = Calendar.getInstance();
                if(month.equals("January"))
                    month_no=0;
                if(month.equals("February"))
                    month_no=1;
                if(month.equals("March"))
                    month_no=2;
                if(month.equals("April"))
                    month_no=3;
                if(month.equals("May"))
                    month_no=4;
                if(month.equals("June"))
                    month_no=5;
                if(month.equals("July"))
                    month_no=6;
                if(month.equals("August"))
                    month_no=7;
                if(month.equals("September"))
                    month_no=8;
                if(month.equals("October"))
                    month_no=9;
                if(month.equals("November"))
                    month_no=10;
                if(month.equals("December"))
                    month_no=11;
                cal.set(year, month_no, Integer.parseInt(date));
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", true);
                intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
                intent.putExtra("title", name);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upcoming_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings4) {
            intentatten = new Intent(UpcomingEvents.this, AboutUs.class);
            startActivity(intentatten);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.upcomingevents4) {
            // Handle the camera action
        } else if (id == R.id.knowyourgroup4) {

            intentatten = new Intent(UpcomingEvents.this, KnowGroup.class);
            startActivity(intentatten);
            finish();

        } else if (id == R.id.attendance4) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentatten = new Intent(UpcomingEvents.this, Attendance.class);
                startActivity(intentatten);
                finish();
            }else
                Toast.makeText(getApplicationContext(), "Please login to gain access to this content", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.photogallery4) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentatten = new Intent(UpcomingEvents.this, PhotoGallery.class);
                startActivity(intentatten);
                finish();
            }else
                Toast.makeText(getApplicationContext(), "Please login to gain access to this content", Toast.LENGTH_SHORT).show();

        }  else if (id == R.id.join4) {
            intentatten = new Intent(UpcomingEvents.this, JoinUs.class);
            startActivity(intentatten);
            finish();

        }   else if (id == R.id.feedback4) {
            intentatten = new Intent(UpcomingEvents.this, Feedback.class);
            startActivity(intentatten);

        }   else if (id == R.id.finance4) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentatten = new Intent(UpcomingEvents.this, Finance.class);
                startActivity(intentatten);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }    else if (id == R.id.mom4) {
            intentatten = new Intent(UpcomingEvents.this, MOM.class);
            startActivity(intentatten);
            finish();
        }   else if (id == R.id.myprofile_drawer4) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentatten = new Intent(UpcomingEvents.this, My_Profile.class);
                startActivity(intentatten);
                finish();
            } else
                Toast.makeText(UpcomingEvents.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
