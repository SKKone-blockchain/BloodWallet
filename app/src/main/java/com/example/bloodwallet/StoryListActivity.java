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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Integer.min;

public class StoryListActivity extends AppCompatActivity {

    private StoryListViewAdapter listViewAdapter;
    ListView listView;

    private ArrayList<String> keyword = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<ScoredPostList> postLists = new ArrayList<>();
    private DatabaseReference myPostRef;
    private DatabaseReference myKeywordRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        myPostRef = FirebaseDatabase.getInstance().getReference();
        myKeywordRef = FirebaseDatabase.getInstance().getReference();

        String userID = "김진범";
        KwTask kwt = new KwTask();
        kwt.execute(userID);

        PostTask pt = new PostTask();
        pt.execute();

        get_postList();
        Collections.sort(postLists);
        System.out.println("postLists size after getFirebase "+postLists.size());

        ImageButton f = findViewById(R.id.myinfobutton_list);
        f.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  StoryListActivity.this , Myinfo.class );
                startActivity(i);
            }
        });

        listViewAdapter = new StoryListViewAdapter();

        for (int i=0; i<min(15, postLists.size()); i++) {
            listViewAdapter.addItem(null, postLists.get(i).post.title, postLists.get(i).post.summary, "10:22PM", postLists.get(i).percent);
        }

        listView = (ListView) findViewById(R.id.story_list);
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



    public void get_postList(){
        postLists.clear();
        for(int i=0; i<posts.size(); i++){
            Double score = 0.0;
            Double percent = 0.0;
            Post p = posts.get(i);

            for (int j=0; j< 3; j++){
                if (p.story.contains(keyword.get(j))){
                    score += 25.0;
                }
            }
            percent = (double)p.donatedNum / (double)p.targetNum;
            score += 25.0 * percent;

            ScoredPostList sp = new ScoredPostList(p, score, percent);
            postLists.add(sp);
        }
    }






    private class KwTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            final String userID = strings[0];
            final ValueEventListener keywordListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    keyword.clear();
                    for(DataSnapshot keywordSnapshot : dataSnapshot.getChildren()){
                        String Key = keywordSnapshot.getKey();
                        Keyword get = keywordSnapshot.getValue(Keyword.class);
                        if(userID.equals(get.userID)){
                            keyword.add(get.kw1);
                            keyword.add(get.kw2);
                            keyword.add(get.kw3);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            myKeywordRef.child("Keyword").addValueEventListener(keywordListener);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
        }

    }

    private class PostTask extends AsyncTask<Void, Integer, Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            posts.clear();

            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        String key = postSnapshot.getKey();
                        Post get = postSnapshot.getValue(Post.class);

                        int tN = 0, dn = 0;
                        tN = get.targetNum;
                        dn = get.donatedNum;

                        if(tN != dn){
                            posts.add(get);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            myPostRef.child("Post").addValueEventListener(postListener);
            return true;
        }
    }




}

