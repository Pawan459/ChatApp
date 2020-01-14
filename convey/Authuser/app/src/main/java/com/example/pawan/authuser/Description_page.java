package com.example.pawan.authuser;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Description_page extends AppCompatActivity {
    AppCompatButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_page);
        btn = (AppCompatButton) findViewById(R.id.des_next);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        btn.startAnimation(anim);

    }

    public void click (View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.downtoup,R.anim.no_animation);
    }

}
