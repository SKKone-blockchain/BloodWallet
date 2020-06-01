package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    EditText PN;
    EditText code;
    Button Confcode_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        PN = findViewById(R.id.register_phoneNo);
        code = findViewById(R.id.register_code);
        Confcode_register = findViewById(R.id.Confcode_register);
        Button e = findViewById(R.id.backbutton_register);
        e.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  Register.this , MainActivity.class );
                startActivity(i);
            }
        });

        Button f = findViewById(R.id.Confcode_register);
        f.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  Register.this , Register_info.class );
                i.putExtra("PN",PN.getText().toString());
                startActivity(i);
            }
        });
        Button h = findViewById(R.id.Sendcode_register);
        h.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "인증번호가 전송되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
