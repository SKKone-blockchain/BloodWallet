package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MainInfo extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    String userID;
    String userName;
    String userBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        userID=intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        userBirthDate = intent.getStringExtra("userBirthDate");

        Button donationlistbutton=findViewById(R.id.donationlistbutton_maininfo);
        donationlistbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , MyDonationList.class );
                i.putExtra("userID",userID);
                i.putExtra("userName", userName);
                i.putExtra("userBirthDate", userBirthDate);
                startActivity(i);
            }
        });
        Button donationsreceivedlistbutton=findViewById(R.id.donationsreceivedlistbutton_maininfo);
        donationsreceivedlistbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , MyDonationsReceivedList.class );
                i.putExtra("userID",userID);
                i.putExtra("userName", userName);
                i.putExtra("userBirthDate", userBirthDate);
                startActivity(i);
            }
        });
        Button scanbutton=findViewById(R.id.scanbutton_maininfo);
        scanbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , BloodCertificationCameraActivity.class );
                i.putExtra("userID",userID);
                i.putExtra("userName", userName);
                i.putExtra("userBirthDate", userBirthDate);
                startActivity(i);
            }
        });
        Button myinfobutton=findViewById(R.id.myinfobutton_maininfo);
        myinfobutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , Myinfo.class );
                i.putExtra("userID",userID);
                i.putExtra("userName", userName);
                i.putExtra("userBirthDate", userBirthDate);
                startActivity(i);
            }
        });
        Button searchingstorybutton=findViewById(R.id.searchingstorybutton_maininfo);
        searchingstorybutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , StoryListActivity.class );
                i.putExtra("userID",userID);
                i.putExtra("userName", userName);
                i.putExtra("userBirthDate", userBirthDate);
                startActivity(i);
            }
        });
        Button writingstorybutton=findViewById(R.id.writingstorybutton_maininfo);
        writingstorybutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MainInfo.this , StoryPostingActivity.class );
                i.putExtra("userID",userID);
                i.putExtra("userName", userName);
                i.putExtra("userBirthDate", userBirthDate);
                startActivity(i);
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
