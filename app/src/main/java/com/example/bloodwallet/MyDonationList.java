package com.example.bloodwallet;

import android.content.Intent;
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

import org.web3j.abi.datatypes.Int;
import org.web3j.tuples.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.bloodwallet.Constants.CHAIN_ID;
import static com.example.bloodwallet.Constants.CONTRACT_ADDRESS;
import static com.example.bloodwallet.Constants.GAS_PROVIDER;

public class MyDonationList extends AppCompatActivity {
    ListView listView;
    myadapter adapter;
    ArrayList<String> title = new ArrayList<>(); //{"혈액이 급합니다 도와주세요(백혈병)","교통사고 수술(교통사고)","희귀병 수술을 위한 혈액이 필요해요(희귀병)","수술할 돈이 없습니다(대장암)"};
    ArrayList<String> time = new ArrayList<>(); //{"10:56 PM","2:00 PM","11:03 AM","07:14 AM"};
    ArrayList<String> story = new ArrayList<>(); //{"aaaa","bbbb","cccc","dddd"};
    ArrayList<String> percent = new ArrayList<>(); //{"0%","30%", "95%", "100%"};
    ArrayList<String> check = new ArrayList<>(); //{"사용\n대기중","사용\n대기중","사용\n대기중","사용\n완료"};

    String userID;
    HashMap<String, String> post2certificate = new HashMap<>();
    HashMap<String, Integer> certificate2index = new HashMap<>();
    public int index = 0;
    HashMap<String, String> code2hospital = new HashMap<>();

    private BloodWallet mContract;
    private String private_key = "";
    private String address = "";


    private ArrayList<String> address_list = new ArrayList<>();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        userID=intent.getStringExtra("userID");
        listView =(ListView) findViewById(R.id.donatedlist);
        adapter = new myadapter();
        listView.setAdapter(adapter);


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
                Intent i = new Intent(  MyDonationList.this , StoryActivity.class );
                i.putExtra("Button_on",0);
                startActivity(i);
            }
        });

        // SharedPreferences pref = getSharedPreferences("KEY", MODE_PRIVATE);
        // TODO: private key load 해서 사용하기
        // TODO: User의 Public key를 가져오기
        private_key = "0x63e98ad7ee907dc08f2f3934808d256ff1dcc417579a1ccce577f67e341da43b"; //pref.getString("PRIVATE_KEY", null);
        System.out.println("Private Key: " + private_key);
        assert private_key != null;

        address = "0x5039d770becfa6ae56df428f4a3f413560b15678"; //pref.getString("PUBLIC_KEY", null);
        System.out.println("Public Key: " + address);
        assert address != null;

        mContract = BloodWallet.load(
                CONTRACT_ADDRESS,
                CaverFactory.get(),
                KlayCredentials.create(private_key),
                CHAIN_ID,
                GAS_PROVIDER
        );

        // TODO: 내가 쓴 글 List 정보 띄우기
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
                                        adapter = new myadapter();
                                        listView.setAdapter(adapter);
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


    class myadapter extends BaseAdapter{
        @Override
        public int getCount() { return title.size(); }

        @Override
        public Object getItem(int position) {
            return title.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            DonationListReading view= new DonationListReading(getApplicationContext());
            view.setTitle(title.get(position));
            view.setTime(time.get(position));
            view.setStory(story.get(position));
            view.percent(percent.get(position));
            view.check(check.get(position));


            return view;
        }
    }

    public void getHospitalCode(DatabaseReference databaseReference, final onGetDataListener listener){
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
                    post2certificate.put(donateSnapshot.getValue(String.class), donateSnapshot.getKey());
                }
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });

    }

    public void getHospitalHistory(DatabaseReference databaseReference, final onGetDataListener listener){
        listener.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot hospitalSnapshot : dataSnapshot.getChildren()){
                    // TODO: 병원 정보 가져오기
                    if(certificate2index.containsKey(hospitalSnapshot.getKey())){
                        if(!hospitalSnapshot.child("owner").child("hospital_code").getValue(String.class).isEmpty()){
                            int idx = certificate2index.get(hospitalSnapshot.getKey());
                            String hos_name = code2hospital.get(hospitalSnapshot.child("owner").child("hospital_code").getValue(String.class));
                            check.set(idx, hos_name + " 접수 완료");
                        }
                        else{
                            int idx = certificate2index.get(hospitalSnapshot.getKey());
                            check.set(idx, "접수중");
                        }

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


    public void getPost(DatabaseReference databaseReference, final onGetDataListener listener){
        listener.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    if (post2certificate.containsKey(postSnapshot.getKey())){
                        // TODO: getOwner로 확인하기
                        OwnerCheckTask ownerCheckTask = new OwnerCheckTask();
                        String a = post2certificate.get(postSnapshot.getKey());
                        ownerCheckTask.execute(a);
                        String owner = "";
                        try{
                            owner = ownerCheckTask.get();
                            owner = owner.replace("=", ",");
                            owner = owner.split(",")[1];
                            System.out.println("Owner: " + owner);


                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        if (owner.equals(postSnapshot.child("public_key").getValue(String.class))){
                            // TODO: 동일하면 Append
                            title.add(postSnapshot.child("title").getValue(String.class));
                            time.add(postSnapshot.child("timestamp").getValue(String.class).split("-")[1] + "월" + postSnapshot.child("timestamp").getValue(String.class).split("-")[2] + "일");
                            story.add(postSnapshot.child("content").getValue(String.class));

                            double donated = postSnapshot.child("donated_num").getValue(Integer.class);
                            double goal = postSnapshot.child("goal_num").getValue(Integer.class);
                            System.out.println("Percent " + donated / goal * 100.0f);

                            percent.add(String.format("%.1f", donated/goal  * 100.0f) + "%");

                            check.add("");

                            certificate2index.put(post2certificate.get(postSnapshot.getValue(String.class)), index);
                            index ++;
                        }

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
