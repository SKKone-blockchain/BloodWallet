package com.example.bloodwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MyDonationList extends AppCompatActivity {
    ListView listView;
    myadapter adapter;
    String[] title={"혈액이 급합니다 도와주세요(백혈병)","교통사고 수술(교통사고)","희귀병 수술을 위한 혈액이 필요해요(희귀병)","수술할 돈이 없습니다(대장암)"};
    String[] time={"10:56 PM","2:00 PM","11:03 AM","07:14 AM"};
    String[] story={"aaaa","bbbb","cccc","dddd"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView =(ListView) findViewById(R.id.donatedlist);
        adapter = new myadapter();
        listView.setAdapter(adapter);


        ImageButton myinfobutton=findViewById(R.id.myinfobutton_list);
        myinfobutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(  MyDonationList.this , Myinfo.class );
                startActivity(i);
            }
        });
    }
    class myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return title[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            DonationListReading view= new DonationListReading(getApplicationContext());
            view.setTitle(title[position]);
            view.setTime(time[position]);
            view.setStory(story[position]);

            return view;
        }
    }
}
