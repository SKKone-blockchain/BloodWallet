package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class Myinfo extends AppCompatActivity {
    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);


        ImageButton backbutton=findViewById(R.id.back_listreading);
        backbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  Myinfo.this , MainInfo.class );
                startActivity(i);
            }
        });
    }

}
