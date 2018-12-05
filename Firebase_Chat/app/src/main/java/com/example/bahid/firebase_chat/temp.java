package com.example.bahid.firebase_chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class temp extends AppCompatActivity {

    private DatabaseReference dbr = FirebaseDatabase.getInstance()
            .getReference().child("Registered");
    User one;
    User two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        one = new User();
        one.Name = "bahi";
        one.PhoneNumber = "03900900";
        two = new User();
        two.Name = "ihab";
        two.PhoneNumber = "03700800";

        //Register(one);
        //Register(two);
        //UserExists(one);


    }


    public void Register(User u)
    {
        dbr.child(u.PhoneNumber).setValue(u);

    }


}
