package com.example.bloodwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DonationsRecievedListReading extends LinearLayout {

    TextView RecievedtitleView;
    TextView RecievedtimeView;
    TextView RecievedstoryView;
    TextView RecievedpercentView;

    public DonationsRecievedListReading(Context context) {
        super(context);
        inflation_init(context);
        RecievedtitleView= (TextView)findViewById(R.id.title_recievedreading);
        RecievedtimeView=(TextView)findViewById(R.id.time_recievedreading);
        RecievedstoryView= (TextView)findViewById(R.id.stoty_recievedreading);
        RecievedpercentView= (TextView)findViewById(R.id.percent_recievedreading);
    }
    private void inflation_init(Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_donations_received_list_reading,this, true);

    }

    public void setTitle(String title){
        RecievedtitleView.setText(title);
    }
    public void setTime(String time){
        RecievedtimeView.setText(time);
    }
    public void setStory(String story){  RecievedstoryView.setText(story);}
    public void percent(String percent){
        RecievedpercentView.setText(percent);
    }
}