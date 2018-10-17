package com.allanakshay.donboscoyouth;

import android.content.Context;
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

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> listnumber = new ArrayList<String>();
    private ArrayList<String> listquery = new ArrayList<String>();
    private Context context;
    private DatabaseReference refnum;
    private FirebaseDatabase fbdb;
    private String newpos;
    Query query;
    private  int lastPosition = -1;



    public MyCustomAdapter(ArrayList<String> list, ArrayList<String> listnumber, Context context) {
        this.list = list;
        this.context = context;
        this.listnumber = listnumber;
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
            view = inflater.inflate(R.layout.attendancelist, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.attendancenameview);
        listItemText.setText(list.get(position));
        TextView listItemTextnumber = (TextView)view.findViewById(R.id.attendancenumberview);
        listItemTextnumber.setText(listnumber.get(position));
        //Handle buttons and add onClickListeners
        Button addBtn = (Button)view.findViewById(R.id.attendanceadd);
        Button subBtn = (Button)view.findViewById(R.id.attendancesubtract);
        final View finalView = view;
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                refnum = fbdb.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

                query =refnum.orderByChild("Name").equalTo(list.get(position));
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                newpos = snapshot.getKey();
                            int number = Integer.parseInt(dataSnapshot.child(newpos).child("Attendance").getValue().toString()) + 1;
                            refnum.child(newpos).child("Attendance").setValue(String.valueOf(number));
                        }catch (Exception e) {}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                list.clear();
                listnumber.clear();
                Snackbar.make(v, "Attendance Successfully Updated", Snackbar.LENGTH_SHORT).show();
            }
        });
        subBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                refnum = fbdb.getInstance().getReferenceFromUrl("https://donboscoyouth-24000.firebaseio.com/").child("Youth_List");

                query =refnum.orderByChild("Name").equalTo(list.get(position));
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                newpos = snapshot.getKey();
                            int number = Integer.parseInt(dataSnapshot.child(newpos).child("Attendance").getValue().toString()) - 1;
                            refnum.child(newpos).child("Attendance").setValue(String.valueOf(number));
                        } catch(Exception e) {}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                list.clear();
                listnumber.clear();
                Snackbar.make(v, "Attendance Successfully Updated", Snackbar.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}