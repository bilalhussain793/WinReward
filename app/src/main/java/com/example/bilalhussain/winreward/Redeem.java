package com.example.bilalhussain.winreward;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Redeem extends AppCompatActivity {


    EditText et;
    Button btn;
    FirebaseUserAdapter firebaseUserAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Dialog dialog;
    LinearLayout l1,l2,l3;
    String pmethod;
    ImageView im;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);


        dialog=new Dialog(Redeem.this);
        txt=findViewById(R.id.txt);
        dialog.setContentView(R.layout.cash_selector);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        l1=dialog.findViewById(R.id.ppl);
        l2=dialog.findViewById(R.id.eps);
        l3=dialog.findViewById(R.id.jzc);
        dialog.setCancelable(false);
        dialog.show();
        et = findViewById(R.id.editr);
        btn = findViewById(R.id.bt);
        im=findViewById(R.id.ssi);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pmethod="Paypal";
                im.setImageResource(R.drawable.paypal);
                dialog.dismiss();
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pmethod="EasyPaisa";
                im.setImageResource(R.drawable.easypaisa);
                dialog.dismiss();
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pmethod="JazzCash";
                im.setImageResource(R.drawable.jazzcash);
                dialog.dismiss();
            }
        });

        SharedPreferences prefs = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String phone = prefs.getString("phone", "");

        DatabaseReference myRef = database.getReference("users/"+phone);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.child("Points").getValue(Integer.class);

                if(value<50000){
                    btn.setEnabled(false);
                    txt.setText("Your points are less then 50000");
                }else{
                    btn.setEnabled(true);
                    txt.setText("");
                }

                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String paynumber =et.getText().toString();
               String method =pmethod;


             //   Toast.makeText(Redeem.this, ""+sp, Toast.LENGTH_SHORT).show();


                SharedPreferences prefs = getSharedPreferences("LOGIN", MODE_PRIVATE);
                String ph = prefs.getString("phone", "");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users/"+ph);

                myRef.child("Redemeption").setValue("yes");
                myRef.child("Paymentmethod").setValue(method);
                myRef.child("paymentsNumber").setValue(paynumber);

            }
        });
    }


}