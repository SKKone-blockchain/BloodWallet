package com.example.bloodwallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class StoryListActivity extends AppCompatActivity {

    private StoryListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        ImageButton f = findViewById(R.id.myinfobutton_list);
        f.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  StoryListActivity.this , Myinfo.class );
                startActivity(i);
            }
        });

        listViewAdapter = new StoryListViewAdapter();
        for (int i=0; i<15; i++) {
            listViewAdapter.addItem(null, "제목", "본문", "10:22PM", 30);
        }

        ListView listView = (ListView) findViewById(R.id.story_list);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(  StoryListActivity.this , StoryActivity.class );
                i.putExtra("title","홍길동"); /*제목송신*/
                i.putExtra("Button_on",1);
                startActivity(i);
                }
            });
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.story_list_action_bar, null);
        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }
}
