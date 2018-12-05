package com.example.bahid.firebase_chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DiscussionActivity extends AppCompatActivity {


    Button btnSendMsg;
    EditText etMsg;

    ListView lvDiscussion;
    ArrayList<String> listConversation = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    String PhoneNumber, SelectedTopic, user_msg_key;

    private DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        btnSendMsg = (Button) findViewById(R.id.send_button); // initialize btn
        etMsg = (EditText) findViewById(R.id.editText_msg); // initialize editTxt
        lvDiscussion = (ListView) findViewById(R.id.lvDiscussion); // init listview
        arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listConversation);
                    // init adapter to use listConservation array
        lvDiscussion.setAdapter(arrayAdapter); // integrate the adapter into the listview

        this.PhoneNumber = getIntent().getExtras().get("phone_number").toString();
        this.SelectedTopic = getIntent().getExtras().get("selected_topic").toString();


        setTitle("Topic: " + this.SelectedTopic);


        dbr = FirebaseDatabase.getInstance().getReference().child("Chat Rooms").child(this.SelectedTopic);

        // **************************************************
        // now code the button logic
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                user_msg_key = dbr.push().getKey();
                dbr.updateChildren(map);

                DatabaseReference dbr2 = dbr.child(user_msg_key);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("msg", etMsg.getText().toString());
                map2.put("user", PhoneNumber);
                dbr2.updateChildren(map2);
            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);
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

    public void updateConversation(DataSnapshot dataSnapshot)
    {
        String msg, user, conversation;
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
            msg  = ((DataSnapshot)i.next()).getValue().toString();
            user = ((DataSnapshot)i.next()).getValue().toString();

            conversation = user + ": " + msg;
            arrayAdapter.insert(conversation, arrayAdapter.getCount());
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
