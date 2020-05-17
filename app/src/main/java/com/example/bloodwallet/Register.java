package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button e = findViewById(R.id.backbutton_register);
        e.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  Register.this , MainActivity.class );
                startActivity(i);
            }
        });
    }
}
