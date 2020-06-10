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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        if(firebaseAuth.getCurrentUser() != null) {
            String email = firebaseAuth.getCurrentUser().getEmail();
            ID.setText(email);
            loadUserID(email);
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

    private void loadUserID(String userEmail) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap> userInfo = (HashMap)dataSnapshot.getValue();
                for (Map.Entry<String, HashMap> entry : userInfo.entrySet()) {
                    HashMap userMap = entry.getValue();
                    if (userMap.get("email") == null) {
                        continue;
                    }
                    String email = userMap.get("email").toString();
                    if (email.equals(userEmail)) {
                        String userID = userMap.get("id").toString();
                        String userName = userMap.get("name").toString();
                        String userBirthDate = userMap.get("birthdate").toString();
                        Toast.makeText(Login.this, userID+"님 환영합니다." , Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Login.this, MainInfo.class);
                        intent.putExtra("userID", userID);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userBirthDate", userBirthDate);
                        getFirebaseDatabase();
                        finish();
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                            loadUserID(email);
                        } else {
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "이메일 또는 비밀번호가 틀렸습니다", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}

