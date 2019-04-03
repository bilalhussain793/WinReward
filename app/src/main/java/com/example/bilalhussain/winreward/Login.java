package com.example.bilalhussain.winreward;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Button bt_login;
    EditText et_pn,et_ps;
    CheckBox checkBox;
    FirebaseUserAdapter firebaseUserAdapter=new FirebaseUserAdapter();
    TextView reg_btn;
    private AdView adView;
    AdRequest adRequest;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_pn=findViewById(R.id.et_phone_login);
        et_ps=findViewById(R.id.et_pass_login);

        bt_login=findViewById(R.id.bt_login);
        reg_btn=findViewById(R.id.tv_reg);

        checkBox=findViewById(R.id.cbx);

        adView = (AdView) findViewById(R.id.ad_view);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        SharedPreferences prefs = getSharedPreferences("LOGIN", MODE_PRIVATE);
        int r = prefs.getInt("flg", 0);
        if (r == 2) {
            startActivity(new Intent(new Intent(Login.this,DashBoard.class)));
            finish();
        }else{
            Toast.makeText(this, "Login here", Toast.LENGTH_SHORT).show();
        }

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validlogin(et_pn,et_ps);

            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

    }

    public void validlogin(final EditText phone, final EditText password){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/"+phone.getText().toString());
        pd=new ProgressDialog(Login.this);
        pd.setTitle("Loading....");
        pd.show();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String phn = dataSnapshot.child("Phone").getValue(String.class);
                String pass = dataSnapshot.child("Password").getValue(String.class);

                if(phone.getText().toString().equals(phn)){

                    if(password.getText().toString().equals(pass)){

                        if(checkBox.isEnabled()){
                            SharedPreferences.Editor editor = getSharedPreferences("LOGIN", MODE_PRIVATE).edit();
                            editor.putInt("flg", 2);
                            editor.putString("phone",phone.getText().toString());
                            editor.apply();
                            startActivity(new Intent(Login.this,DashBoard.class));
                            pd.dismiss();
                        }else{
                            SharedPreferences.Editor editor = getSharedPreferences("LOGIN", MODE_PRIVATE).edit();
                            editor.putString("phone",phone.getText().toString());
                            editor.apply();

                            startActivity(new Intent(Login.this,DashBoard.class));
                            pd.dismiss();
                        }

                    }else {

                        password.setError("Wrong Password");
                    }

                }else {
                    phone.setError("Invalid Contact");
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
                phone.setError("Invalid User");
            }
        });

    }
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
