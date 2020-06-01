package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodwallet.ui.Currentuserinfo;
import com.example.bloodwallet.ui.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Login extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    EditText ID;
    EditText PW;
    Button loginbutton_login;
    String userID;
    HashMap<String, User> user_map = new HashMap<>();
//    {"park": user_instancee, "sungyoun":user_instance}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbutton_login = findViewById(R.id.loginbutton_login);
        Button d = findViewById(R.id.registerbutton_login);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();

        ID = findViewById(R.id.login_id);
        PW = findViewById(R.id.login_password);

        if(firebaseAuth.getCurrentUser() != null){
            userID = firebaseAuth.getCurrentUser().getEmail().split("@")[0];
                Intent intent=new Intent(this,MainInfo.class);
                intent.putExtra("userID",userID);
            getFirebaseDatabase();
            finish();
            startActivity(intent);
        }

        loginbutton_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userLogin();
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        getFirebaseDatabase();

    }

    public void getFirebaseDatabase(){
        user_map.clear();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnaphot : dataSnapshot.getChildren()) {
                    User temp_user = postSnaphot.getValue(User.class);
                    user_map.put(temp_user.getName(), temp_user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child("users").addValueEventListener(postListener);
    }


    private void userLogin() {
        String email = ID.getText().toString().trim();
        String password = PW.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            userID=ID.getText().toString().trim().split("@")[0];

                            Toast.makeText(Login.this, userID+"님 환영합니다." , Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Login.this, MainInfo.class);
                            i.putExtra("userID",userID);
                            startActivity(i);
                        } else {
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "로그인 실패!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}

