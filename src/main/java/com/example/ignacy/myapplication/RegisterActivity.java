package com.example.ignacy.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextSurname;
    private TextView textViewSignin;
    private Spinner spinnerCity;
    private String city;
    private RadioGroup rgroupGroup;
    private RadioButton rbutton0m;
    private RadioButton rbutton0p;
    private RadioButton rbuttonAm;
    private RadioButton rbuttonAp;
    private RadioButton rbuttonBm;
    private RadioButton rbuttonBp;
    private RadioButton rbuttonABm;
    private RadioButton rbuttonABp;
    private RadioGroup rgroupGender;
    private RadioButton rbuttonMale;
    private RadioButton rbuttonFemale;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseHelper helper;
    private DatabaseReference db;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db);
        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editName);
        editTextSurname = (EditText) findViewById(R.id.editSurname);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.miasta));
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapterCity);
        rgroupGroup = (RadioGroup) findViewById(R.id.radioGroupGroup);
        rbutton0m = (RadioButton) findViewById(R.id.radioButton0m);
        rbutton0p = (RadioButton) findViewById(R.id.radioButton0p);
        rbuttonAm = (RadioButton) findViewById(R.id.radioButtonAm);
        rbuttonAp = (RadioButton) findViewById(R.id.radioButtonAp);
        rbuttonBm = (RadioButton) findViewById(R.id.radioButtonBm);
        rbuttonBp = (RadioButton) findViewById(R.id.radioButtonBp);
        rbuttonABm = (RadioButton) findViewById(R.id.radioButtonABm);
        rbuttonABp = (RadioButton) findViewById(R.id.radioButtonABp);
        rgroupGender = (RadioGroup) findViewById(R.id.radioGroupGender);
        rbuttonMale = (RadioButton) findViewById(R.id.radioButtonMale);
        rbuttonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);

        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        rbutton0m.setOnClickListener(this);
        rbutton0p.setOnClickListener(this);
        rbuttonAm.setOnClickListener(this);
        rbuttonAp.setOnClickListener(this);
        rbuttonBm.setOnClickListener(this);
        rbuttonBp.setOnClickListener(this);
        rbuttonABm.setOnClickListener(this);
        rbuttonABp.setOnClickListener(this);
        rbuttonMale.setOnClickListener(this);
        rbuttonFemale.setOnClickListener(this);
        spinnerCity.setOnItemSelectedListener(this);

    }

    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String surname = editTextSurname.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Wprowadź swój email",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Wprowadź swoje hasło",Toast.LENGTH_SHORT).show();
            return;
        }




        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Rejestracja zakończona sukcesem", Toast.LENGTH_SHORT).show();
                            User user = new User();
                            user.setEmail(email);
                            user.setName(name);
                            user.setSurname(surname);
                            user.setCity(city);
//                            user.notifications.add("testowe powiadomienie");
//                            user.notifications.add("2testowe powiadomienie");
                            String token = FirebaseInstanceId.getInstance().getToken();
                            user.setValidToken(token);

                            int checkedRadioButtonIdGender = rgroupGender.getCheckedRadioButtonId();

                            switch (checkedRadioButtonIdGender) {
                                case R.id.radioButtonMale:
                                    if (rbuttonMale.isChecked()) {
                                        user.setGender("M");
                                    }
                                    break;
                                case R.id.radioButtonFemale:
                                    if (rbuttonFemale.isChecked()) {
                                        user.setGender("K");
                                    }
                                    break;
                            }

                            int checkedRadioButtonIdGroup = rgroupGroup.getCheckedRadioButtonId();

                            switch (checkedRadioButtonIdGroup) {
                                case R.id.radioButton0m:
                                    if (rbutton0m.isChecked()) {
                                        user.setGroup("0 Rh-");
                                    }
                                    break;
                                case R.id.radioButton0p:
                                    if (rbutton0p.isChecked()) {
                                        user.setGroup("0 Rh+");
                                    }
                                    break;
                                case R.id.radioButtonAm:
                                    if (rbuttonAm.isChecked()) {
                                        user.setGroup("A Rh-");
                                    }
                                    break;
                                case R.id.radioButtonAp:
                                    if (rbuttonAp.isChecked()) {
                                        user.setGroup("A Rh+");
                                    }
                                    break;
                                case R.id.radioButtonBm:
                                    if (rbuttonBm.isChecked()) {
                                        user.setGroup("B Rh-");
                                    }
                                    break;
                                case R.id.radioButtonBp:
                                    if (rbuttonBp.isChecked()) {
                                        user.setGroup("B Rh+");
                                    }
                                    break;
                                case R.id.radioButtonABm:
                                    if (rbuttonABm.isChecked()) {
                                        user.setGroup("AB Rh-");
                                    }
                                    break;
                                case R.id.radioButtonABp:
                                    if (rbuttonABp.isChecked()) {
                                        user.setGroup("AB Rh+");
                                    }
                                    break;
                            }
                            helper.save(user);


                            Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);

                            RegisterActivity.this.startActivity(myIntent);
                        }
                        else{

                            Toast.makeText(RegisterActivity.this,"Rejestracja zakończona porażką",Toast.LENGTH_SHORT).show();

                        }

                    }

                });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonRegister){
            registerUser();
        }
        if(view == textViewSignin){

        }

    }

    public void onPostExecute(){
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        i.putExtra("caller", "MainActivity");
        startActivity(i);
        //finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        city = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
