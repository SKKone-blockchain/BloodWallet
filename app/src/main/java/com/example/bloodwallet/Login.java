package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button d = findViewById(R.id.registerbutton_login);
        d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  Login.this , Register.class );
                startActivity(i);
            }
        });
            Button LoginIntoMainInfo = findViewById(R.id.loginbutton_login);
            LoginIntoMainInfo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(Login.this, MainInfo.class);
                    startActivity(i);
                }
            });
        }
    }

