package com.example.bloodwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.klaytn.caver.crypto.KlayCredentials;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.io.DataOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

public class Register_info extends AppCompatActivity {

    EditText ID;
    EditText PW;
    EditText name;
    EditText Email;
    EditText birthdate;
    String sex;
    String PN;

    private FirebaseAuth mAuth;
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
        Email = findViewById(R.id.register_email);
        PW = findViewById(R.id.register_password);
        birthdate = findViewById(R.id.register_birthdate);

        sex = "";
        Button maleButton = findViewById(R.id.register_male);
        Button femaleButton = findViewById(R.id.register_female);
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "male";
                maleButton.setBackgroundColor(getColor(R.color.colorPrimary));
                femaleButton.setBackgroundColor(getColor(R.color.colorInactive));
            }
        });
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "female";
                maleButton.setBackgroundColor(getColor(R.color.colorInactive));
                femaleButton.setBackgroundColor(getColor(R.color.colorPrimary));
            }
        });

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

        setupBouncyCastle();
    }
    private void register_info() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

        String namestr = name.getText().toString();
        String IDstr = ID.getText().toString().trim();
        String Emailstr = Email.getText().toString().trim();
        String PWstr = PW.getText().toString();
        String birthdatestr = birthdate.getText().toString();
        String[] EmailData = Emailstr.split("@");
        if (TextUtils.isEmpty(IDstr)) {
            Toast.makeText(getApplicationContext(), "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: private key, public key 생성 하기
        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
        String privateKey = Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey());
        String address = credentials.getAddress();

        Log.d("private key: ", privateKey);
        Log.d("address: ", address);

        // TODO: public key는 firebase에 올리기

        User userinfo = new User(Emailstr, IDstr, namestr, PWstr, birthdatestr, sex, PN, address);  // 유저 이름과 메세지로 chatData 만들기
        Map<String, Object> new_user = new HashMap<>();
        new_user.put(IDstr, userinfo);

        DatabaseReference newUserRef = firebaseDatabase.getReference("users");
        newUserRef.updateChildren(new_user); // child(IDdata[0]).setValue(userinfo);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기

        // TODO: private key는 sharedPreference에 저장하기
        SharedPreferences sharedpreferences = getSharedPreferences("KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("PRIVATE_KEY", privateKey);
        editor.putString("PUBLIC_KEY", address);
        editor.commit();

    }

    private void register(){
        String email = Email.getText().toString().trim();
        String password = PW.getText().toString().trim();
        if (name.getText().length() <= 0) {
            Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (ID.getText().length() <= 0) {
            Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.length() <= 0) {
            Toast.makeText(getApplicationContext(), "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "6자리 이상의 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (birthdate.getText().length() != 6) {
            Toast.makeText(this, "생일을 yyMMdd 형태로 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (sex.length() <= 0) {
            Toast.makeText(this, "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Register_info", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            try {
                                register_info();
                            } catch (InvalidAlgorithmParameterException e) {
                                System.out.println("Key generation error: " + e);
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                System.out.println("Key generation error: " + e);
                                e.printStackTrace();
                            } catch (NoSuchProviderException e) {
                                System.out.println("Key generation error: " + e);
                                e.printStackTrace();
                            }
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
    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
}
