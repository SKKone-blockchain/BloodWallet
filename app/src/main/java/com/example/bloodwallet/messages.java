package com.example.bloodwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class messages extends LinearLayout {

    TextView message;

    public messages(Context context) {
        super(context);
        inflation_init(context);
        message = findViewById(R.id.messagetext);
    }

    private void inflation_init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_messages,this, true);
    }

    public void setMessages(String messagestr){
        message.setText(messagestr);
    }
}