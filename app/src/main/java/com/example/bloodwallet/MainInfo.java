package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininfo);


        Button donationlistbutton=findViewById(R.id.donationlistbutton_maininfo);
        donationlistbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , MyDonationList.class );
                startActivity(i);
            }
        });
        Button donationsreceivedlistbutton=findViewById(R.id.donationsreceivedlistbutton_maininfo);
        donationsreceivedlistbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , MyDonationsReceivedList.class );
                startActivity(i);
            }
        });
        Button scanbutton=findViewById(R.id.scanbutton_maininfo);
        scanbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , Scan.class );
                startActivity(i);
            }
        });
        Button myinfobutton=findViewById(R.id.myinfobutton_maininfo);
        myinfobutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , Myinfo.class );
                startActivity(i);
            }
        });
        Button searchingstorybutton=findViewById(R.id.searchingstorybutton_maininfo);
        searchingstorybutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , Searchingstory.class );
                startActivity(i);
            }
        });
        Button writingstorybutton=findViewById(R.id.writingstorybutton_maininfo);
        writingstorybutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , Writingstory.class );
                startActivity(i);
            }
        });
    }
}
