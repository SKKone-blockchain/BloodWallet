package com.example.bloodwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//for firebase
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// for nlp
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import java.io.IOException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StoryPostingActivity extends AppCompatActivity {
    private EditText et_title, et_story, et_targetNum;
    private Button btn;

    //for firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    String title;
    String story;
    String writer;
    String postID;
    Integer donatedNum;
    Integer targetNum;
    String summary;
    String tarNumString;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_posting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        userID=intent.getStringExtra("userID");
        userID = "nlpTest";

        et_title = findViewById(R.id.story_posting_title);
        et_story = findViewById(R.id.story_posting_content);
        et_targetNum = findViewById(R.id.target_num);
        btn = findViewById(R.id.story_posting_submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                Date date = new Date();
                String timestamp = formatter.format(date);


                title = et_title.getText().toString();
                story = et_story.getText().toString();
                writer = userID;
                postID = writer + timestamp;
                donatedNum = 0;
                tarNumString = et_targetNum.getText().toString();
                targetNum = Integer.parseInt(tarNumString);


                if (title.length() <= 0) {
                    Toast.makeText(StoryPostingActivity.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (story.length() <= 0) {
                    Toast.makeText(StoryPostingActivity.this, "본문을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tarNumString.length() <= 0) {
                    Toast.makeText(StoryPostingActivity.this, "목표 갯수를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    String[] sentences = story.split("\\.");
                    float worst_score = 10;
                    String worst_sentence = sentences[0];
                    for(int i=0; i< sentences.length; i++){
                        try(LanguageServiceClient language = LanguageServiceClient.create()){
                            Document doc = Document.newBuilder().setContent(sentences[i]).setType(Type.PLAIN_TEXT).build();

                            // Detects the sentiment of the text
                            Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

                            float score = sentiment.getScore();

                            if(worst_score > score){
                                worst_sentence = sentences[i];
                                worst_score = score;
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace(); //오류 출력(방법은 여러가지)
                            try {
                                throw e;
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    summary = worst_sentence;


                    postFirebase();
                    onBackPressed();
                }
            }
        });

    }

    // firebase에 업로드
    public void postFirebase(){
        Post post = new Post(postID, title, story, writer, donatedNum, targetNum, summary);
        myRef.child("posts").child(postID).setValue(post);
    }




}
