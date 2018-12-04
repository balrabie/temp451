package com.example.bahid.firebase_chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import java.util.concurrent.TimeUnit;

public class SignInUp extends AppCompatActivity {

    EditText etPhoneNumber, etVerificationCode;
    String PhoneNumber, CodeSent;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        etPhoneNumber = findViewById(R.id.editTextPhone); // initialize
        etVerificationCode = findViewById(R.id.editTextCode); // initialize
        mAuth = FirebaseAuth.getInstance(); // initialize

        // verification code btn
        findViewById(R.id.buttonGetVerificationCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
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
        signInWithPhoneAuthCredential(credential);
    }
    // verify end

    // sign in
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText
                                    (
                                            getApplicationContext(),
                                            "Success",
                                            Toast.LENGTH_LONG
                                    )
                                    .show();

                            // *************** GO to next activity: maybe change this later
                            //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            //i.putExtra("phone_number", PhoneNumber);
                            //startActivity(i);
                            // ********************************************************************


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
    //sign in end
}
