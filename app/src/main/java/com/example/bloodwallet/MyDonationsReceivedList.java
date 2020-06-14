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
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.bloodwallet.Constants.CHAIN_ID;
import static com.example.bloodwallet.Constants.CONTRACT_ADDRESS;
import static com.example.bloodwallet.Constants.GAS_PROVIDER;

public class MyDonationsReceivedList extends AppCompatActivity {
    ListView listView;
    MyDonationReceivedAdapter adapter;
    String userID;
    private BloodWallet mContract;
    private String private_key = "";
    private String address = "";
    boolean isStarted;

    private ArrayList<Post> postList = new ArrayList<>();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations_received_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        System.out.println("User id via intent :" + userID);

        isStarted = false;

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
                Intent i = new Intent(MyDonationsReceivedList.this , Myinfo.class);
                i.putExtra("userID",userID);
                startActivity(i);
            }
        });

        ListView listView = (ListView) findViewById(R.id.donationreceivedlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyDonationsReceivedList.this , StoryActivity.class);
                intent.putExtra("Button_on",0);
                intent.putExtra("userID", userID);
                intent.putExtra("writer", userID);
                MyDonationReceivedListItem item = (MyDonationReceivedListItem) listView.getItemAtPosition(position);
                intent.putExtra("postID", item.postID);
                intent.putExtra("title", item.title);
                intent.putExtra("content", item.content);
                intent.putExtra("donatedNum", item.donatedNum);
                intent.putExtra("goalNum", item.goalNum);
                startActivity(intent);
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
                adapter = new MyDonationReceivedAdapter();
                int total = 0;
                for (int i = 0; i < postList.size(); i++) {
                    total += postList.get(i).donated_num;

                    // Add data into list
                    MyDonationReceivedListItem item = new MyDonationReceivedListItem();
                    item.postID = postList.get(i).post_id;
                    item.title = postList.get(i).title;
                    item.content = postList.get(i).story;
                    item.donatedNum = postList.get(i).donated_num;
                    item.goalNum = postList.get(i).target_num;
                    item.time = postList.get(i).timestamp.split("-")[1] + "월" +  postList.get(i).timestamp.split("-")[2] + "일";
                    adapter.addItem(item);
                }

                System.out.println("total: " + total);

                if (total != Integer.parseInt(count)){
                    System.out.println("DATA IS CHANGED!!!");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyDonationsReceivedList.this);
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
                    if(!isStarted) {
                        System.out.println("Nothing changed");
                        Toast.makeText(getApplicationContext(), "헌혈 증서 기부 기록이 무결합니다.", Toast.LENGTH_SHORT).show();
                        isStarted = true;
                    }
                }

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

    class MyDonationReceivedAdapter extends BaseAdapter {

        private ArrayList<MyDonationReceivedListItem> listViewItemList = new ArrayList<MyDonationReceivedListItem>();

        @Override
        public int getCount() {
            return listViewItemList.size();
        }

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

            DonationsRecievedListReading view = new DonationsRecievedListReading(getApplicationContext());

            MyDonationReceivedListItem item = (MyDonationReceivedListItem) getItem(position);
            view.setTitle(item.title);
            view.setTime(item.time);
            view.setStory(item.content);
            int percent = (int)((double)item.donatedNum / item.goalNum * 100);
            view.percent(percent + "%");
            view.setPercent(percent);

            return view;
        }

        public void addItem(MyDonationReceivedListItem item) {
            this.listViewItemList.add(item);
        }

        public void clearAllItems() {
            this.listViewItemList.clear();
        }
    }

    public class MyDonationReceivedListItem {
        public String postID;
        public String title;
        public String content;
        public String time;
        public int donatedNum;
        public int goalNum;
    }

    public void getMyPost(DatabaseReference post_ref, String user_id, final onGetDataListener listener){
        listener.onStart();
        post_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!isStarted) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String write_id = postSnapshot.child("user_id").getValue(String.class);
                        Post post = postSnapshot.getValue(Post.class);

                        // TODO: User ID 가져오기
                        if (write_id.equals(user_id)) {
                            postList.add(post);
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
}
