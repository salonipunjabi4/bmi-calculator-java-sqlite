package com.sp.bmicalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ImageView ivBackground;
    TextView tvBmi;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTitle("BmiSplash");
        ivBackground = findViewById(R.id.ivBackground);
        tvBmi = findViewById(R.id.tvBmi);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.a1);
        ivBackground.startAnimation(animation);
        tvBmi.startAnimation(animation);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent a = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(a);
                finish();

            }
        }).start();

    }
}
