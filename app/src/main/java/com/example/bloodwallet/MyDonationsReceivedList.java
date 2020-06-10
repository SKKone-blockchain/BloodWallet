package com.example.bloodwallet;

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

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.bloodwallet.Constants.CHAIN_ID;
import static com.example.bloodwallet.Constants.CONTRACT_ADDRESS;
import static com.example.bloodwallet.Constants.GAS_PROVIDER;

public class MyDonationsReceivedList extends AppCompatActivity {
    ListView listView;
    ArrayList<String> title = new ArrayList<>(); // {"기부 받은 기록1","기부 받은 기록2","기부 받은 기록3"};
    ArrayList<String> time = new ArrayList<>(); //{"10:56 PM","2:00 PM","11:03 AM"};
    ArrayList<String> story = new ArrayList<>(); //{"aaaa","bbbb","cccc"};
    ArrayList<String> percent = new ArrayList<>(); //{"100%","100%","50%"};
    ArrayList<Integer> num_percent = new ArrayList<>();
    myadapter adapter;
    String userID;
    private BloodWallet mContract;
    private String private_key = "";
    private String address = "";

    private ArrayList<Post> post_list = new ArrayList<>();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations_received_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: user id intent 다시 정리하기
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        System.out.println("User id via intent :" + userID);


        listView = (ListView)findViewById(R.id.donationreceivedlist);


        // SharedPreferences pref = getSharedPreferences("KEY", MODE_PRIVATE);
        // TODO: private key load 해서 사용하기
        // TODO: User의 Public key를 가져오기
        SharedPreferences pref = getSharedPreferences("KEY", MODE_PRIVATE);
        private_key = pref.getString("PRIVATE_KEY", "0x63e98ad7ee907dc08f2f3934808d256ff1dcc417579a1ccce577f67e341da43b");
        System.out.println("Private Key: " + private_key);
        assert private_key != null;

        address = pref.getString("PUBLIC_KEY", "0x5039d770becfa6ae56df428f4a3f413560b15678");
        System.out.println("Public Key: " + address);
        assert address != null;





        ImageButton myinfobutton = findViewById(R.id.myinfobutton_list);
        myinfobutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MyDonationsReceivedList.this , Myinfo.class );
                i.putExtra("userID",userID);
                startActivity(i);
            }
        });

        ListView listView = (ListView) findViewById(R.id.donationreceivedlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(  MyDonationsReceivedList.this , StoryActivity.class );
                i.putExtra("Button_on",0);
                startActivity(i);
            }
        });

        mContract = BloodWallet.load(
                CONTRACT_ADDRESS,
                CaverFactory.get(),
                KlayCredentials.create(private_key),
                CHAIN_ID,
                GAS_PROVIDER
        );

        // TODO: 내가 쓴 글 List 정보 띄우기
        DatabaseReference post_ref = mDatabase.getReference("posts");
        getMyPost(post_ref, userID, new onGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // 유저의 헌혈증 보유갯수 가져와야 함: Firebase 동기화 먼저

                CountCheckTask countCheckTask = new CountCheckTask();
                countCheckTask.execute(address);
                String count = "";
                try{
                    count = countCheckTask.get();
                    System.out.println("Holding Count : " + count);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (ExecutionException e) {
                    e.printStackTrace();
                }


                // 내가 쓴 글에서 총 기부 받은 횟수와 스마트 컨트랙트에서 기부 받은 횟수가 동일한지 비교
                int total = 0;
                for (int i = 0; i < post_list.size(); i++){
                    total += post_list.get(i).donated_num;

                    // Add data into list
                    title.add(post_list.get(i).title);
                    story.add(post_list.get(i).story);

                    double donated = post_list.get(i).donated_num;
                    double goal = post_list.get(i).target_num;
                    System.out.println("Percent " + donated / goal * 100.0f);
                    num_percent.add((int)Math.round(donated / goal * 100));
                    percent.add(String.format("%.1f", donated/goal  * 100.0f) + "%");
                    time.add(post_list.get(i).timestamp.split("-")[1] + "월" +  post_list.get(i).timestamp.split("-")[2] + "일");

                }
                System.out.println("total: " + total);

                if (total != Integer.parseInt(count)){
                    System.out.println("DATA IS CHANGED!!!");
                    Toast.makeText(getApplicationContext(), "헌혈 증서 기부 기록에 문제가 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    System.out.println("Nothing changed");
                    Toast.makeText(getApplicationContext(), "헌혈 증서 기부 기록이 무결합니다.", Toast.LENGTH_SHORT).show();
                }

                adapter = new myadapter();
                listView.setAdapter(adapter);
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

    class CountCheckTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                return mContract.getHoldingCount(params[0]).send().toString();
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

    class myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return title.size();
        }

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

            DonationsRecievedListReading view = new DonationsRecievedListReading(getApplicationContext());
            view.setTitle(title.get(position));
            view.setTime(time.get(position));
            view.setStory(story.get(position));
            view.percent(percent.get(position));
            view.setPercent(num_percent.get(position));

            return view;
        }
    }

    public void getMyPost(DatabaseReference post_ref, String user_id, final onGetDataListener listener){
        listener.onStart();
        post_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String write_id = postSnapshot.child("user_id").getValue(String.class);
                    Post post = postSnapshot.getValue(Post.class);

                    // TODO: User ID 가져오기
                    if(write_id.equals(user_id)){
                        post_list.add(post);
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
