package com.example.bloodwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StoryListActivity extends AppCompatActivity {

    private StoryListViewAdapter listViewAdapter;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        Intent intent = getIntent();
        userID=intent.getStringExtra("userID");
        ImageButton f = findViewById(R.id.myinfobutton_list);
        f.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  StoryListActivity.this , Myinfo.class );
                i.putExtra("userID",userID);
                startActivity(i);
            }
        });

        listViewAdapter = new StoryListViewAdapter();

        final ListView listView = (ListView) findViewById(R.id.story_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(StoryListActivity.this , StoryActivity.class);
                i.putExtra("userID",userID);
                StoryListItem item = (StoryListItem)listView.getItemAtPosition(position);
                i.putExtra("title", item.title);
                i.putExtra("content", item.content);
                i.putExtra("donatedNum", item.donatedNum);
                i.putExtra("goalNum", item.goalNum);
                startActivity(i);
            }
        });

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("posts");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listViewAdapter.getCount() > 0) {
                    return;
                }

                HashMap<String, HashMap> posts = (HashMap)dataSnapshot.getValue();
                for (Map.Entry<String, HashMap> entry : posts.entrySet()) {
                    HashMap post = entry.getValue();
                    listViewAdapter.addItem(post);
                }

                listView.setAdapter(listViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
