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

    ListView lvDiscussionTopics, lvFeatures;
    ArrayList<String> listOfDiscussion = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    String PhoneNumber;

    private DatabaseReference dbr = FirebaseDatabase.getInstance()
            .getReference().child("Chat Rooms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvDiscussionTopics = (ListView) findViewById(R.id.lvDiscussionTopics); // initialize
        lvFeatures = (ListView) findViewById(R.id.lvFeatures); // initialize
        FillFeaturesLV();
        arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listOfDiscussion); // initialize
        lvDiscussionTopics.setAdapter(arrayAdapter); // arrayAdapter is empty at this stage

        PhoneNumber = getIntent().getExtras().get("phone_number_SignIn").toString();

        //********************************************************************
        // we are reading the children of node "Chat Rooms"
        // we use a listview to represent those rooms
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
                i.putExtra("phone_number", PhoneNumber);
                startActivity(i);
            }
        });
        //********************************************************************
        lvFeatures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_feature = ((TextView) view).getText().toString();
                if (selected_feature.equals("Compose Message"))
                {
                   // go to send activity
                    Intent i = new Intent(getApplicationContext(), SendMessage.class);
                    i.putExtra("message_from", PhoneNumber);
                    startActivity(i);
                }
                else if (selected_feature.equals("Inbox"))
                {
                    Intent i = new Intent(getApplicationContext(), Inbox.class);
                    i.putExtra("to_inbox", PhoneNumber);
                    startActivity(i);
                }



            }
        });


    }

    private void FillFeaturesLV() {
        ArrayAdapter adap = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        ArrayList<String> features = new ArrayList<>();
        features.add("Compose Message");
        features.add("Inbox");
        lvFeatures.setAdapter(adap);
        adap.addAll(features);
        adap.notifyDataSetChanged();
    }


}
