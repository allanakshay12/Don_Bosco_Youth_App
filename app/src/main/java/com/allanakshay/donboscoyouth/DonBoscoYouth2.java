package com.allanakshay.donboscoyouth;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Allan Akshay on 11-07-2017.
 */

public class DonBoscoYouth2 extends MultiDexApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
    }
}
