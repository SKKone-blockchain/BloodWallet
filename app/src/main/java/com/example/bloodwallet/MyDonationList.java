package com.example.bloodwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bloodwallet.contract.BloodWallet;
import com.example.bloodwallet.task.onGetDataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.klaytn.caver.crypto.KlayCredentials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.bloodwallet.Constants.CHAIN_ID;
import static com.example.bloodwallet.Constants.CONTRACT_ADDRESS;
import static com.example.bloodwallet.Constants.GAS_PROVIDER;

public class MyDonationList extends AppCompatActivity {
    ListView listView;
    MyDonationAdapter adapter;

    String userID;
    HashMap<String, ArrayList<String>> post2certificate = new HashMap<>();
    HashMap<String, Integer> certificate2index = new HashMap<>();
    public int index = 0;
    HashMap<String, String> code2hospital = new HashMap<>();

    boolean isChanged = false;
    private BloodWallet mContract;
    private String private_key = "";
    private String address = "";
    boolean isStarted;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        listView = (ListView) findViewById(R.id.donatedlist);
        adapter = new MyDonationAdapter();
        listView.setAdapter(adapter);
        isStarted = false;

        ImageButton myinfobutton=findViewById(R.id.myinfobutton_list);
        myinfobutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(MyDonationList.this , Myinfo.class);
                i.putExtra("userID",userID);
                startActivity(i);
            }
        });

        ListView listView = (ListView) findViewById(R.id.donatedlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyDonationList.this , StoryActivity.class);

                intent.putExtra("Button_on",0);
                intent.putExtra("userID", userID);
                MyDonationListItem item = (MyDonationListItem) listView.getItemAtPosition(position);
                intent.putExtra("postID", item.postID);
                intent.putExtra("writer", item.writer);
                intent.putExtra("title", item.title);
                intent.putExtra("content", item.content);
                intent.putExtra("donatedNum", item.donatedNum);
                intent.putExtra("goalNum", item.goalNum);
                startActivity(intent);
            }
        });

        // private key load 해서 사용하기
        // User의 Public key를 가져오기
        SharedPreferences pref = getSharedPreferences("KEY", MODE_PRIVATE);
        private_key = pref.getString("PRIVATE_KEY", "0x63e98ad7ee907dc08f2f3934808d256ff1dcc417579a1ccce577f67e341da43b");
        System.out.println("Private Key: " + private_key);
        assert private_key != null;

        address = pref.getString("PUBLIC_KEY", "0x5039d770becfa6ae56df428f4a3f413560b15678");
        System.out.println("Public Key: " + address);
        assert address != null;

        mContract = BloodWallet.load(
                CONTRACT_ADDRESS,
                CaverFactory.get(),
                KlayCredentials.create(private_key),
                CHAIN_ID,
                GAS_PROVIDER
        );

        DatabaseReference donationReference = mDatabase.getReference("users/" + userID + "/donations");
        DatabaseReference postReference = mDatabase.getReference("posts");
        DatabaseReference hospitalReference = mDatabase.getReference("certificates");
        DatabaseReference hosCodeReference = mDatabase.getReference("hospitals");

        getHospitalCode(hosCodeReference, new onGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                getDonationHistory(donationReference, new onGetDataListener() {

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        System.out.println("Success step 1");
                        getPost(postReference, new onGetDataListener() {

                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                System.out.println("Success step 2");
                                getHospitalHistory(hospitalReference, new onGetDataListener() {

                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        if (isChanged){
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MyDonationList.this);
                                            builder.setTitle("\n헌혈증서 기부 내역 조회")
                                                    .setMessage("기부 기록에 문제가 있습니다.")
                                                    .setCancelable(false)// 뒤로버튼으로 취소금지
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog dialog2 = builder.create();
                                            dialog2.show();
                                        } else {
                                            if (!isStarted) {
                                                Toast.makeText(getApplicationContext(), "헌혈증서 기부 기록이 무결합니다.", Toast.LENGTH_SHORT).show();
                                                isStarted = true;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onFailure() {

                                    }
                                });
                            }

                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onFailure() {

                            }
                        });
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
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    class MyDonationAdapter extends BaseAdapter {

        private ArrayList<MyDonationListItem> listViewItemList = new ArrayList<MyDonationListItem>();

        @Override
        public int getCount() { return listViewItemList.size(); }

        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DonationListReading view = new DonationListReading(getApplicationContext());

            MyDonationListItem item = (MyDonationListItem) getItem(position);
            view.setTitle(item.title);
            view.setTime(item.time);
            view.setStory(item.content);
            view.check(item.check);
            int percent = (int)((double)item.donatedNum / item.goalNum * 100);

            view.percent(item.donation + "개");
            view.setPercent(percent);

            return view;
        }

        public void addItem(MyDonationListItem item) {
            listViewItemList.add(item);
        }

        public void clearAllItems() {
            listViewItemList.clear();
        }
    }

    public class MyDonationListItem {
        public String postID;
        public String writer;
        public String title;
        public String content;
        public String summary;
        public String time;
        public String check;
        public int donatedNum;
        public int goalNum;
        public int donation;
    }

    public void getHospitalCode(DatabaseReference databaseReference, final onGetDataListener listener) {
        listener.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot hospitalSnapshot : dataSnapshot.getChildren()){
                    code2hospital.put(hospitalSnapshot.child("code").getValue(String.class), hospitalSnapshot.child("name").getValue(String.class));
                }
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void getDonationHistory(DatabaseReference databaseReference, final onGetDataListener listener){
        listener.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot donateSnapshot : dataSnapshot.getChildren()){
                    // TODO: 기부한 Post ID, Certificates map 생성
                    if (post2certificate.containsKey(donateSnapshot.getValue(String.class))) {
                        post2certificate.get(donateSnapshot.getValue(String.class)).add(donateSnapshot.getKey());
                    } else {
                        post2certificate.put(donateSnapshot.getValue(String.class), new ArrayList<String>(Arrays.asList(donateSnapshot.getKey())));
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

    public void getHospitalHistory(DatabaseReference databaseReference, final onGetDataListener listener) {
        listener.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // TODO: 고치기
                if (!isStarted) {
                    for (DataSnapshot hospitalSnapshot : dataSnapshot.getChildren()) {
                        if (certificate2index.containsKey(hospitalSnapshot.getKey())) {
                            if (!hospitalSnapshot.child("owner").child("hospital_code").getValue(String.class).isEmpty()) {
                                int idx = certificate2index.get(hospitalSnapshot.getKey());
                                String hos_name = code2hospital.get(hospitalSnapshot.child("owner").child("hospital_code").getValue(String.class));
                                ((MyDonationListItem) adapter.getItem(idx)).check = hos_name + " 접수 완료";
                            } else {
                                int idx = certificate2index.get(hospitalSnapshot.getKey());
                                ((MyDonationListItem) adapter.getItem(idx)).check = "접수중";
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }

                    listener.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void getPost(DatabaseReference databaseReference, final onGetDataListener listener) {
        listener.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clearAllItems();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (post2certificate.containsKey(postSnapshot.getKey())) {
                        // TODO: getOwner로 확인하기
                        OwnerCheckTask ownerCheckTask = new OwnerCheckTask();
                        ArrayList<String> certificate_list = post2certificate.get(postSnapshot.getKey());

                        ownerCheckTask.execute(certificate_list.get(0));
                        String owner = "";
                        try {
                            owner = ownerCheckTask.get();
                            owner = owner.replace("=", ",");
                            owner = owner.split(",")[1];
                            System.out.println("Owner: " + owner);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        if (owner.equals(postSnapshot.child("public_key").getValue(String.class))) {
                            MyDonationListItem item = new MyDonationListItem();
                            item.postID = postSnapshot.child("post_id").getValue(String.class);
                            item.writer = postSnapshot.child("user_id").getValue(String.class);
                            item.title = postSnapshot.child("title").getValue(String.class);
                            item.summary = postSnapshot.child("summary").getValue(String.class);
                            item.content = postSnapshot.child("story").getValue(String.class);
                            item.time = postSnapshot.child("timestamp").getValue(String.class).split("-")[1] +
                                    "월" + postSnapshot.child("timestamp").getValue(String.class).split("-")[2] + "일";
                            item.donatedNum = postSnapshot.child("donated_num").getValue(Integer.class);
                            item.goalNum = postSnapshot.child("target_num").getValue(Integer.class);
                            item.check = "";
                            item.donation = post2certificate.get(item.postID).size();

                            adapter.addItem(item);

                            for (int i = 0; i < certificate_list.size(); i++) {
                                certificate2index.put(certificate_list.get(i), index);
                            }

                            index++;
                        } else {
                            isChanged = true;
                        }
                    }
                }

                listView.setAdapter(adapter);
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    class OwnerCheckTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                return mContract.getOwner(params[0]).send().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                System.out.println("Holding Count: " + s);
            }
        }
    }
}
