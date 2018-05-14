package com.example.ignacy.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import static com.example.ignacy.myapplication.LoginActivity.email;

public class Stats extends AppCompatActivity {

    private FirebaseHelper helper;
    private DatabaseReference mRootRef;
    private Integer count;
    private Double litres;
    private String last_date;
    private TextView textCount;
    private TextView textLitres;
    private TextView textHonor;
    private TextView textHonorp;
    private TextView textDays;
    private Integer remaining_days;
    private Double remaining_litres;
    private Double remaining_litres_p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(mRootRef);

        textCount = (TextView) findViewById(R.id.textViewCount);
        textLitres = (TextView) findViewById(R.id.textViewLitres);
        textHonor = (TextView) findViewById(R.id.textViewHonor);
        textHonorp = (TextView) findViewById(R.id.textViewHonorPlus);
        textDays = (TextView) findViewById(R.id.textViewDays);



        mRootRef.child("users")
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            User user = childSnapshot.getValue(User.class);
                            count = user.count;
                            litres = user.litres;
                            last_date = user.last_date;
                            textCount.append(count.toString());
                            textLitres.append(litres.toString());
                            Date current_date = Calendar.getInstance().getTime();
                            //remaining_days = daysBetween(last_date, current_date);
                            remaining_days = 0;

                            remaining_litres = 5 - litres;
                            remaining_litres_p = 25 - litres;
                            textDays.append(remaining_days.toString());
                            if (litres >= 25){
                                textHonorp.setText("Przysługuje Ci tytuł Honorowego Dawcy Krwi - Zasłużonego dla Zdrowia Narodu");
                            }
                            else
                            {
                                textHonorp.append(remaining_litres_p.toString());
                            }
                            if (litres >= 5){
                                textHonorp.setText("Przysługuje Ci tytuł Zasłużonego Honorowego Dawcy Krwi");
                            }
                            else
                            {
                                textHonorp.append(remaining_litres.toString());
                            }


                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Stats.this, "Nie udało się wczytać danych.",
                                Toast.LENGTH_SHORT).show();
                    }

                });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    public static int daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        int daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

}

