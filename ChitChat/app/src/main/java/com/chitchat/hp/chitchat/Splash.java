package com.chitchat.hp.chitchat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class Splash extends AppCompatActivity {
     private  static  int SPLASH_TIME_OUT=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar );
//        getSupportActionBar().setTitle("jnfjd");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(Splash.this,Login.class);
                startActivity(i);
            }
        },SPLASH_TIME_OUT);


    }
}
