package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Donation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donation);

        ImageButton backbutton=findViewById(R.id.back_stroy);
        backbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  Donation.this , StoryActivity.class );
                startActivity(i);
            }
        });
        ImageButton myinfobutton=findViewById(R.id.myinfobutton_donation);
        myinfobutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  Donation.this , Myinfo.class );
                startActivity(i);
            }
        });
    }

}
