package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodwallet.ui.User;
import com.example.bloodwallet.ui.Userkeywords;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.*;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class Myinfo extends AppCompatActivity {

    EditText keyword1;
    EditText keyword2;
    EditText keyword3;
    TextView sex_myinfo;
    TextView age_myinfo;
    TextView name_myinfo;
    TextView certificationNumTextView;

    String name;
    String sex;
    String age;

    HashMap<String, User> user_map2 = new HashMap<>();

    String userID;

    public Myinfo() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        keyword1 = findViewById(R.id.myinfo_keyword1);
        keyword2 = findViewById(R.id.myinfo_keyword2);
        keyword3 = findViewById(R.id.myinfo_keyword3);
        name_myinfo = findViewById(R.id.name_myinfo);
        age_myinfo = findViewById(R.id.age_myinfo);
        sex_myinfo = findViewById(R.id.sex_myinfo);
        certificationNumTextView = findViewById(R.id.myinfo_certification_num);

        getKeywordsFromFirebase(userID);
        loadUserInfoFromFirebase();
        loadNumOfCertification();

        Button saveButton = findViewById(R.id.myinfo_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                keywordUpdate();
            }
        });
    }

    private void keywordUpdate() {
        String keyword1str = keyword1.getText().toString().trim();
        String keyword2str = keyword2.getText().toString().trim();
        String keyword3str = keyword3.getText().toString().trim();
        Userkeywords userkeywords = new Userkeywords(userID, keyword1str, keyword2str, keyword3str);  // 유저 이름과 메세지로 chatData 만들기
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Keyword");
        database.child(userID).setValue(userkeywords);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
    }

    private void loadUserInfoFromFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users/" + userID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> userInfo = (HashMap)dataSnapshot.getValue();

                String name = userInfo.get("name").toString();
                name_myinfo.setText(name);

                String sex = userInfo.get("sex").toString();
                sex = (sex.equals("male"))? "남자" : "여자";
                sex_myinfo.setText(sex);

                String birthDate = userInfo.get("birthdate").toString();
                int userYear = Integer.parseInt(birthDate.substring(0, 4));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date(System.currentTimeMillis());
                String today = formatter.format(date);
                int year = Integer.parseInt(today.substring(0, 4));
                age_myinfo.setText((year - userYear + 1) + "세");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getKeywordsFromFirebase(String userID) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Keyword");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap> keywords = (HashMap)dataSnapshot.getValue();
                for (Map.Entry<String, HashMap> entry : keywords.entrySet()) {
                    HashMap keywordMap = entry.getValue();
                    if (keywordMap.get("userID").equals(userID)) {
                        if (keywordMap.get("keyword1") != null) {
                            keyword1.setText(keywordMap.get("keyword1").toString());
                        }
                        if (keywordMap.get("keyword2") != null) {
                            keyword2.setText(keywordMap.get("keyword2").toString());
                        }
                        if (keywordMap.get("keyword3") != null) {
                            keyword3.setText(keywordMap.get("keyword3").toString());
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNumOfCertification() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("certificates");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                HashMap<String, HashMap> certificates = (HashMap)dataSnapshot.getValue();
                for (Map.Entry<String, HashMap> entry : certificates.entrySet()) {
                    HashMap certificateMap = entry.getValue();
                    HashMap<String, String> owner = (HashMap) certificateMap.get("owner");
                    if (owner == null) {
                        continue;
                    }

                    String ownerID = owner.get("owner_id").toString();
                    if (userID.equals(ownerID)) {
                        count++;
                    }
                }

                certificationNumTextView.setText(count + "개");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
