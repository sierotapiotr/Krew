package com.example.ignacy.myapplication;

import static com.example.ignacy.myapplication.LoginActivity.email;
import static com.example.ignacy.myapplication.MainActivity.user_location;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyData extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FirebaseHelper helper;
    private DatabaseReference mRootRef;
    private ValueEventListener mUserListener;

    private Button buttonSave;
    private EditText editTextName;
    private EditText editTextSurname;
    private Spinner spinnerCity;
    private RadioGroup rgroupGroup;
    private RadioButton rbutton0m;
    private RadioButton rbutton0p;
    private RadioButton rbuttonAm;
    private RadioButton rbuttonAp;
    private RadioButton rbuttonBm;
    private RadioButton rbuttonBp;
    private RadioButton rbuttonABm;
    private RadioButton rbuttonABp;
    private String userkey;
    private String gender;
    private String city;
    private String last_date;
    private Integer count;
    private Double litres, latitude, longitude;

    private Map<String,String> user_notifications;


    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        helper = new FirebaseHelper(mRootRef);
        progressDialog = new ProgressDialog(this);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        editTextName = (EditText) findViewById(R.id.editTextName2);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname2);

        spinnerCity = (Spinner) findViewById(R.id.spinnerCity2);
        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(MyData.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.miasta));
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapterCity);
        rgroupGroup = (RadioGroup) findViewById(R.id.radioGroupGroup2);
        rbutton0m = (RadioButton) findViewById(R.id.radioButton0m2);
        rbutton0p = (RadioButton) findViewById(R.id.radioButton0p2);
        rbuttonAm = (RadioButton) findViewById(R.id.radioButtonAm2);
        rbuttonAp = (RadioButton) findViewById(R.id.radioButtonAp2);
        rbuttonBm = (RadioButton) findViewById(R.id.radioButtonBm2);
        rbuttonBp = (RadioButton) findViewById(R.id.radioButtonBp2);
        rbuttonABm = (RadioButton) findViewById(R.id.radioButtonABm2);
        rbuttonABp = (RadioButton) findViewById(R.id.radioButtonABp2);
        buttonSave.setOnClickListener(this);
        rbutton0m.setOnClickListener(this);
        rbutton0p.setOnClickListener(this);
        rbuttonAm.setOnClickListener(this);
        rbuttonAp.setOnClickListener(this);
        rbuttonBm.setOnClickListener(this);
        rbuttonBp.setOnClickListener(this);
        rbuttonABm.setOnClickListener(this);
        rbuttonABp.setOnClickListener(this);
        spinnerCity.setOnItemSelectedListener(this);

        mRootRef.child("users")
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            userkey = childSnapshot.getKey();
                            User olduser = childSnapshot.getValue(User.class);
                            editTextName.setText(olduser.name);
                            editTextSurname.setText(olduser.surname);
                            gender = olduser.gender;
                            city = olduser.city;
                            count = olduser.count;
                            litres = olduser.litres;
                            latitude = olduser.latitude;
                            longitude = olduser.longitude;
                            last_date = olduser.last_date;

                            user_notifications = olduser.notifications;
                            switch(olduser.group){
                                case "0 Rh-":
                                    rbutton0m.toggle();
                                    break;
                                case "0 Rh+":
                                    rbutton0p.toggle();
                                    break;
                                case "A Rh-":
                                    rbuttonAm.toggle();
                                    break;
                                case "A Rh+":
                                    rbuttonAp.toggle();
                                    break;
                                case "B Rh-":
                                    rbuttonBm.toggle();
                                    break;
                                case "B Rh+":
                                    rbuttonBp.toggle();
                                    break;
                                case "AB Rh-":
                                    rbuttonABm.toggle();
                                    break;
                                case "AB Rh+":
                                    rbuttonABp.toggle();
                                    break;
                            }

                            switch (olduser.city) {
                                case "Białystok":
                                    spinnerCity.setSelection(0);
                                    break;
                                case "Bydgoszcz":
                                    spinnerCity.setSelection(1);
                                    break;
                                case "Gdańsk":
                                    spinnerCity.setSelection(2);
                                    break;
                                case "Kalisz":
                                    spinnerCity.setSelection(3);
                                    break;
                                case "Katowice":
                                    spinnerCity.setSelection(4);
                                    break;
                                case "Kielce":
                                    spinnerCity.setSelection(5);
                                    break;
                                case "Kraków":
                                    spinnerCity.setSelection(6);
                                    break;
                                case "Lublin":
                                    spinnerCity.setSelection(7);
                                    break;
                                case "Łódź":
                                    spinnerCity.setSelection(8);
                                    break;
                                case "Olsztyn":
                                    spinnerCity.setSelection(9);
                                    break;
                                case "Opole":
                                    spinnerCity.setSelection(10);
                                    break;
                                case "Poznań":
                                    spinnerCity.setSelection(11);
                                    break;
                                case "Racibórz":
                                    spinnerCity.setSelection(12);
                                    break;
                                case "Radom":
                                    spinnerCity.setSelection(13);
                                    break;
                                case "Rzeszów":
                                    spinnerCity.setSelection(14);
                                    break;
                                case "Słupsk":
                                    spinnerCity.setSelection(15);
                                    break;
                                case "Szczecin":
                                    spinnerCity.setSelection(16);
                                    break;
                                case "Wałbrzych":
                                    spinnerCity.setSelection(17);
                                    break;
                                case "Warszawa":
                                    spinnerCity.setSelection(18);
                                    break;
                                case "Wrocław":
                                    spinnerCity.setSelection(19);
                                    break;
                                case "Zielona Góra":
                                    spinnerCity.setSelection(20);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MyData.this, "Nie udało się wczytać danych.",
                                Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSave:
                final String name = editTextName.getText().toString().trim();
                final String surname = editTextSurname.getText().toString().trim();

                User user = new User();
                user.setName(name);
                user.setSurname(surname);
                user.setEmail(email);
                user.setGender(gender);
                user.setCity(city);
                user.setNotifications(user_notifications);
                user.setCount(count);
                user.setLitres(litres);
                user.setLastDate(last_date);
                user.setLocation(latitude, longitude);
                int checkedRadioButtonId = rgroupGroup.getCheckedRadioButtonId();

                switch (checkedRadioButtonId) {
                    case R.id.radioButton0m2:
                        if (rbutton0m.isChecked()) {
                            user.setGroup("0 Rh-");
                        }
                        break;
                    case R.id.radioButton0p2:
                        if (rbutton0p.isChecked()) {
                            user.setGroup("0 Rh+");
                        }
                        break;
                    case R.id.radioButtonAm2:
                        if (rbuttonAm.isChecked()) {
                            user.setGroup("A Rh-");
                        }
                        break;
                    case R.id.radioButtonAp2:
                        if (rbuttonAp.isChecked()) {
                            user.setGroup("A Rh+");
                        }
                        break;
                    case R.id.radioButtonBm2:
                        if (rbuttonBm.isChecked()) {
                            user.setGroup("B Rh-");
                        }
                        break;
                    case R.id.radioButtonBp2:
                        if (rbuttonBp.isChecked()) {
                            user.setGroup("B Rh+");
                        }
                        break;
                    case R.id.radioButtonABm2:
                        if (rbuttonABm.isChecked()) {
                            user.setGroup("AB Rh-");
                        }
                        break;
                    case R.id.radioButtonABp2:
                        if (rbuttonABp.isChecked()) {
                            user.setGroup("AB Rh+");
                        }
                        break;
                }
                helper.save(user);
                Toast.makeText(MyData.this, "Dane zaktualizowano poprawnie", Toast.LENGTH_SHORT).show();
                mRootRef.child("users").child(userkey).removeValue();



                Intent myIntent = new Intent(MyData.this, MainActivity.class);

                MyData.this.startActivity(myIntent);

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        city = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
