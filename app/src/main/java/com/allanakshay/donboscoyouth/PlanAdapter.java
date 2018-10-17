package com.allanakshay.donboscoyouth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import static com.allanakshay.donboscoyouth.Admin_Update_Plans.builder;

public class PlanAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Activity activity;
    private ArrayList<String> newnamelist = new ArrayList<String>();
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
    private int lastPosition =-1;



    public PlanAdapter(ArrayList<String> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
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
            view = inflater.inflate(R.layout.youthmember_list, null);
        }


        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.youthmembername);
        listItemText.setText(list.get(position));
        //Handle buttons and add onClickListeners
        Button editBtn = (Button)view.findViewById(R.id.youthmember_edit);
        Button removeBtn = (Button)view.findViewById(R.id.youthmember_remove);
        final View finalView = view;
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Admin_Update_Plans.maintainquery.removeEventListener(Admin_Update_Plans.myevent);
                refnum = fbdb.getInstance().getReference().child("Upcoming_Plans");

                queryremove =refnum.orderByChild("Plan").equalTo(list.get(position));
                queryremove.addListenerForSingleValueEvent(editevent = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            editstuff.clear();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                newpos = snapshot.getKey();
                                editstuff.add(newpos);
                                editstuff.add(snapshot.child("Plan").getValue().toString());
                            }
                            intent = new Intent(context, Plan_update_edit.class);
                            context.startActivity(intent);
                            activity.finish();
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
                builder.setMessage("Are you sure you want to remove  the plan :\n"+ list.get(position)).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        refnum = fbdb.getInstance().getReference().child("Upcoming_Plans");

                        query = refnum.orderByKey().limitToLast(1);
                        query.addValueEventListener(queryevent = new ValueEventListener() {
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
                        refnum = fbdb.getInstance().getReference().child("Upcoming_Plans");

                        refnum.addListenerForSingleValueEvent(event = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        newnamelist.add(snapshot.child("Plan").getValue().toString());
                                    }
                                }catch (Exception e){}
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        refnum = fbdb.getInstance().getReference().child("Upcoming_Plans");

                        queryremove =refnum.orderByChild("Plan").equalTo(list.get(position));
                        queryremove.addListenerForSingleValueEvent(querynewevent = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                        newpos = snapshot.getKey();
                                    newnamelist.remove(Integer.parseInt(newpos) - 1);
                                    for (int i = 0; i < keyval - 1; i++) {
                                        refnum.child(String.valueOf(i + 1)).child("Plan").setValue(newnamelist.get(i));
                                    }
                                    refnum.child(String.valueOf(keyval)).removeValue();

                                }catch(Exception e){}

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        newnamelist.clear();
                        list.clear();

                        Snackbar.make(v, "Plan Removed", Snackbar.LENGTH_SHORT).show();

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