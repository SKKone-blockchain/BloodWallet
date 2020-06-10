package com.example.bloodwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryActivity extends AppCompatActivity {
    ListView listView;
    List<String> commentList;
    myadapter adapter;

    String userID;
    String postID;
    String writer;

    TextView patientIDTextView;
    TextView patientInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commentList = new ArrayList<>();

        Intent intent2 = getIntent();
        userID = intent2.getStringExtra("userID");
        postID = intent2.getStringExtra("postID");
        writer = intent2.getStringExtra("writer");
        loadDataFromFirebase();

        ImageButton myInfoButton = findViewById(R.id.myinfobutton_list);
        myInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryActivity.this , Myinfo.class );
                i.putExtra("userID",userID);
                startActivity(i);
            }
        });

        listView = (ListView)findViewById(R.id.messages);
        adapter = new myadapter();

        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        TextView titleTextView = findViewById(R.id.story_title);
        titleTextView.setText(title);

        String content = intent.getStringExtra("content");
        TextView contentTextView = findViewById(R.id.story_content);
        contentTextView.setText(content);

        int donatedNum = intent.getIntExtra("donatedNum", 0);
        int goalNum = intent.getIntExtra("goalNum", 0);
        TextView progressNumTextView = findViewById(R.id.story_progress_number);
        int percent = (int)((double)donatedNum / goalNum * 100);
        progressNumTextView.setText(donatedNum + "/" + goalNum + " (" + percent + "%)");

        ProgressBar progressBar = findViewById(R.id.story_progress_bar);
        progressBar.setProgress(percent);

        Button donateButton = findViewById(R.id.story_donate);
        donateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  StoryActivity.this , Donation.class );
                i.putExtra("userID",userID);
                i.putExtra("postID", postID);
                i.putExtra("writer", writer);
                startActivity(i);
            }
        });

        patientIDTextView = findViewById(R.id.story_patient_id);
        patientInfoTextView = findViewById(R.id.story_patient_info);
    }

    private void loadDataFromFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("posts/" + postID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap> posts = (HashMap)dataSnapshot.getValue();
                HashMap<String, HashMap> comments = posts.get("comments");
                commentList.clear();
                if (comments != null && comments.isEmpty() == false) {
                    for (Map.Entry<String, HashMap> entry : comments.entrySet()) {
                        String comment = entry.getValue().get("content").toString();
                        commentList.add(comment);
                    }
                }
                listView.setAdapter(adapter);

                HashMap<String, String> user = (HashMap)dataSnapshot.getValue();
                String patientID = user.get("user_id");
                loadPatientInformation(patientID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPatientInformation(String patientID) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users/" + patientID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> users = (HashMap)dataSnapshot.getValue();
                String sex = users.get("sex").toString();
                sex = (sex.equals("male"))? "남자" : "여자";

                String birthDate = users.get("birthdate").toString();
                int patientYear = Integer.parseInt(birthDate.substring(0, 4));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date(System.currentTimeMillis());
                String today = formatter.format(date);
                int year = Integer.parseInt(today.substring(0, 4));
                String info = ((year - patientYear + 1) + "세, ") + sex;

                patientInfoTextView.setText(info);
                patientIDTextView.setText(patientID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    class myadapter extends BaseAdapter {
        @Override
        public int getCount() { return commentList.size(); }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            messages view = new messages(getApplicationContext());
            view.setMessages(commentList.get(position));

            return view;
        }
    }
}
