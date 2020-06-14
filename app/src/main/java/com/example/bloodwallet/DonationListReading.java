package com.example.bloodwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class DonationListReading extends LinearLayout {

    TextView donationtitleView;
    TextView donationtimeView;
    TextView donationstoryView;
    TextView donationpercentView;
    TextView donationcheckView;
    CircularProgressBar ProgressBar;

    public DonationListReading(Context context) {
        super(context);
        inflation_init(context);

        donationtitleView = (TextView)findViewById(R.id.title_listreading);
        donationtimeView = (TextView)findViewById(R.id.time_listreading);
        donationstoryView = (TextView)findViewById(R.id.stoty_listreading);
        donationpercentView = (TextView)findViewById(R.id.percent_listreading);
        donationcheckView = (TextView)findViewById(R.id.check_listreading);
        ProgressBar = findViewById(R.id.progress_reading);
        ProgressBar.setProgressBarColor(getResources().getColor(R.color.colorPrimary));
    }

    private void inflation_init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public void percent(String percent){ donationpercentView.setText(percent);}
    public void check(String check){ donationcheckView.setText(check);}
    public void setPercent(float percent) { ProgressBar.setProgress(percent); }

}
