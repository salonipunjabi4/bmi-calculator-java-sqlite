package com.sp.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class BmiData extends AppCompatActivity
      implements GoogleApiClient.OnConnectionFailedListener,
      GoogleApiClient.ConnectionCallbacks
{
    TextView tvWelcome,tvHeight,tvFeet,tvInch,tvLocation,tvWeather;
    EditText etWeight;
    Spinner spnFeet,spnInch;
    SharedPreferences sp;
    Location loc;
    GoogleApiClient gac;
    Button btnCalculate, btnHistory;
    Handler handler;
    Runnable runnable;
    Timer timer;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_data);


        tvWeather = findViewById(R.id.tvWeather);
        tvLocation = findViewById(R.id.tvLocation);
        tvHeight = findViewById(R.id.tvHeight);
        tvFeet = findViewById(R.id.tvFeet);
        tvInch = findViewById(R.id.tvInch);
        tvWelcome = findViewById(R.id.tvWelcome);
        spnFeet = findViewById(R.id.spnFeet);
        spnInch = findViewById(R.id.spnInch);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnHistory = findViewById(R.id.btnHistory);
        etWeight = findViewById(R.id.etWeight);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac = builder.build();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                timer.cancel();

            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },2000,1000);

        sp = getSharedPreferences("f1", MODE_PRIVATE);

        String n = sp.getString("name", "");
        tvWelcome.setText("Welcome   " + n);
        ArrayList<Integer> feet = new ArrayList<>();
        feet.add(1);
        feet.add(2);
        feet.add(3);
        feet.add(4);
        feet.add(5);
        feet.add(6);
        feet.add(7);
        feet.add(8);
        ArrayAdapter<Integer> feetadapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, feet);
        spnFeet.setAdapter(feetadapter);
        spnFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String feet = parent.getItemAtPosition(position).toString();
                //Toast.makeText(BmiData.this, "feet" + feet, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(BmiData.this, "select something", Toast.LENGTH_SHORT).show();

            }
        });



        ArrayList<Integer> inch = new ArrayList<>();
        inch.add(1);
        inch.add(2);
        inch.add(3);
        inch.add(4);
        inch.add(5);
        inch.add(6);
        inch.add(7);
        inch.add(8);
        inch.add(9);
        inch.add(10);
        ArrayAdapter<Integer> inchadapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, inch);
        spnInch.setAdapter(inchadapter);
        spnInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String inch = parent.getItemAtPosition(position).toString();
                //Toast.makeText(BmiData.this, "inch" + inch, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(BmiData.this, "select something", Toast.LENGTH_SHORT).show();

            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = etWeight.getText().toString();
                String feet = spnFeet.getSelectedItem().toString();
                String inch = spnInch.getSelectedItem().toString();

                if (weight.length() == 0) {
                    etWeight.setError("Enter Weight");
                    etWeight.requestFocus();
                    return;
                }

                double meter = (Integer.parseInt(feet) * 0.3048) + (Integer.parseInt(inch) * 0.0254);
                double weight1 = Double.parseDouble(weight);
                double bmi = weight1 / (meter * meter);

                String result = Double.toString(bmi);


                Intent i1 = new Intent(BmiData.this,BmiResult.class);
                i1.putExtra("bmi",result);
                startActivity(i1);
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BmiData.this,ViewHistory.class);
                startActivity(i);
            }
        });






    }



    @Override
    protected void onStart() {
        super.onStart();
        if(gac!=null)
            gac.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(gac!=null)
            gac.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.About) {
            Toast.makeText(this, "Developer : Saloni", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.Website) {
            Intent c = new Intent(Intent.ACTION_VIEW);
            c.setData(Uri.parse("http://" + "www.google.co.in"));
            startActivity(c);
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Exit");
        alertDialog.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        loc=LocationServices.FusedLocationApi.getLastLocation(gac);
        Geocoder g=new Geocoder(BmiData.this,Locale.ENGLISH);
        if(loc!=null) {
            double lat=loc.getLatitude();
            double lon=loc.getLongitude();
            try {
                List<Address> city=g.getFromLocation(lat,lon,1);
                Address address=city.get(0);
                String add=address.getLocality();
                tvLocation.setText(add);
                // add1=add;
                MyTask t1=new MyTask();
                String web="http://api.openweathermap.org/data/2.5/weather?units=metric";
                String que="&q="+add;
                String api="&appid=e8b580274cc15be731a33a26793371d1";
                //String api="&appid=1a3ca7900eebc64a517a486e8c5c75c0";
                String info=web+que+api;
                t1.execute(info);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else {
            Toast.makeText(this, "Enable GPS/come in open area", Toast.LENGTH_SHORT).show();

        }



    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();

    }
    class MyTask extends AsyncTask<String,Void,Double> {
        double temp;

        @Override
        protected Double doInBackground(String... strings) {
            String json = "";
            String line = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    json = json + line + "\n";
                }
                JSONObject o = new JSONObject(json);
                JSONObject p = o.getJSONObject("main");
                temp = p.getDouble("temp");

            } catch (Exception e) {
                Log.d("KK123", "" + e);
            }
            return temp;

        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            tvWeather.setText("Temp: " + aDouble);
        }
    }
}
