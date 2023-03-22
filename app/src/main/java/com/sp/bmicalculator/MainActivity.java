package com.sp.bmicalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tvPersonal;
    EditText etName,etAge,etPhone;
    Button btnRegister;
    RadioGroup rgGender;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvPersonal = findViewById(R.id.tvPersonal);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        rgGender = findViewById(R.id.rgGender);
        sp = getSharedPreferences("f1",MODE_PRIVATE);
        //String name = sp.getString("name",null);
        //String bool = sp.contains("name");
        final SharedPreferences.Editor editor = sp.edit();

        if(sp.getBoolean("data",false) == false)
        {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etName.getText().toString();
                    String age = etAge.getText().toString();
                    String phone = etPhone.getText().toString();
                    if (name.length() == 0) {
                        etName.setError("Name is empty");
                        etName.requestFocus();
                        return;
                    }





                    if (age.length() == 0) {
                        etAge.setError("Age is empty");
                        etAge.requestFocus();
                        return;
                    }
                    if (phone.length() == 0) {
                        etPhone.setError("Number is empty");
                        etPhone.requestFocus();
                        return;
                    }


                    if (phone.length() != 10) {
                        Toast.makeText(MainActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                        etPhone.requestFocus();
                        return;
                    }


                    int id=rgGender.getCheckedRadioButtonId();
                    RadioButton rb=findViewById(id);
                    String gender=rb.getText().toString();
                    int age1 = Integer.parseInt(age);
                    long phone1 = Long.parseLong(phone);

                    editor.putString("name",name);
                    editor.putInt("age",age1);
                    editor.putLong("phone",phone1);
                    editor.putString("gender",gender);
                    editor.putBoolean("data",true);
                    editor.commit();
                    Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this,BmiData.class);
                    startActivity(i);
                    finish();

                }
            });
        }
        else
        {
            Intent i = new Intent(MainActivity.this,BmiData.class);
            startActivity(i);
            finish();

        }
    }
}
