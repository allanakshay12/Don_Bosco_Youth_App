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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.allanakshay.donboscoyouth.Admin_Update_Members.builder;

public class YouthMemberAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Activity activity;
    private ArrayList<String> newnamelist = new ArrayList<String>();
    private ArrayList<String> newattendancelist = new ArrayList<String>();
    private ArrayList<String> newdobdatelist = new ArrayList<String>();
    private ArrayList<String> newdobmonthlist = new ArrayList<String>();
    private ArrayList<String> newdesignationlist = new ArrayList<String>();
    private ArrayList<String> newfbuserlist = new ArrayList<String>();
    private ArrayList<String> newimageurllist = new ArrayList<String>();
    private ArrayList<String> newoccupationlist = new ArrayList<String>();
    private ArrayList<String> newpasswordlist = new ArrayList<String>();
    private ArrayList<String> newusernamelist = new ArrayList<String>();
    private ArrayList<String> newphonenolist = new ArrayList<String>();
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
    private StorageReference storageReference;
    private String photo;



    public YouthMemberAdapter(ArrayList<String> list, Context context, Activity activity) {
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
                Admin_Update_Members.maintainquery.removeEventListener(Admin_Update_Members.myevent);
                refnum = fbdb.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

                queryremove =refnum.orderByChild("Name").equalTo(list.get(position));
                queryremove.addListenerForSingleValueEvent(editevent = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                        editstuff.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            newpos = snapshot.getKey();
                            editstuff.add(newpos);
                            editstuff.add(snapshot.child("Name").getValue().toString());
                            editstuff.add(snapshot.child("Phone Number").getValue().toString());
                            editstuff.add(snapshot.child("Username").getValue().toString());
                            editstuff.add(snapshot.child("Password").getValue().toString());
                            editstuff.add(snapshot.child("Occupation").getValue().toString());
                            editstuff.add(snapshot.child("DOB Date").getValue().toString());
                            editstuff.add(snapshot.child("DOB Month").getValue().toString());
                            editstuff.add(snapshot.child("Designation").getValue().toString());
                        }
                        intent = new Intent(context, Youthmember_update_edit.class);
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
                storageReference = FirebaseStorage.getInstance().getReference();
                builder.setCancelable(true);
                builder.setTitle("Alert");
                builder.setCancelable(true);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setMessage("Are you sure you want to remove "+ list.get(position) + " ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        refnum = fbdb.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

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
                        refnum = fbdb.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

                        refnum.addListenerForSingleValueEvent(event = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        newnamelist.add(snapshot.child("Name").getValue().toString());
                                        newimageurllist.add(snapshot.child("Imageurl").getValue().toString());
                                        newattendancelist.add(snapshot.child("Attendance").getValue().toString());
                                        newdobdatelist.add(snapshot.child("DOB Date").getValue().toString());
                                        newdobmonthlist.add(snapshot.child("DOB Month").getValue().toString());
                                        newdesignationlist.add(snapshot.child("Designation").getValue().toString());
                                        newfbuserlist.add(snapshot.child("Facebook_UserID").getValue().toString());
                                        newoccupationlist.add(snapshot.child("Occupation").getValue().toString());
                                        newpasswordlist.add(snapshot.child("Password").getValue().toString());
                                        newphonenolist.add(snapshot.child("Phone Number").getValue().toString());
                                        newusernamelist.add(snapshot.child("Username").getValue().toString());
                                    }
                                }catch (Exception e){}
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        refnum = fbdb.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

                        queryremove =refnum.orderByChild("Name").equalTo(list.get(position));
                        queryremove.addListenerForSingleValueEvent(querynewevent = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        newpos = snapshot.getKey();
                                        photo = snapshot.child("Imageurl").getValue().toString();
                                    }
                                        if(!photo.equals(""))
                                        {
                                            StorageReference remove = storageReference.child(photo);
                                            remove.delete();
                                        }
                                    newusernamelist.remove(Integer.parseInt(newpos) - 1);
                                    newimageurllist.remove(Integer.parseInt(newpos) - 1);
                                    newattendancelist.remove(Integer.parseInt(newpos) - 1);
                                    newdobdatelist.remove(Integer.parseInt(newpos) - 1);
                                    newdobmonthlist.remove(Integer.parseInt(newpos) - 1);
                                    newpasswordlist.remove(Integer.parseInt(newpos) - 1);
                                    newfbuserlist.remove(Integer.parseInt(newpos) - 1);
                                    newdesignationlist.remove(Integer.parseInt(newpos) - 1);
                                    newnamelist.remove(Integer.parseInt(newpos) - 1);
                                    newphonenolist.remove(Integer.parseInt(newpos) - 1);
                                    newoccupationlist.remove(Integer.parseInt(newpos) - 1);
                                   for (int i = 0; i < keyval - 1; i++) {
                                        refnum.child(String.valueOf(i + 1)).child("Attendance").setValue(newattendancelist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("Imageurl").setValue(newimageurllist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("Name").setValue(newnamelist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("DOB Date").setValue(newdobdatelist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("DOB Month").setValue(newdobmonthlist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("Designation").setValue(newdesignationlist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("Facebook_UserID").setValue(newfbuserlist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("Occupation").setValue(newoccupationlist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("Password").setValue(newpasswordlist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("Phone Number").setValue(newphonenolist.get(i));
                                        refnum.child(String.valueOf(i + 1)).child("Username").setValue(newusernamelist.get(i));
                                    }
                                    refnum.child(String.valueOf(keyval)).removeValue();

                                }catch(Exception e){}

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        newattendancelist.clear();
                        newnamelist.clear();
                        newdobdatelist.clear();
                        newdobmonthlist.clear();
                        newdesignationlist.clear();
                        newfbuserlist.clear();
                        newoccupationlist.clear();
                        newpasswordlist.clear();
                        newphonenolist.clear();
                        newusernamelist.clear();
                        newimageurllist.clear();
                        list.clear();

                        Snackbar.make(v, "Member Account Removed", Snackbar.LENGTH_SHORT).show();

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