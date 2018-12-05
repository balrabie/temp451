package com.example.bahid.firebase_chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Inbox extends AppCompatActivity {

    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("Message");
    ArrayAdapter arrayAdapter;
    ListView lvSenders;
    TextView tvMessage;
    String current_To;
    Map<String, String> FromContent_Map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        FromContent_Map = new HashMap<>();
        tvMessage = (TextView) findViewById(R.id.textView_inbox);
        lvSenders = (ListView) findViewById(R.id.lv_inbox);
        arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1);
        lvSenders.setAdapter(arrayAdapter);

        current_To = getIntent().getExtras().get("to_inbox").toString();





        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UpdateInbox(dataSnapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UpdateInbox(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void UpdateInbox(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        String to,from,content;

        while (i.hasNext())
        {
            content = (((DataSnapshot) i.next()).getValue().toString());
            from = (((DataSnapshot) i.next()).getValue().toString());
            to = (((DataSnapshot) i.next()).getValue().toString());

            if (!to.equals(current_To))
                continue;

            // make a map between "From" and "Content" meaning senders and messages
            if (!FromContent_Map.containsKey(from))
            {
                FromContent_Map.put(from, content);
            }
            else
            {
                String old = FromContent_Map.get(from);
                String combined_message = old + "\n" + content;
                FromContent_Map.put(from, combined_message);
            }

            arrayAdapter.insert(from , arrayAdapter.getCount());
            arrayAdapter.notifyDataSetChanged();
        }// end of while

        lvSenders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chosen_sender = ((TextView)view).getText().toString();
                String msg = FromContent_Map.get(chosen_sender);
                tvMessage.setText(msg);
            }
        });
    }
}
