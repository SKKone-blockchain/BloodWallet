package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Donation extends AppCompatActivity {

    int i=1;
    private Spinner spinner;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        Intent intent2 = getIntent();
        userID=intent2.getStringExtra("userID");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton myInfoButton = findViewById(R.id.myinfobutton_donation);
        myInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Donation.this , Myinfo.class );
                i.putExtra("userID",userID);
                startActivity(i);
            }
        });

        Button d = findViewById(R.id.doantion_donation);
        d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Donation.this);

                builder.setTitle("확인 메세지")
                        .setMessage("이 환자에게 헌혈증을 기부하시겠습니까?")
                        .setCancelable(false)// 뒤로버튼으로 취소금지
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(Donation.this);
                                builder.setTitle("\n헌혈증 기부가 완료되었습니다.")
                                        .setMessage("감사합니다.")
                                        .setCancelable(false)// 뒤로버튼으로 취소금지
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog dialog2 = builder.create();
                                dialog2.show();

                                dialog.cancel();
                                i = 0;

                                // TODO : firebase 연동해서 기부 및 댓글 생성
                                int numOfDonation = spinner.getSelectedItemPosition() + 1;
                                EditText commentEditTextView = findViewById(R.id.donation_comment);
                                String comment = commentEditTextView.getText().toString();
                            }


                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // TODO : 유저의 헌혈증 보유갯수 가져와야 함
        int numOfCertificate = 5;

        spinner = findViewById(R.id.donation_number);
        String[] numberArray = new String[numOfCertificate];
        for (int i = 0; i < numOfCertificate; i++)
        {
            numberArray[i] = String.valueOf(i + 1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.blood_type_spinner_item, numberArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
