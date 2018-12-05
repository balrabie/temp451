package com.example.bahid.firebase_chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class SignIn extends AppCompatActivity {

    Button btnSignIn;
    Button btnSignUp;
    EditText etPhoneNumber;
    String PhoneNumber;
    boolean User_Exists;
    DatabaseReference dbr = FirebaseDatabase.getInstance()
            .getReference().child("Registered");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        InitializeViews();

        try {
            SetupSignUpButton();
            SetupSignInButton();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }


    private void InitializeViews()
    {
        btnSignIn = findViewById(R.id.buttonSignIn_Login);
        btnSignUp = findViewById(R.id.buttonSignUp_Login);
        etPhoneNumber = findViewById(R.id.editTextPhone_Login);
        User_Exists = false;
    }

    private void SetupSignInButton()
    {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check database if the phone number is found in the database
                PhoneNumber = etPhoneNumber.getText().toString();

                // *********
                dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator i = dataSnapshot.getChildren().iterator();

                        User_Exists = false;
                        while (i.hasNext()) {
                            String data = ((DataSnapshot) i.next()).getKey();
                            System.out.println(PhoneNumber + "comparing to " + data + "of size " + data.length());
                            if (data.equals(PhoneNumber))
                            {
                                User_Exists = true;
                                break;

                            }
                        } // end while
                        if (User_Exists)
                        {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("phone_number_SignIn", PhoneNumber);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText
                                    (
                                            getApplicationContext(),
                                            "Phone number is not registered. Sign up first",
                                            Toast.LENGTH_LONG
                                    )
                                    .show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                // *********



            }// on click end
        }); // set on click listener end

    } // end

    private void SetupSignUpButton()
    {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
            }
        });

    } //end

    private void CheckIfUserExists(@NonNull final KeyEvent.Callback callback)
    {
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                // datasnapshot is an instance containing data from FBase db
                System.out.println("HELLO********* " + PhoneNumber + " " + PhoneNumber.length());

                while (i.hasNext()) {
                    String data = ((DataSnapshot) i.next()).getKey();
                    System.out.println(PhoneNumber + "comparing to " + data + "of size " + data.length());
                    if (data.equals(PhoneNumber))
                    {
                        System.out.println("gotcha");
                        User_Exists = true;

                        return;
                    }
                } // end while
                User_Exists = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
