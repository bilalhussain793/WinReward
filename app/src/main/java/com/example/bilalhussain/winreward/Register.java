package com.example.bilalhussain.winreward;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    private Spinner sp_day,sp_month;
    private EditText et_phone,et_username,et_email,et_password,et_confirmpass,et_year,refreledit;
    ArrayAdapter adapter_day,adapter_month;
    Button reg_btn;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    String day,month,year,dob,nm,ph,em,pass,cnfm_pass,rf;
    ProgressDialog pd;

    FirebaseUserAdapter firebaseUserAdapter =new FirebaseUserAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_username=findViewById(R.id.et_username);
        et_email=findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_password=findViewById(R.id.et_pass);
        et_confirmpass=findViewById(R.id.et_cp);
        refreledit=findViewById(R.id.refrel);

        radioSexGroup=(RadioGroup)findViewById(R.id.r_group);
        et_year=findViewById(R.id.et_year);

        sp_day=findViewById(R.id.sp_day);
        sp_month=findViewById(R.id.sp_month);
        reg_btn=findViewById(R.id.r_btn);

        adapter_day=new ArrayAdapter(Register.this,R.layout.support_simple_spinner_dropdown_item,Array_Data.day);
        sp_day.setAdapter(adapter_day);



        adapter_month=new ArrayAdapter(Register.this,R.layout.support_simple_spinner_dropdown_item,Array_Data.months);
        sp_month.setAdapter(adapter_month);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addusermethod();
            }
        });

    }

    private void addusermethod(){

        rf=refreledit.getText().toString();
        nm=et_username.getText().toString();
        em=et_email.getText().toString();
        ph=et_phone.getText().toString();
        pass=et_password.getText().toString();
        cnfm_pass=et_confirmpass.getText().toString();
        dob=sp_day.getSelectedItem().toString()+"-"+sp_month.getSelectedItem().toString()+"-"+et_year.getText().toString();


        if(nm.length()==0){
            et_username.setError("Empty!");
        }else {
            if(em.length()==0){
                et_email.setError("Empty!");
            }else{
                if (pass.length()==0){
                    et_password.setError("Empty");
                }else {
                    if(cnfm_pass.length()==0){
                        et_confirmpass.setError("Empty");
                    }else {
                        if(ph.length()==0){
                            et_phone.setError("Empty");
                        }else{
                            if(em.contains("@")&&em.contains(".com")){
                                if(pass.equals(cnfm_pass)){
// Write a message to the database
                                    pd=new ProgressDialog(Register.this);
                                    pd.setTitle("Loading....");
                                    pd.show();

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("users/"+ph);

                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // This method is called once with the initial value and again
                                            // whenever data at this location is updated.
                                            String value = dataSnapshot.child("Email").getValue(String.class);
                                            String value2 = dataSnapshot.child("Phone").getValue(String.class);
                                            if(ph.equals(value2) ){

                                                et_phone.setError("Phone is already Registered");
                                               // Toast.makeText(Register.this, "Phone is already Registered", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                if(em.equals(value)){
                                                    et_email.setError("Email is already in use.");
                                                 //   Toast.makeText(Register.this, "Email is already in use.", Toast.LENGTH_SHORT).show();
                                                }else {


                                                    int selectedId=radioSexGroup.getCheckedRadioButtonId();
                                                    radioSexButton=(RadioButton)findViewById(selectedId);
                                                    firebaseUserAdapter.adduser(nm,em,ph,pass,dob,"0$",radioSexButton.getText().toString(),rf);
                                                    startActivity(new Intent(Register.this,Login.class));
                                                    finish();
                                                    pd.dismiss();


                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            // Failed to read value
                                            Log.w("TAG", "Failed to read value.", error.toException());
                                        }
                                    });



                                }else {

                                    et_password.setError("Not Matched");
                                    et_confirmpass.setError("Not Matched");

                                }
                            }else {
                                et_email.setError("Email is not Correct");
                                Toast.makeText(this, "Email is not correct", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
            }
        }
    }
}
