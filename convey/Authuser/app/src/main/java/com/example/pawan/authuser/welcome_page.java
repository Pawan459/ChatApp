package com.example.pawan.authuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class welcome_page extends AppCompatActivity {
    ImageView iv;
    TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        iv = (ImageView) findViewById(R.id.logo);
        tv1 = (TextView) findViewById(R.id.app_name);
        tv2 = (TextView) findViewById(R.id.des);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.splash);
        tv1.startAnimation(myanim);
        tv2.startAnimation(myanim);
        iv.startAnimation(myanim);
        final Intent i = new Intent(this, Description_page.class);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();

                }
            }
        };
        timer.start();
    }
}
