package com.example.ignacy.myapplication;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;

public class FirebaseHelper {
    DatabaseReference db;
    Boolean saved;
    ArrayList<User> users=new ArrayList<>();

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    public boolean save(User user)
    {
        if(user==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("users").push().setValue(user);
                saved=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }
        return saved;
    }


    private void fetchData(DataSnapshot dataSnapshot)
    {
        users.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            User user=ds.getValue(User.class);
            users.add(user);
        }
    }

}