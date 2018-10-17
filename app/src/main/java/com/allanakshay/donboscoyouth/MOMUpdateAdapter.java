package com.allanakshay.donboscoyouth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.allanakshay.donboscoyouth.Admin_Update_MOM.builder;

public class MOMUpdateAdapter extends BaseAdapter implements ListAdapter {
    private  static ArrayList<String> list = new ArrayList<String>();
    private Activity activity;
    private static ArrayList<String> monthlist = new ArrayList<String>();
    private static ArrayList<String> datelist = new ArrayList<String>();
    private static ArrayList<String> eventlist = new ArrayList<String>();
    private static ArrayList<String> arraydate = new ArrayList<>();
    private static ArrayList<String> arrayevent = new ArrayList<>();
    private Context context;
    public static DatabaseReference refnum;
    public static FirebaseDatabase fbdb;
    public static String newpos;
    public static Query queryremove;
    public static Query query;
    Intent intent;
    private String key;
    private int keyval;
    public static ValueEventListener queryevent;
    public static ValueEventListener querynewevent;
    public static ValueEventListener event;
    public static ValueEventListener editevent;
    public static ArrayList<String> editstuff = new ArrayList<>();
    private int lastPosition = -1;



    public MOMUpdateAdapter(ArrayList<String> list, Context context, Activity activity, ArrayList<String> arraydate, ArrayList<String> arrayevent) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.arraydate = arraydate;
        this.arrayevent = arrayevent;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.admineventlist, null);
        }


        //Handle TextView and display string from your list
        TextView listmonth = (TextView)view.findViewById(R.id.adminupdatemonth);
        listmonth.setText(list.get(position));
        final TextView listdate = (TextView)view.findViewById(R.id.adminupdatedate);
        listdate.setText(arraydate.get(position));
        TextView listevent = (TextView)view.findViewById(R.id.adminupdatedescription);
        listevent.setText(arrayevent.get(position));
        //Handle buttons and add onClickListeners
        Button editBtn = (Button)view.findViewById(R.id.eventupdate_edit);
        Button removeBtn = (Button)view.findViewById(R.id.eventupdate_remove);



        final View finalView = view;
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                refnum = fbdb.getInstance().getReference().child("MOM");

                queryremove =refnum.orderByChild("Description").equalTo(arrayevent.get(position));
                final String nlist = list.get(position);
                final String narraydate = arraydate.get(position);
                queryremove.addListenerForSingleValueEvent(editevent = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                        editstuff.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("Month").getValue().toString().equals(nlist) && snapshot.child("Date").getValue().toString().equals(narraydate)) {
                                newpos = snapshot.getKey();
                                editstuff.add(newpos);
                                editstuff.add(snapshot.child("Month").getValue().toString());
                                editstuff.add(snapshot.child("Date").getValue().toString());
                                editstuff.add(snapshot.child("Description").getValue().toString());
                                Admin_Update_MOM.maintainquery.removeEventListener(Admin_Update_MOM.myevent);
                                intent = new Intent(context, MOM_Update_Edit.class);
                                context.startActivity(intent);
                                activity.finish();
                            }
                        }
                        }catch (Exception e){}


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        removeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {

                    builder.setCancelable(true);
                    builder.setTitle("Alert");
                builder.setCancelable(true);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                    builder.setMessage("Are you sure you want to remove the MOM:\n" + arrayevent.get(position) + " \n\nNoted down for:\n " + list.get(position) + " " + arraydate.get(position)).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            refnum = fbdb.getInstance().getReference().child("MOM");

                            query = refnum.orderByKey().limitToLast(1);
                            query.addValueEventListener(queryevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        key = child.getKey();
                                        keyval = Integer.parseInt(key);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            refnum = fbdb.getInstance().getReference().child("MOM");

                            refnum.addListenerForSingleValueEvent(event = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            monthlist.add(snapshot.child("Month").getValue().toString());
                                            datelist.add(snapshot.child("Date").getValue().toString());
                                            eventlist.add(snapshot.child("Description").getValue().toString());
                                        }
                                    }catch(Exception e){}


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            refnum = fbdb.getInstance().getReference().child("MOM");

                            queryremove = refnum.orderByChild("Description").equalTo(arrayevent.get(position));
                            final String nlist = list.get(position);
                            final String narraydate = arraydate.get(position);

                            queryremove.addListenerForSingleValueEvent(querynewevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            if (snapshot.child("Month").getValue().toString().equals(nlist) && snapshot.child("Date").getValue().toString().equals(narraydate)) {
                                                newpos = snapshot.getKey();
                                                monthlist.remove(Integer.parseInt(newpos) - 1);
                                                datelist.remove(Integer.parseInt(newpos) - 1);
                                                eventlist.remove(Integer.parseInt(newpos) - 1);
                                                for (int i = 0; i < keyval - 1; i++) {
                                                    refnum.child(String.valueOf(i + 1)).child("Month").setValue(monthlist.get(i));
                                                    refnum.child(String.valueOf(i + 1)).child("Date").setValue(datelist.get(i));
                                                    refnum.child(String.valueOf(i + 1)).child("Description").setValue(eventlist.get(i));
                                                }
                                                refnum.child(String.valueOf(keyval)).removeValue();
                                            }


                                        }

                                    }catch(Exception e){}

                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            monthlist.clear();
                            datelist.clear();
                            eventlist.clear();
                            list.clear();
                            arraydate.clear();
                            arrayevent.clear();

                            Snackbar.make(v, "Minutes of Meeting Removed", Snackbar.LENGTH_SHORT).show();

                        }
                    });
                    builder.show();

            }
        });

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = position;

        return view;
    }
}