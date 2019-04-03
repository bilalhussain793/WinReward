package com.example.bilalhussain.winreward;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUserAdapter {


    public String adduser(String name,String email,String phone,String password,String date_of_birth,String t_bal,String gender,String ref){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/"+phone);

        myRef.child("Name").setValue(name);
        myRef.child("Email").setValue(email);
        myRef.child("Gender").setValue(gender);
        myRef.child("Phone").setValue(phone);
        myRef.child("Password").setValue(password);
        myRef.child("Date of Birth").setValue(date_of_birth);
        myRef.child("Total Balance").setValue(t_bal);
        myRef.child("Points").setValue(0);
        myRef.child("Refer To").setValue(ref);


        return "Added";

    }
    public String addpoints(String phone,int points){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/"+phone);

        myRef.child("Points").setValue(points);

        return "";
    }










}
