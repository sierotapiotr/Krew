package com.example.ignacy.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.ignacy.myapplication.LoginActivity.email;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btnLocation;
    private TextView textLocation;
    static LatLng user_location;
    static double user_latitude;
    static double user_longitude;
    private String user_name;
    private String user_surname;
    private String user_email;
    private String user_group;
    private String user_gender;
    private String user_city;
    private Integer user_count;
    private Double user_litres;
    private String user_last_date;
    private Map<String,String> user_notifications;
    private User user;
    private FirebaseHelper helper;
    private DatabaseReference mRootRef;

    private LocationManager locationManager;
    private LocationListener locationListener;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        btnLocation = (Button) findViewById(R.id.buttonLocation);
        textLocation = (TextView) findViewById(R.id.textViewLocation);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        startService(new Intent(this, MyFirebaseMessagingService.class));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textLocation.setText("\n " + location.getLatitude() + " " + location.getLongitude());
                user_location = new LatLng(location.getLatitude(), location.getLongitude());
                user_latitude = location.getLatitude();
                user_longitude = location.getLongitude();

                mRootRef.child("users")
                        .orderByChild("email")
                        .equalTo(email)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String userkey = childSnapshot.getKey();
                                    User olduser = childSnapshot.getValue(User.class);
                                    user_email = olduser.getEmail();
                                    user_group = olduser.getGroup();
                                    user_gender = olduser.getGender();
                                    user_name = olduser.getName();
                                    user_surname = olduser.getSurname();
                                    user_city = olduser.getCity();
                                    user_count = olduser.count;
                                    user_litres = olduser.litres;
                                    user_last_date = olduser.last_date;
                                    //user_notifications = new ArrayList<>();
                                    user_notifications = olduser.notifications;
                                    user = new User();
                                    user.setName(user_name);
                                    user.setSurname(user_surname);
                                    user.setEmail(user_email);
                                    user.setGroup(user_group);
                                    user.setGender(user_gender);
                                    user.setCity(user_city);
                                    user.setNotifications(user_notifications);
                                    user.setLocation(user_latitude, user_longitude);
                                    helper = new FirebaseHelper(mRootRef);
                                    helper.save(user);
                                    mRootRef.child("users").child(userkey).removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "Nie udało się wczytać danych.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intentEnableLocation = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intentEnableLocation);
            }
        };


        configureButton();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button btnShowToken = (Button) findViewById(R.id.button_show_token);
        btnShowToken.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d(TAG,"Token: " + token);
                Toast.makeText(MainActivity.this,token,Toast.LENGTH_SHORT).show();

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Snackbar.make(view2, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configureButton();
                break;
            default:
                break;
        }
    }

    void configureButton(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonInbox:
                intent = new Intent(MainActivity.this, InboxActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonList:
                intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonMyData:
                intent = new Intent(MainActivity.this, MyData.class);
                startActivity(intent);
                break;
            case R.id.buttonMyStats:
                intent = new Intent(MainActivity.this, Stats.class);
                startActivity(intent);
                break;
            case R.id.buttonAdd:
                intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;


        }

    }

}
