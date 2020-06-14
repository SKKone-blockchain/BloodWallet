package com.example.bloodwallet;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.bloodwallet.task.onGetDataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StoryListActivity extends AppCompatActivity {

    final String[] keywords = new String[3];

    private StoryListViewAdapter listViewAdapter;
    private ListView listView;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        Intent intent = getIntent();

        userID = intent.getStringExtra("userID");
        ImageButton f = findViewById(R.id.myinfobutton_list);
        f.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryListActivity.this, Myinfo.class);
                i.putExtra("userID", userID);
                startActivity(i);
            }
        });

        listViewAdapter = new StoryListViewAdapter();

        listView = (ListView) findViewById(R.id.story_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(StoryListActivity.this, StoryActivity.class);
                i.putExtra("userID", userID);
                StoryListItem item = (StoryListItem) listView.getItemAtPosition(position);
                i.putExtra("postID", item.postID);
                i.putExtra("writer", item.writer);
                i.putExtra("title", item.title);
                i.putExtra("content", item.content);
                i.putExtra("donatedNum", item.donatedNum);
                i.putExtra("goalNum", item.goalNum);
                startActivity(i);
            }
        });


        DatabaseReference kwref = FirebaseDatabase.getInstance().getReference("Keyword/" + userID);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("posts");

        // TODO: keyword intereface call
        getKeyword(kwref, userID, new onGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                getPost(database, new onGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        //
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
        // Todo: post interface


    }

    public void getKeyword(DatabaseReference keywordRef, String userID, final onGetDataListener listener){
        listener.onStart();
        keywordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                try {
                    keywords[0] = dataSnapshot.child("keyword1").getValue(String.class);
                    keywords[1] = dataSnapshot.child("keyword2").getValue(String.class);
                    keywords[2] = dataSnapshot.child("keyword3").getValue(String.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void getPost(DatabaseReference postRef, final onGetDataListener listener) {

        listener.onStart();
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listViewAdapter.getCount() > 0) {
                    return;
                }

                HashMap<String, HashMap> posts = (HashMap)dataSnapshot.getValue();
                for (Map.Entry<String, HashMap> entry : posts.entrySet()) {
                    HashMap post = entry.getValue();
                    Double score = 0.0;
                    Integer donatedNum = Integer.parseInt(post.get("donated_num").toString());
                    Integer goalNum = Integer.parseInt(post.get("target_num").toString());
                    String story = post.get("story").toString();

                    for(int i=0; i< 3; i++) {
                        if(keywords[i] != null && story.contains(keywords[i])){
                            score += 25.0;
                        }
                    }

                    score += 25.0 *(1 - (double)donatedNum / goalNum);

                    listViewAdapter.addItem(post, score);
                }
                listViewAdapter.sortList();
                listView.setAdapter(listViewAdapter);
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
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