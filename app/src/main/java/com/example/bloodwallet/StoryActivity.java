package com.example.bloodwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StoryActivity extends AppCompatActivity {
    ListView listView;
    String[] messagestr={"힘내세요!","힘내요!","정말 안타까운 이야기네요 ㅜㅜ","비록 적은 수의 헌혈증이지만 도움이 됐으면 좋겠습니다."};
    myadapter adapter;
    String userID;
    private StoryListViewAdapter listViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Intent intent2 = getIntent();
        userID=intent2.getStringExtra("userID");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        listView.setAdapter(adapter);

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
                startActivity(i);
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
        public int getCount() { return messagestr.length; }

        @Override
        public Object getItem(int position) {
            return messagestr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            messages view= new messages(getApplicationContext());
            view.setMessages(messagestr[position]);

            return view;
        }
    }
}
