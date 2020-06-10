package com.example.bloodwallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.contentcapture.DataRemovalRequest;
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
import android.widget.TextView;

import com.example.bloodwallet.contract.BloodWallet;
import com.example.bloodwallet.retrofit.PayerResponse;
import com.example.bloodwallet.retrofit.PayerService;
import com.example.bloodwallet.task.PayerTask;
import com.example.bloodwallet.task.WithProgressView;
import com.example.bloodwallet.task.onGetDataListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.utils.Convert;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bloodwallet.Constants.CHAIN_ID;
import static com.example.bloodwallet.Constants.CONTRACT_ADDRESS;
import static com.example.bloodwallet.Constants.NONCE_BIAS;
import static com.example.bloodwallet.Constants.PRIVATE_KEY;
import static com.example.bloodwallet.Constants.SCOPE_BASE_URL;

public class Donation extends AppCompatActivity implements WithProgressView {

    private int num_smart_contract_call = 0;
    private int current_num_smart_contract_call = 0;
    private String result_line = "";
    private int success = 0;
    private int num_donated = 0;
    boolean flag = false;

    int i=1;
    private Spinner spinner;
    private TextView holding_count;
    String userID;
    private static final String TAG = Donation.class.getSimpleName();
    private KlayCredentials mUserCredential;
    private BloodWallet mContract;

    private AppCompatCheckBox mPayerCheckbox;
    private String mPayerURL = "http://115.145.173.215:5555";
    private View mProgress;

    private String mCurrentServiceURL;
    private PayerService mPayerService;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private String receiver_public_key;
    private ArrayList<String> available_certificates = new ArrayList<>();

    // 넘어와야 하는 것: Writer, Post ID
    private String writer;
    private String post_id;

    // TODO: private key sharedPreference로 가져오기
    private String privateKey;

    TextView patientInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        mProgress = findViewById(R.id.view_progress);
        Intent intent2 = getIntent();
        userID = intent2.getStringExtra("userID");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        post_id = getIntent().getStringExtra("postID");
        writer = getIntent().getStringExtra("writer");

