package com.example.ignacy.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

/**
 * Created by Ignacy on 2017-10-31.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseInsIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "New Token: " + token);

        storeRegIdInPref(token);


        sendRegistrationToServer(token);

        Intent registrationComp = new Intent("registrationComp");
        registrationComp.putExtra("token", token);

        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComp);



    }

    private void sendRegistrationToServer(final String token){
        Log.e("TAG","sendRegistrationToSErver: " + token);
    }

    private void storeRegIdInPref(String token){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Pref",0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId",token);
        editor.commit();
    }
}
