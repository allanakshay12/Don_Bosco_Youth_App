package com.allanakshay.donboscoyouth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Finance_Update_Edit extends AppCompatActivity {

    private ArrayList<String> amountlist = new ArrayList<>();
    private DatabaseReference ref;
    private FirebaseDatabase fbref;
    String key;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance__update__edit);
        amountlist = FinanceUpdateAdapter.editstuff;
        String[] months =new String[]{"January","February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] days =new String[]{"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        String[] type = new String[]{"Income", "Expense"};

        final EditText amount_edit = (EditText)findViewById(R.id.admin_finance_amt_edit);
        final EditText desc_edit = (EditText)findViewById(R.id.admin_finance_desc_edit);

        final Spinner month = (Spinner)findViewById(R.id.admin_finance_month_edit);
        final Spinner date = (Spinner)findViewById(R.id.admin_finance_day_edit);
        final Spinner type_spin = (Spinner)findViewById(R.id.admin_finance_type_edit);

        amount_edit.setText(amountlist.get(5));
        desc_edit.setText(amountlist.get(3));

        ArrayAdapter<String> arrayAdapterdate = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, days);
        date.setAdapter(arrayAdapterdate);
        ArrayAdapter<String> arrayAdaptermonth = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, months);
        month.setAdapter(arrayAdaptermonth);
        ArrayAdapter<String> arrayAdaptertype = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, type);
        type_spin.setAdapter(arrayAdaptertype);

        date.setSelection(Integer.parseInt(amountlist.get(2).toString())-1);

        if(amountlist.get(1).toString().equals("January"))
            month.setSelection(0);
        else if(amountlist.get(1).toString().equals("February"))
            month.setSelection(1);
        else if(amountlist.get(1).toString().equals("March"))
            month.setSelection(2);
        else if(amountlist.get(1).toString().equals("April"))
            month.setSelection(3);
        else if(amountlist.get(1).toString().equals("May"))
            month.setSelection(4);
        else if(amountlist.get(1).toString().equals("June"))
            month.setSelection(5);
        else if(amountlist.get(1).toString().equals("July"))
            month.setSelection(6);
        else if(amountlist.get(1).toString().equals("August"))
            month.setSelection(7);
        else if(amountlist.get(1).toString().equals("September"))
            month.setSelection(8);
        else if(amountlist.get(1).toString().equals("October"))
            month.setSelection(9);
        else if(amountlist.get(1).toString().equals("November"))
            month.setSelection(10);
        else if(amountlist.get(1).toString().equals("December"))
            month.setSelection(11);

        if(amountlist.get(4).equals("Income"))
            type_spin.setSelection(0);
        else
            type_spin.setSelection(1);

        Button update = (Button)findViewById(R.id.finance_realupdate_edit);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(amount_edit.getText().toString().isEmpty() || desc_edit.getText().toString().isEmpty())) {
                    ref = fbref.getInstance().getReference().child("Finance_List");

                    ref.child(amountlist.get(0).toString()).child("Description").setValue(desc_edit.getText().toString());
                    ref.child(amountlist.get(0).toString()).child("Amount").setValue(amount_edit.getText().toString());
                    ref.child(amountlist.get(0).toString()).child("Month").setValue(month.getItemAtPosition(month.getSelectedItemPosition()).toString());
                    ref.child(amountlist.get(0).toString()).child("Date").setValue(date.getItemAtPosition(date.getSelectedItemPosition()).toString());
                    ref.child(amountlist.get(0).toString()).child("Type").setValue(type_spin.getItemAtPosition(type_spin.getSelectedItemPosition()).toString());
                    Toast.makeText(getApplicationContext(), "Data Successfully Updated", Toast.LENGTH_SHORT).show();


                    intent = new Intent(Finance_Update_Edit.this, Admin_Update_Finance.class);
                    startActivity(intent);
                    FinanceUpdateAdapter.editstuff.clear();
                    finish();

                }
                else
                    Toast.makeText(getApplicationContext(), "Please Enter all the Fields", Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(Finance_Update_Edit.this, Admin_Update_Finance.class);
        startActivity(intent);
        UpcomingEventsUpdateAdapter.editstuff.clear();
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
