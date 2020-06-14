package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    EditText phoneNumberEditText;
    EditText registerCodeEditText;
    Button authButton;
    String authCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phoneNumberEditText = findViewById(R.id.register_phoneNo);
        registerCodeEditText = findViewById(R.id.register_code);
        authButton = findViewById(R.id.Confcode_register);

        Button backButton = findViewById(R.id.backbutton_register);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button registerButton = findViewById(R.id.Confcode_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userInputCode = registerCodeEditText.getText().toString();
                if (userInputCode.equals("") == false && userInputCode.equals(authCode)) {
                    Intent i = new Intent(Register.this, Register_info.class);
                    i.putExtra("PN", phoneNumberEditText.getText().toString());
                    startActivity(i);
                } else {
                    Toast.makeText(Register.this, "인증번호가 잘못되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button sendAuthCodeButton = findViewById(R.id.Sendcode_register);
        sendAuthCodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                if (phoneNumber.length() <= 0) {
                    Toast.makeText(Register.this, "핸드폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phoneNumber.startsWith("010")) {
                    phoneNumber = "+82" + phoneNumber.substring(1);
                }
                Toast.makeText(getApplicationContext(), "인증번호가 전송되었습니다.", Toast.LENGTH_LONG).show();
                verifyPhoneNumber(phoneNumber);
            }
        });

        authCode = "";
    }

    private void verifyPhoneNumber(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60L /*timeout*/, TimeUnit.SECONDS,
                this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                        // ...
                        Log.d("test", "on verification completed");
                        authCode = phoneAuthCredential.getSmsCode();
                        Log.d("test", authCode);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // ...
                        Log.d("test", "on verification failed");
                        e.printStackTrace();
                    }
                });
    }
}