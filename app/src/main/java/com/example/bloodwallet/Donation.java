package com.example.bloodwallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.example.bloodwallet.contract.BloodWallet;
import com.example.bloodwallet.retrofit.PayerResponse;
import com.example.bloodwallet.retrofit.PayerService;
import com.example.bloodwallet.task.PayerTask;
import com.example.bloodwallet.task.WithProgressView;
import com.google.android.material.snackbar.Snackbar;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.utils.Convert;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bloodwallet.Constants.CHAIN_ID;
import static com.example.bloodwallet.Constants.CONTRACT_ADDRES;
import static com.example.bloodwallet.Constants.PRIVATE_KEY;
import static com.example.bloodwallet.Constants.SCOPE_BASE_URL;

public class Donation extends AppCompatActivity implements WithProgressView {

    int i=1;
    private Spinner spinner;
    String userID;
    private static final String TAG = Donation.class.getSimpleName();
    private KlayCredentials mUserCredential;
    private BloodWallet mContract;

    private AppCompatCheckBox mPayerCheckbox;
    private String mPayerURL = "http://115.145.173.215:5555";
    private View mProgress;

    private String mCurrentServiceURL;
    private PayerService mPayerService;

    private String privateKey = "0xc305d195e88bc2022e052631ac7f2bafdc0c8fc7e31eafabe89647cea0de720c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        mProgress = findViewById(R.id.view_progress);
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

//        SharedPreferences pref = getSharedPreferences(APP_NAME, MODE_PRIVATE);
//        String privateKey = pref.getString(PRIVATE_KEY, null);


        assert privateKey != null;

        mUserCredential = KlayCredentials.create(privateKey);

        mContract = BloodWallet.load(
                CONTRACT_ADDRES,
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



                                // TODO: 클레이튼 트랜잭션 전송하기
                                donate("0x5039d770becfa6ae56df428f4a3f413560b15678", "2020-05-23", "000-000-93");




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
                CONTRACT_ADDRES
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
        String msg;

        if (resp == null || resp.getError() != null) {
            msg = resp == null ? "Failed to communicate with payer"
                    : "Something went wrong: " + resp.getError();
        } else {
            msg = "Accepted; your TX hash is " + resp.getTxhash();
            Log.d(TAG , resp.getTxhash());
        }

        // Show message
        Snackbar.make(mProgress, msg, Snackbar.LENGTH_LONG).show();

        // TODO start polling the result
    }

    private Uri getScopeUri(String txHash) {
        return Uri.parse(SCOPE_BASE_URL + "/tx/" + txHash);
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    private void onTransactionReceipt(KlayTransactionReceipt.TransactionReceipt receipt) {
        String msg;

        if (receipt == null) {
            msg = "Something went wrong";
        } else {
            // BigDecimal is handy when dealing with large integers
            BigDecimal gasUsed = Utils.hexToBigDecimal(receipt.getGasUsed());
            BigDecimal gasPrice = Utils.hexToBigDecimal(receipt.getGasPrice());

            // Convert the total amount of gas spent in KLAY using Convert.fromPeb
            BigDecimal gasSpent = Convert.fromPeb(gasUsed.multiply(gasPrice), Convert.Unit.KLAY);

            msg = gasSpent.toString() + " KLAY spent";
        }

        // Prepare Snackbar
        Snackbar snackbar = Snackbar.make(mProgress, msg, Snackbar.LENGTH_LONG);

        // Provide a Klaytnscope link for the (confirmed) transaction information
        if (receipt != null) {
            snackbar.setActionTextColor(getColor(R.color.white))
                    .setAction("Open in Scope", view -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(getScopeUri(receipt.getTransactionHash()));
                        startActivity(intent);
                    });
        }

        // Show message
        snackbar.show();
    }
}
