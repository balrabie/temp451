package com.example.bahid.firebase_chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendMessage extends AppCompatActivity {

    Message message;
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("Message");
    EditText etTo;
    EditText etContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        final String from = getIntent().getExtras().get("message_from").toString();


        etTo = (EditText) findViewById(R.id.editTextTo_SendMessage);
        etContent = (EditText) findViewById(R.id.editTextMsgContent_SendMessage);

        findViewById(R.id.buttonSend_SendMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = new Message(
                        from,
                        etTo.getText().toString(),
                        etContent.getText().toString());
                dbr.push().setValue(message);


            }
        }); // end




    } // end on create
}
