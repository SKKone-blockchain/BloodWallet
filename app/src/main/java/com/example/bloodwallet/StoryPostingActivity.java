package com.example.bloodwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StoryPostingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_posting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
