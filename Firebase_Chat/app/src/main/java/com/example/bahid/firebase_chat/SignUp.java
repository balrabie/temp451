package com.example.bahid.firebase_chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    EditText etFullName, etPhoneNumber, etVerificationCode;
    Button btnVerification, btnRegister;
    String FullName, PhoneNumber, CodeSent;
    FirebaseAuth mAuth;
    DatabaseReference dbr = FirebaseDatabase.getInstance()
            .getReference().child("Registered");
    Boolean User_Exists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFullName = findViewById(R.id.editTextFullName_SignUp); // initialize
        etPhoneNumber = findViewById(R.id.editTextPhone_SignUp); // initialize
        etVerificationCode = findViewById(R.id.editTextCode_SignUp); // initialize
        mAuth = FirebaseAuth.getInstance(); // initialize
        btnVerification = findViewById(R.id.buttonGetVerificationCode_SignUp);
        btnRegister = findViewById(R.id.buttonRegister_SignUp);
        User_Exists = false;

        // verification code btn
        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullName = etFullName.getText().toString();
                PhoneNumber = etPhoneNumber.getText().toString();
                try {
                    sendVerificationCode();
                }
                catch (Exception ex)
                {
                    Toast.makeText
                            (
                                    getApplicationContext(),
                                    ex.getMessage(),
                                    Toast.LENGTH_LONG
                            )
                            .show();
                }
            }
        });
        // end verification code btn

        // sign in btn
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    VerifySignInCode();
                }
                catch (Exception ex)
                {
                    Toast.makeText
                            (
                                    getApplicationContext(),
                                    ex.getMessage(),
                                    Toast.LENGTH_LONG
                            )
                            .show();
                }
            }
        });
        // end sign in btn

    } // end onCreate()

    private void sendVerificationCode()
    {
        // PhoneNumber = this.etPhoneNumber.getText().toString();
        if (PhoneNumber.isEmpty()){
            etPhoneNumber.setError("Phone number is required");
            etPhoneNumber.requestFocus();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                PhoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    // mCallback init
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            CodeSent = s;
        }
    };
    // mCallback end

    // verify code
    private void VerifySignInCode(){
        String codeEntered = etVerificationCode.getText().toString();
        if (codeEntered.isEmpty()){
            etVerificationCode.setError("Code is required for verification");
            etVerificationCode.requestFocus();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent, codeEntered);
        signUpWithPhoneAuthCredential(credential);
    }
    // verify end

    // sign up process
    private void signUpWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // *********************************************************
                            dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterator i = dataSnapshot.getChildren().iterator();
                                    // datasnapshot is an instance containing data from FBase db
                                    User_Exists = false; // initialize
                                    while (i.hasNext()) {
                                        String data = ((DataSnapshot) i.next()).getKey();
                                        if (data.equals(PhoneNumber))
                                        {
                                            User_Exists = true;
                                            break;
                                        }
                                    } // end while
                                    if (User_Exists)
                                    {
                                        Toast.makeText
                                                (
                                                        getApplicationContext(),
                                                        "User is already registered",
                                                        Toast.LENGTH_LONG
                                                )
                                                .show();
                                    }
                                    else
                                    {
                                        User user = new User(PhoneNumber, FullName);
                                        dbr.child(user.PhoneNumber).setValue(user);

                                        Toast.makeText
                                                (
                                                        getApplicationContext(),
                                                        "Success",
                                                        Toast.LENGTH_LONG
                                                )
                                                .show();

                                    }
                                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                    startActivity(intent);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            // *************************************




                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText
                                        (
                                                getApplicationContext(),
                                                "Incorrect Verification Code",
                                                Toast.LENGTH_LONG
                                        )
                                        .show();
                            }

                        }
                    } // oncomplete end
                });
                // add on complete listener end
    }
    //sign up end

}
