package com.example.ignacy.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.ignacy.myapplication.LoginActivity.email;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseHelper helper;
    private DatabaseReference mRootRef;
    private String name;
    private String userkey;
    private String surname;
    private String gender;
    private String group, city;
    private Integer count;
    private Double litres, litres_added, latitude, longitude;
    private Button buttonAdd;
    private TextView textLitres, textDate;
    private EditText editLitres, editDay, editMonth, editYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        editDay = (EditText) findViewById(R.id.editTextDay);
        editMonth = (EditText) findViewById(R.id.editTextMonth);
        editYear = (EditText) findViewById(R.id.editTextYear);
        editLitres = (EditText) findViewById(R.id.editTextLitres);
        textDate = (TextView) findViewById(R.id.textViewDate);
        textLitres = (TextView) findViewById(R.id.textViewLitres);
        buttonAdd.setOnClickListener(this);


        mRootRef = FirebaseDatabase.getInstance().getReference();

        helper = new FirebaseHelper(mRootRef);

        mRootRef.child("users")
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            userkey = childSnapshot.getKey();
                            User olduser = childSnapshot.getValue(User.class);
                            group = olduser.group;
                            gender = olduser.gender;
                            city = olduser.city;
                            name = olduser.name;
                            surname = olduser.surname;
                            count = olduser.count;
                            litres = olduser.litres;
                            latitude = olduser.latitude;
                            longitude = olduser.longitude;
                            email = olduser.email;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AddActivity.this, R.string.addXml3,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                Double litres_added = Double.parseDouble(editLitres.getText().toString());
                String day = editDay.getText().toString();
                String month = editMonth.getText().toString();
                String year = editYear.getText().toString();
                String date = day + "-" + month + "-" + year;
                User user = new User();
                user.setName(name);
                user.setSurname(surname);
                user.setEmail(email);
                user.setGender(gender);
                user.setCity(city);
                user.setLocation(latitude, longitude);
                user.setGroup(group);
                user.IncreaseLitres(litres + litres_added);
                user.IncreaseCount(count+1);
                user.setLastDate(date);
                helper.save(user);
                Toast.makeText(AddActivity.this, "Dodano poprawnie", Toast.LENGTH_SHORT).show();
                mRootRef.child("users").child(userkey).removeValue();
                Intent myIntent = new Intent(AddActivity.this, MainActivity.class);
                AddActivity.this.startActivity(myIntent);
        }
    }



}
