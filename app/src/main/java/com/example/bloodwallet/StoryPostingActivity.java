package com.example.bloodwallet;

import androidx.appcompat.app.AppCompatActivity;

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

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_posting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_title = findViewById(R.id.story_posting_title);
        et_story = findViewById(R.id.story_posting_content);
        et_targetNum = findViewById(R.id.target_num);
        btn = findViewById(R.id.story_posting_submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
                Date date = new Date(now);
                String timestamp = sdfNow.format(date);
                timestamp = timestamp.replaceAll("/", "");
                timestamp = timestamp.replaceAll(" ", "");
                timestamp = timestamp.replaceAll(":", "");

                title = et_title.getText().toString();
                story = et_story.getText().toString();
                writer = "김진범";
                postID = writer + timestamp;
                //donatedNum = 0;
                donatedNum = 1;
                targetNum = Integer.parseInt(et_targetNum.getText().toString());
                summary = "dddd";

                story = "제 동생이 얼마전에 백혈병을 진단받아 골수이식을 진행하였습니다. 골수이식 직후, 혈액수치가 급격히 떨여져 긴급수혈을 93차례 받았습니다. 그러다보니 수혈 비용이 만만치 않게 나와 여러분께 도움을 청합니다.";
                try {
                    getNLP();
                } catch (Exception e) {
                    Log.d("nlp", " not working");
                }




                postFirebase();
                onBackPressed();
            }
        });

    }

    // firebase에 업로드
    public void postFirebase(){
        Post post = new Post(postID, title, story, writer, donatedNum, targetNum, summary);
        myRef.child("Post").child(postID).setValue(post);
    }

    //nlp로 summary 추출
    public void getNLP() throws Exception{
        String[] sentences = story.split(".");
        Double worst = 10.0;
        Log.d("getNLP ", sentences[0]);
        try(LanguageServiceClient language = LanguageServiceClient.create()){
            for(int i=0; i< sentences.length; i++){
                String text = sentences[i];
                Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();

                // Detects the sentiment of the text
                Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

                float score = sentiment.getScore();
                Log.d(text, " : " + String.valueOf(score));

                if(worst > score){
                    summary = text;
                }
            }
        }
        catch (Exception e){
            Log.d("LaunguageSErviceClient ", "not workding");
        }
    }



}
