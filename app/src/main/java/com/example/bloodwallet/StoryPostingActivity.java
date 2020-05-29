package com.example.bloodwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StoryPostingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_posting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText postingTitleEditTextView = findViewById(R.id.story_posting_title);
        final String titleString = postingTitleEditTextView.getText().toString();

        EditText postingContentEditTextView = findViewById(R.id.story_posting_content);
        final String contentString = postingContentEditTextView.getText().toString();

        EditText postingGoalEditTextView = findViewById(R.id.story_posting_goal);
        final String goalString = postingGoalEditTextView.getText().toString();

        Button postingSubmitButton = findViewById(R.id.story_posting_submit);
        postingSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleString.length() <= 0) {
                    Toast.makeText(StoryPostingActivity.this, "제목을 입력해주세요", Toast.LENGTH_SHORT);
                    return;
                } else if (contentString.length() <= 0) {
                    Toast.makeText(StoryPostingActivity.this, "본문을 입력해주세요", Toast.LENGTH_SHORT);
                    return;
                } else if (goalString.length() <= 0) {
                    Toast.makeText(StoryPostingActivity.this, "목표 갯수를 입력해주세요", Toast.LENGTH_SHORT);
                    return;
                }

                int goal = Integer.parseInt(goalString);
                if (goal <= 0) {
                    Toast.makeText(StoryPostingActivity.this, "목표 갯수를 양수로 설정해주세요", Toast.LENGTH_SHORT);
                    return;
                }
                
                // TODO : firebase 연동해서 글 작성
            }
        });
    }
}
