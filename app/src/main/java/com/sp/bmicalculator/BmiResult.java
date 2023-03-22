package com.sp.bmicalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BmiResult extends AppCompatActivity {

    TextView tvResult, tvUnderweight, tvNormal, tvOverweight, tvObese;
    Button btnBack, btnShare, btnSave;
    SharedPreferences sp1;
    private PieChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bmi_result);
        setContentView(R.layout.activity_bmi_result);
        tvResult = findViewById(R.id.tvResult);
        tvUnderweight = findViewById(R.id.tvUnderweight);
        tvNormal = findViewById(R.id.tvNormal);
        tvOverweight = findViewById(R.id.tvOverweight);
        tvObese = findViewById(R.id.tvObese);
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        btnSave = findViewById(R.id.btnSave);
        mChart = findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.WHITE);
        moveOffScreen();
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(true);
        mChart.setDrawHoleEnabled(true);
        mChart.setMaxAngle(180);
        mChart.setRotationAngle(180);
        mChart.setCenterTextOffset(0,-20);
        setData(4,100);
        //mChart.animateY(1000,Easing.EasingOption.EaseInOutCubic);
        mChart.animateY(1000,Easing.EasingOption.EaseInOutCubic);
        sp1 = getSharedPreferences("f1",MODE_PRIVATE);
        final DatabaseHandler db = new DatabaseHandler(this);
        Intent i = getIntent();
        String result = i.getStringExtra("bmi");
        tvResult.append(" "+ result);

        String bmi1 = null;
        final double bmi = Double.parseDouble(result);
        if ( bmi < 18.5)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvUnderweight.setTextColor(getColor(android.R.color.holo_red_dark));
            }
            bmi1 = tvUnderweight.getText().toString();

        }
        else if(bmi >= 18.5 && bmi < 25)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvNormal.setTextColor(getColor(android.R.color.holo_red_dark));
            }
            bmi1 = tvNormal.getText().toString();
        }
        else if(bmi >= 25 && bmi < 30)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvOverweight.setTextColor(getColor(android.R.color.holo_red_dark));
            }
            bmi1 = tvOverweight.getText().toString();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvObese.setTextColor(getColor(android.R.color.holo_red_dark));
            }
            bmi1 = tvObese.getText().toString();
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String name = sp1.getString("name"," ");
        int age = sp1.getInt("age", 0);
        long phone = sp1.getLong("phone",0);
        String gender = sp1.getString("gender","");
        final String msg = "Name: "+name+"\nAge: "+age+"\nPhone: "+phone+"\nGender: "+gender+"\nBMI: "+bmi+"\n"+bmi1;

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,msg);
                startActivity(i);
                Toast.makeText(BmiResult.this, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        final String bmires = Double.toString(bmi);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addDetails(1, bmires);
                //Toast.makeText(BmiResultActivity.this, "Data is Saved", Toast.LENGTH_SHORT).show();
            }
        });




    }
    String[] countries = new String[]{"Underweight","Normal","Overweight","Obese"};
    private void setData(int count, int range)
    {
        ArrayList<PieEntry> values = new ArrayList<>();
        for (int i=0; i<count;i++)
        {
            //float val = (float)((Math.random()*range)+range/4);
            values.add(new PieEntry(1, countries[i]));
            //values.add(new PieEntry(countries[i]);

        }
        PieDataSet dataSet =  new PieDataSet(values,"Partner");
        dataSet.setSelectionShift(4f);
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.invalidate();

    }
    private void moveOffScreen()
    {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int offset = (int)(height*0.9);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mChart.getLayoutParams();
        params.setMargins(0,0,0,-offset);
        mChart.setLayoutParams(params);


    }
}
