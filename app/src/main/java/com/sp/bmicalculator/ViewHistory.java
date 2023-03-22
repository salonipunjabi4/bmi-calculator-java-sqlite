package com.sp.bmicalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewHistory extends AppCompatActivity {
    TextView tvHistory;
    SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        tvHistory = findViewById(R.id.tvHistory);
        sp1 = getSharedPreferences("f1",MODE_PRIVATE);
        final DatabaseHandler db = new DatabaseHandler(this);

        String name = sp1.getString("name"," ");
        int age = sp1.getInt("age", 0);
        long phone = sp1.getLong("phone", 0);
        String gender = sp1.getString("gender", "" );

        String bmi = db.getDetails();
        String msg = "Name: " + name + "\nAge: "+age+"\nPhone: "+phone+"\nGender: "+gender+"\n";
        tvHistory.setText(msg);
        tvHistory.append(bmi);
    }
}