        ImageButton myInfoButton = findViewById(R.id.myinfobutton_donation);
        myInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Donation.this , Myinfo.class );
                i.putExtra("userID",userID);
                startActivity(i);
            }
        });

        patientInfoTextView = findViewById(R.id.donation_patient_info);

        SharedPreferences pref = getSharedPreferences("KEY", MODE_PRIVATE);
        privateKey = pref.getString("PRIVATE_KEY", "0x63e98ad7ee907dc08f2f3934808d256ff1dcc417579a1ccce577f67e341da43b");
        System.out.println("Private Key: " + privateKey);

        assert privateKey != null;

        // 기부 받을 사람의 Public key 가져오기
        DatabaseReference public_ref = mDatabase.getReference("users/"+ writer + "/public_key");
        public_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiver_public_key = dataSnapshot.getValue(String.class);
                System.out.println("receiver public key " + receiver_public_key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference patient_ref = mDatabase.getReference("users/"+ writer);
        patient_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sex = dataSnapshot.child("sex").getValue(String.class);
                String birthdate = dataSnapshot.child("birthdate").getValue(String.class);
                int userYear = Integer.parseInt(birthdate.substring(0, 4));

                // 나이 계산하기
                sex = (sex.equals("male"))? "남자" : "여자";

                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date(System.currentTimeMillis());
                String today = formatter.format(date);
                int year = Integer.parseInt(today.substring(0, 4));
                int age = (year - userYear + 1);

                String info = "환자 ID : " + writer + "\n환자 정보 : " + String.valueOf(age) + "세, " + sex;

                patientInfoTextView.setText(info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference post_ref = mDatabase.getReference("posts/" + post_id + "/donated_num");
        post_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                num_donated = dataSnapshot.getValue(Integer.class);
                System.out.println("Donated number " + num_donated);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // 기부자의 보유 헌혈증서 코드 모두 가져오기
        DatabaseReference certificate_ref = mDatabase.getReference("certificates");
        getCertificate(certificate_ref, userID, new onGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // 유저의 헌혈증 보유갯수 가져와야 함: Firebase 동기화 먼저
                System.out.println("Success; available certificates: " + available_certificates.size());
                int numOfCertificate = available_certificates.size();

                if (!flag) {
                    if (numOfCertificate <= 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Donation.this);
                        builder.setTitle("\n죄송합니다.")
                                .setMessage("보유한 헌혈 증서가 없습니다.")
                                .setCancelable(false)// 뒤로버튼으로 취소금지
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        dialog.cancel();
                                        Intent intent = new Intent(Donation.this, StoryListActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog dialog2 = builder.create();
                        dialog2.show();
                    }
                }

                holding_count = (TextView)findViewById(R.id.holding_count);
                holding_count.setText(String.valueOf(numOfCertificate));

                spinner = findViewById(R.id.donation_number);
                String[] numberArray = new String[numOfCertificate];
                for (int i = 0; i < numOfCertificate; i++)
                {
                    numberArray[i] = String.valueOf(i + 1);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Donation.this, R.layout.blood_type_spinner_item, numberArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onStart() {
                Log.d("On start", "Started");
            }

            @Override
            public void onFailure() {
                Log.d("onFailure", "Failed");
            }
        });

        mUserCredential = KlayCredentials.create(privateKey);

        mContract = BloodWallet.load(
                CONTRACT_ADDRESS,
                CaverFactory.get(),
                mUserCredential,
                CHAIN_ID,
                new DefaultGasProvider()
        );

        Button d = findViewById(R.id.doantion_donation);
        d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Donation.this);

                builder.setTitle("확인 메세지")
                        .setMessage("이 환자에게 헌혈증을 기부하시겠습니까?")
                        .setCancelable(false)// 뒤로버튼으로 취소금지
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {



                                // 클레이튼 트랜잭션 전송하기
                                // Timestamp 얻기
                                num_smart_contract_call = Integer.parseInt(spinner.getSelectedItem().toString());
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                                Date date = new Date();
                                String timestamp = formatter.format(date);

                                for (int j = 0; j < num_smart_contract_call; j++){
                                    donate(receiver_public_key, timestamp, available_certificates.get(j));
                                }

                                dialog.cancel();
                                i = 0;
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

    private void donate(String owner, String timestamp, String code){

        List<Type> list = Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner),
                new org.web3j.abi.datatypes.Utf8String(timestamp),
                new org.web3j.abi.datatypes.Utf8String(code));

        runWithPayer(new Function(BloodWallet.FUNC_DONATECERTIFICATE, list, Collections.emptyList()));
    }

    private void runWithPayer(Function function) {
        new PayerTask(
                this,
                mUserCredential,
                this::onPayerResponse
        ).execute(
                getPayerService(mPayerURL),
                Numeric.hexStringToByteArray(FunctionEncoder.encode(function)),
                CONTRACT_ADDRESS
        );
    }

    private PayerService getPayerService(String url) {
        if (mPayerService != null && url.equals(mCurrentServiceURL)) return mPayerService;

        mCurrentServiceURL = url;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mCurrentServiceURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mPayerService = retrofit.create(PayerService.class);
        return mPayerService;
    }

    private void onPayerResponse(PayerResponse resp) {

        current_num_smart_contract_call += 1;

        if (resp == null || resp.getError() != null) {
            result_line += resp == null ? "Fee Payer에 연결에 실패하였습니다.\n\n"
                    : "기부에 실패하였습니다.: " + resp.getError() +"\n\n";

            if(current_num_smart_contract_call == num_smart_contract_call) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Donation.this);
                builder.setTitle("\n헌혈증서 기부 결과")
                        .setMessage(result_line)
                        .setCancelable(false)// 뒤로버튼으로 취소금지
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.cancel();
                                Intent intent = new Intent(Donation.this, MainInfo.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog dialog2 = builder.create();
                dialog2.show();
                flag = true;
            }
        }
        else {
            success ++;
            result_line += "성공; Transaction Hash: " + resp.getTxhash() + "\n\n";
            Log.d(TAG , resp.getTxhash());


            if(current_num_smart_contract_call == num_smart_contract_call) {
                current_num_smart_contract_call = 0;
                num_smart_contract_call = 0;
                NONCE_BIAS = BigInteger.valueOf(0);
                System.out.println("Reset nonce bias " + NONCE_BIAS);

                AlertDialog.Builder builder = new AlertDialog.Builder(Donation.this);
                builder.setTitle("\n헌혈증서 기부 결과")
                        .setMessage(String.valueOf(success) + "개의 헌혈증 기부 성공!\n\n" + result_line)
                        .setCancelable(false)// 뒤로버튼으로 취소금지
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.cancel();
                                Intent intent = new Intent(Donation.this, MainInfo.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog dialog2 = builder.create();
                dialog2.show();
                flag = true;

                // Firebase에 정보 Update 하기기
                DatabaseReference postReference = mDatabase.getReference("posts/" + post_id);

                // 1. Post에 기부된 개수
                Map<String, Object> postUpdate = new HashMap<>();
                postUpdate.put("donated_num", num_donated + 1);

                // 2. Post에 comment 추가
                EditText commentEditTextView = (EditText) findViewById(R.id.donation_comment);
                String comment = commentEditTextView.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                Date date = new Date();
                String timestamp = formatter.format(date);
                postUpdate.put("comments/comment" + timestamp, new Comment(timestamp, comment, userID));

                postReference.updateChildren(postUpdate);

                // 3. 사용된 헌혈증서의 owner를 기부받은 사람으로 바꾸기
                DatabaseReference certificate_owner_ref = mDatabase.getReference("certificates");
                Map<String, Object> certificateUpdate = new HashMap<>();

                for (int i = 0; i < success; i++) {
                    certificateUpdate.put(available_certificates.get(i) + "/owner/owner_id", writer);
                }
                certificate_owner_ref.updateChildren(certificateUpdate);

                // 4. User의 보유 헌혈증 개수 update하기
                DatabaseReference user_ref = mDatabase.getReference("users/" + userID);
                Map<String, Object> userUpdate = new HashMap<>();
                userUpdate.put("holding_count", available_certificates.size() - success);
                user_ref.updateChildren(userUpdate);

                // 5. User의 기부 내역
                DatabaseReference donation_ref = mDatabase.getReference("users/" + userID + "/donations");
                Map<String, Object> donationUpdate = new HashMap<>();
                for (int i = 0; i < success; i++) {
                    donationUpdate.put(available_certificates.get(i), post_id);
                }
                donation_ref.updateChildren(donationUpdate);


                result_line = "";
                success = 0;

            }
        }

    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    public void getCertificate(DatabaseReference certificate_ref, String userID, final onGetDataListener listener){

        listener.onStart();
        certificate_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                available_certificates.clear();

                for (DataSnapshot certificateSnapshot : dataSnapshot.getChildren()){
                    String code = certificateSnapshot.getKey();
                    Owner ownership = certificateSnapshot.child("owner").getValue(Owner.class);

                    // TODO: User ID 가져오기
                    if(ownership.user_id.equals(ownership.owner_id) && ownership.hospital_code.isEmpty()){
                        //Available
                        System.out.println("available code " + code );
                        available_certificates.add(code);
                    }
                }
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });

    }


}
