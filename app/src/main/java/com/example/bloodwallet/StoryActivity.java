package com.example.bloodwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // TODO : 게시글 제목으로 ActionBar title 설정해주기


    }
}
