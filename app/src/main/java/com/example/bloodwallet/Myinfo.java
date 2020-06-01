package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;

public class Myinfo extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    EditText keyword1;
    EditText keyword2;
    EditText keyword3;
    TextView sex_myinfo;
    TextView age_myinfo;
    TextView name_myinfo;
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
        keyword1 = findViewById(R.id.myinfo_keyword1);
        keyword2 = findViewById(R.id.myinfo_keyword2);
        keyword3 = findViewById(R.id.myinfo_keyword3);
        userID=intent.getStringExtra("userID");
        databaseReference = firebaseDatabase.getReference();
        getFirebaseDatabase();
        name_myinfo=(TextView)findViewById(R.id.name_myinfo);
        age_myinfo=findViewById(R.id.age_myinfo);
        sex_myinfo=findViewById(R.id.sex_myinfo);

        //
        //
        Button b=findViewById(R.id.myinfo_save);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                keyword_update();
            }
        });
    }
    private void keyword_update(){

        String keyword1str = keyword1.getText().toString();
        String keyword2str =keyword2.getText().toString().trim();
        String keyword3str = keyword3.getText().toString();
        Userkeywords userkeywords = new Userkeywords(keyword1str,keyword2str,keyword3str);  // 유저 이름과 메세지로 chatData 만들기
        databaseReference.child("users").child(userID).child("keywords").setValue(userkeywords);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
    }
    public void getFirebaseDatabase(){
        user_map2.clear();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnaphot : dataSnapshot.getChildren()) {
                    User temp_user2 = postSnaphot.getValue(User.class);
                    Userkeywords temp_user3 = postSnaphot.child("keywords").getValue(Userkeywords.class);
                    String cmp=temp_user2.getID().split("@")[0];
                    if (cmp.equals(userID)) {
                        //user_map2.put(temp_user2.getName(), temp_user2);
                        name=temp_user2.getName();
                        name_myinfo.setText(name);
                        sex=temp_user2.getSex();
                        sex_myinfo.setText(sex);
                        age=temp_user2.getBirthdate();
                        age_myinfo.setText(age);
                        String k1=temp_user3.getKeyword1();
                        keyword1.setText(k1);
                        String k2=temp_user3.getKeyword2();
                        keyword2.setText(k2);
                        String k3=temp_user3.getKeyword3();
                        keyword3.setText(k3);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child("users").addValueEventListener(postListener);
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

}
