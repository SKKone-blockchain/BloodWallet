package com.example.bloodwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BloodCertificationRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;

    TextView birthDateTextView;
    TextView donationDateTextView;

    EditText donationCodeEditTextView;
    EditText nameEditTextView;
    EditText donationSourceEditTextView;

    Button male;
    Button female;
    boolean isMale;

    Set<Map.Entry<String, HashMap>> certificates;

    String userID;
    String userName;
    String userBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_certification_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = getIntent().getStringExtra("userID");
        userName = getIntent().getStringExtra("userName");
        userBirthDate = getIntent().getStringExtra("userBirthDate");

        spinner = (Spinner) findViewById(R.id.blood_certificate_donation_type);
        final String[] bloodTypeArray = { "전혈 320ml", "전혈 400ml", "혈장 500ml", "혈소판 250ml", "혈소판혈장 250ml + 300ml" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.blood_type_spinner_item, bloodTypeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        birthDateTextView = (TextView) findViewById(R.id.blood_certificate_birth);
        birthDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext());
                String text = birthDateTextView.getText().toString();
                if (text.length() > 0) {
                    int year = Integer.parseInt(text.substring(0, 4));
                    int month = Integer.parseInt(text.substring(4, 6));
                    int day = Integer.parseInt(text.substring(6, 8));
                    dialog.updateDate(year, month, day);
                }
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = getDateString(year, month + 1, dayOfMonth);
                        birthDateTextView.setText(dateString);
                    }
                });
                dialog.show();
            }
        });

        nameEditTextView = (EditText) findViewById(R.id.blood_certificate_name);
        donationCodeEditTextView = (EditText) findViewById(R.id.blood_certificate_donation_code);
        donationSourceEditTextView = (EditText) findViewById(R.id.blood_certificate_source);

        donationDateTextView = (TextView) findViewById(R.id.blood_certificate_date);
        donationDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext());
                String text = donationDateTextView.getText().toString();
                if (text.length() > 0) {
                    int year = Integer.parseInt(text.substring(0, 4));
                    int month = Integer.parseInt(text.substring(4, 6));
                    int day = Integer.parseInt(text.substring(6, 8));
                    dialog.updateDate(year, month, day);
                }
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = getDateString(year, month + 1, dayOfMonth);
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

        Button submitButton = findViewById(R.id.blood_certificate_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditTextView.getText().toString();
                int birthDate = getDate(birthDateTextView.getText().toString());
                int donationDate = getDate(donationDateTextView.getText().toString());
                String source = donationSourceEditTextView.getText().toString();
                String code = donationCodeEditTextView.getText().toString();
                String donationType = bloodTypeArray[spinner.getSelectedItemPosition()];
                String sex = isMale? "male" : "female";

                if (name.length() <= 0) {
                    Toast.makeText(BloodCertificationRegisterActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (String.valueOf(birthDate).length() != 8) {
                    Toast.makeText(BloodCertificationRegisterActivity.this, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (String.valueOf(donationDate).length() != 8) {
                    Toast.makeText(BloodCertificationRegisterActivity.this, "헌혈일자를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (source.length() <= 0) {
                    Toast.makeText(BloodCertificationRegisterActivity.this, "혈액원명을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (String.valueOf(code).length() <= 0) {
                    Toast.makeText(BloodCertificationRegisterActivity.this, "증서번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                String certificationKey = "";
                HashMap validCertificate = null;
                for (Map.Entry<String, HashMap> entry : certificates) {
                    HashMap certificate = entry.getValue();
                    if (certificate.get("agency").equals(source)
                            && Integer.parseInt(certificate.get("birthdate").toString()) == birthDate
                            && certificate.get("code").toString().equals(code)
                            && Integer.parseInt(certificate.get("donation_date").toString()) == donationDate
                            && certificate.get("donation_type").equals(donationType)
                            && certificate.get("name").equals(name)
                            && certificate.get("sex").equals(sex)) {
                        certificationKey = entry.getKey();
                        validCertificate = certificate;
                        break;
                    }
                }

                if (certificationKey.length() <= 0 || validCertificate == null) {
                    Toast.makeText(BloodCertificationRegisterActivity.this, "유효하지 않은 헌혈증입니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference database = FirebaseDatabase.getInstance().getReference("certificates/" + certificationKey);

                HashMap<String, Object> child = new HashMap<>();
                HashMap<String, Object> owner = new HashMap<>();
                owner.put("hospital_code", "");
                String userID = getIntent().getStringExtra("userID");
                owner.put("owner_id", userID);
                owner.put("user_id", userID);
                child.put("owner", owner);
                database.updateChildren(child);

                finish();
            }
        });

        certificates = new HashSet<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("certificates");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (certificates.size() > 0) {
                    return;
                }

                HashMap<String, HashMap> posts = (HashMap)dataSnapshot.getValue();
                certificates = posts.entrySet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            try {
                byte[] byteArray = getIntent().getByteArrayExtra("certificate");
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ImageView certificateView = findViewById(R.id.blood_certificate_image);
                certificateView.setImageBitmap(bitmap);

                String ocr = extras.getString("ocr");
                String[] temp = ocr.split("\n");
                Pattern onlyNumberPattern = Pattern.compile("\\d+");
                List<String> parsedOcr = new ArrayList<String>();
                for (String data : temp) {
                    String matched = getOnlyCharacter(data);
                    Matcher m = onlyNumberPattern.matcher(data);
                    if (matched.length() > 0 && m.find()) {
                        parsedOcr.add(matched);
                    }
                }

                parseDataAndFillView(parsedOcr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        nameEditTextView.setText(userName);
        birthDateTextView.setText(userBirthDate);

        male.callOnClick();
    }

    private String getOnlyCharacter(String s) {
        String result = "";
        for (int i=0; i<s.length(); i++) {
            boolean match = Pattern.matches("^[ ㄱ-ㅎ가-힣0-9]*$", Character.toString(s.charAt(i)));
            if (match) {
                result += s.charAt(i);
            }
        }

        return result;
    }

    private void parseDataAndFillView(List<String> parsedOcr) {
        Pattern onlyNumberPattern = Pattern.compile("\\d+");
        Matcher m = onlyNumberPattern.matcher(parsedOcr.get(0));
        while(m.find()) {
            String s = donationCodeEditTextView.getText() + m.group();
            donationCodeEditTextView.setText(s);
        }

        m = onlyNumberPattern.matcher(parsedOcr.get(1));
        if (m.find()) {
            if (m.group().contains("32")) {
                spinner.setSelection(0);
            } else if (m.group().contains("40")) {
                spinner.setSelection(1);
            } else if (m.group().contains("50")) {
                spinner.setSelection(2);
            } else if (m.group().contains("25")) {
                spinner.setSelection(3);
            } else {
                spinner.setSelection(4);
            }
        }

        m = onlyNumberPattern.matcher(parsedOcr.get(2));
        setDateText(m, donationDateTextView);

        String sourceName = parsedOcr.get(3);
        m = onlyNumberPattern.matcher(sourceName);
        if (m.find()) {
            int index = sourceName.indexOf(m.group().charAt(0));
            donationSourceEditTextView.setText(sourceName.substring(0, index));
        }
    }

    private void setDateText(Matcher m, TextView textView) {
        String date = "";
        while (m.find()) {
            date += m.group() + " ";
        }
        if (date.split(" ").length < 3) {
            return;
        }

        int year = Integer.parseInt(date.split(" ")[0]);
        int month = Integer.parseInt(date.split(" ")[1]);
        int day = Integer.parseInt(date.split(" ")[2]);
        textView.setText(getDateString(year, month, day));
    }

    private String getDateString(int year, int month, int day) {
        return String.format("%04d%02d%02d", year, month, day);
    }

    private int getDate(String dateString) {
        return Integer.parseInt(dateString.replace(".", ""));
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }
}
