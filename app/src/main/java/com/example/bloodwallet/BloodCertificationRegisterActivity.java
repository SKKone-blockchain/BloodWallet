package com.example.bloodwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class BloodCertificationRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    TextView birthDateTextView;
    TextView donationDateTextView;
    Button male;
    Button female;
    boolean isMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_certification_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (Spinner) findViewById(R.id.blood_certificate_donation_type);
        String[] bloodTypeArray = { "전혈 320ml", "전혈 400ml", "혈장 500ml", "혈소판 250ml", "혈소판혈장 250ml + 300ml" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.blood_type_spinner_item, bloodTypeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        birthDateTextView = (TextView) findViewById(R.id.blood_certificate_birth);
        birthDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext());
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일";
                        birthDateTextView.setText(dateString);
                    }
                });
                dialog.show();
            }
        });

        donationDateTextView = (TextView) findViewById(R.id.blood_certificate_date);
        donationDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext());
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일";
                        donationDateTextView.setText(dateString);
                    }
                });
                dialog.show();
            }
        });

        male = (Button) findViewById(R.id.blood_certificate_male);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setBackgroundColor(getColor(R.color.colorPrimary));
                female.setBackgroundColor(getColor(R.color.colorInactive));
                isMale = true;
            }
        });

        female = (Button) findViewById(R.id.blood_certificate_female);
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setBackgroundColor(getColor(R.color.colorInactive));
                female.setBackgroundColor(getColor(R.color.colorPrimary));
                isMale = false;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }
}
