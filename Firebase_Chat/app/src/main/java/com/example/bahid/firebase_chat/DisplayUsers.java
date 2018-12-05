package com.example.bahid.firebase_chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DisplayUsers extends AppCompatActivity {

    ListView lvUsers;
    ArrayList<String> listOfUsers = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("Registered");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);

        lvUsers = (ListView) findViewById(R.id.listViewUsers_display); // initialize
        arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1); // initialize
        lvUsers.setAdapter(arrayAdapter); // arrayAdapter is empty at this stage

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GetUsers(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GetUsers(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GetUsers(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void GetUsers(DataSnapshot dataSnapshot)
    {
        ArrayList<String> set = new ArrayList<>();
        String phone, name;

        Iterator i = dataSnapshot.getChildren().iterator();
        // datasnapshot is an instance containing data from FBase db
        while (i.hasNext()) {
            phone = (((DataSnapshot) i.next()).getValue().toString());
            name = (((DataSnapshot) i.next()).getValue().toString());
            set.add(name + " : " + phone);
            System.out.println("*****" + name + " : " + phone);

            arrayAdapter.insert(name + " : " + phone, arrayAdapter.getCount());
            arrayAdapter.notifyDataSetChanged();
        }
        //System.out.print("ithas = "); System.out.println(set.size());
        //arrayAdapter.clear();
        //arrayAdapter.addAll(set);
        //arrayAdapter.notifyDataSetChanged();
    }
}
