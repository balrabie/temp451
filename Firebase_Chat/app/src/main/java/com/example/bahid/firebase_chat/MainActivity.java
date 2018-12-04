package com.example.bahid.firebase_chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView lvDiscussionTopics;
    ArrayList<String> listOfDiscussion = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    String Username;

    private DatabaseReference dbr = FirebaseDatabase.getInstance()
            .getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvDiscussionTopics = (ListView) findViewById(R.id.lvDiscussionTopics); // initialize
        arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listOfDiscussion); // initialize
        lvDiscussionTopics.setAdapter(arrayAdapter); // arrayAdapter is empty at this stage

        getUsername(); // sets this->username from EditText

        //********************************************************************
        // dbr is the root: we are reading the children of the roots which are basically the topics
        // we use a listview to represent those topics
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator i = dataSnapshot.getChildren().iterator();
                // datasnapshot is an instance containing data from FBase db
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                }
                    arrayAdapter.clear();
                    arrayAdapter.addAll(set);
                    arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //********************************************************************

        //********************************************************************
        lvDiscussionTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DiscussionActivity.class);
                i.putExtra("selected_topic", ((TextView)view).getText().toString());
                i.putExtra("user_name", Username);
                startActivity(i);
            }
        });
        //********************************************************************




    }

    private void getUsername(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // alert box
        final EditText userName = new EditText(this); // input text-line on the alert box

        builder.setView(userName);

        // OK button:
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Username = userName.getText().toString();
            }
        });

        // Cancel button:
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getUsername(); // we force the inputting a username. cant sign in without username
            }
        });

        builder.show();
    }
}
