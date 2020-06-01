package com.example.bloodwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bloodwallet.ui.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_info extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText ID;
    EditText PW;
    EditText name;
    EditText birthdate;
    EditText sex;
    String PN;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    Button Register_registerinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.register_name);
        ID = findViewById(R.id.register_id);
        PW = findViewById(R.id.register_password);
        birthdate = findViewById(R.id.register_birthdate);
        sex = findViewById(R.id.register_sex);

        Intent intent = getIntent();
        PN=intent.getStringExtra("PN");


        Button f = findViewById(R.id.Register_registerinfo);
        f.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });


        Button g = findViewById(R.id.backbutton_registerinfo);
        g.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(  Register_info.this , MainActivity.class );
                startActivity(i);
            }
        });


    }
    private void register_info(){

        String namestr = name.getText().toString();
        String IDstr =ID.getText().toString().trim();
        String PWstr = PW.getText().toString();
        String birthdatestr=birthdate.getText().toString();
        String sexstr=sex.getText().toString();
        String[] IDdata = IDstr.split("@");
        if(TextUtils.isEmpty(IDstr)){
            Toast.makeText(getApplicationContext(), "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        User userinfo = new User(IDstr,namestr ,PWstr,birthdatestr,sexstr,PN);  // 유저 이름과 메세지로 chatData 만들기
        databaseReference.child("users").child(IDdata[0]).setValue(userinfo);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
    }

    private void register(){
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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Register_info", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            register_info();
                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Register_info.this, Login.class);
                            startActivity(i);
                        } else {
                            Log.w("Register_info", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register_info.this, "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
