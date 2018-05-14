package com.example.ignacy.myapplication;


import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.ignacy.myapplication.LoginActivity.email;

public class InboxActivity extends Activity {
    private List<String> user_notifications;
    private DatabaseReference mRootRef;
    private ListView listNotification;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        user_notifications = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user_notifications);

        listNotification = (ListView) findViewById(R.id.listNotificatio);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("users")
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot entry : dataSnapshot.getChildren()) {
                            String userkey = entry.getKey();
                            String dane = entry.getValue().toString();

                            User user = entry.getValue(User.class);


                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("users/"+userkey+"/");
                            DatabaseReference usersRef = ref1.child("notifications");
                            String database = usersRef.getDatabase().getClass().toString();
                            System.out.println(database);

                            System.out.println(ref1);

                            for (Map.Entry<String, String> notification : user.notifications.entrySet())
                            {
                                user_notifications.add(notification.getValue());
                            }
                            listNotification.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(InboxActivity.this, "Nie udało się wczytać danych.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }



}