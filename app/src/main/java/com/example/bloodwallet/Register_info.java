package com.example.bloodwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Register_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);

        Button f = findViewById(R.id.Register_registerinfo);
        f.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(  Register_info.this , Login.class );
                startActivity(i);
            }
        });


        Button g = findViewById(R.id.backbutton_registerinfo);
        g.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  Register_info.this , MainActivity.class );
                startActivity(i);
            }
        });
    }
}
