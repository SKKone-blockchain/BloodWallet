package com.example.bloodwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DonationListReading extends LinearLayout {

    TextView donationtitleView;
    TextView donationtimeView;
    TextView donationstoryView;

    public DonationListReading(Context context) {
        super(context);
        inflation_init(context);
        donationtitleView= (TextView)findViewById(R.id.title_listreading);
        donationtimeView=(TextView)findViewById(R.id.time_listreading);
        donationstoryView= (TextView)findViewById(R.id.stoty_listreading);
    }
    private void inflation_init(Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_donation_list_reading,this, true);

    }

    public void setTitle(String title){
        donationtitleView.setText(title);
    }
    public void setTime(String time){
        donationtimeView.setText(time);
    }
    public void setStory(String story){
        donationstoryView.setText(story);
    }


}
