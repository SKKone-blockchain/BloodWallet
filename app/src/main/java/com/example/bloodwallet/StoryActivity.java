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
import android.widget.ListView;

import java.util.ArrayList;

public class StoryActivity extends AppCompatActivity {
    ListView listView;
    String[] messagestr={"힘내세요!","힘내요!","정말 안타까운 이야기네요 ㅜㅜ","비록 적은 수의 헌혈증이지만 도움이 됐으면 좋겠습니다."};
    myadapter adapter;

    private StoryListViewAdapter listViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView =(ListView) findViewById(R.id.messages);
        adapter = new myadapter();
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("title");
        /* android.app.ActionBar actionBar = getActionBar();
        actionBar.setTitle(name); 왜 오류가 나는지 모르겠음*/
        Integer Button_on = intent.getExtras().getInt("Button_on");
        if (Button_on==0){
            Button B=findViewById(R.id.story_donate);
            B.setVisibility(View.GONE);
            Button B2=findViewById(R.id.story_donate2);
            B2.setVisibility(View.GONE);
        }

        Button f = findViewById(R.id.story_donate);
        f.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  StoryActivity.this , Donation.class );
                startActivity(i);
            }
        });
        Button g = findViewById(R.id.story_donate2);
        g.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  StoryActivity.this , Donation.class );
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
