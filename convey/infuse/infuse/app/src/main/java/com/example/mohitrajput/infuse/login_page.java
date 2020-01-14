package com.example.mohitrajput.infuse;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import static com.example.mohitrajput.infuse.R.color.colorAccent;
import static com.example.mohitrajput.infuse.R.color.colorPrimaryDark;

public class login_page extends AppCompatActivity {

    private FloatingActionButton fab;
    private RelativeLayout login_page;
    private RelativeLayout layoutContent;
    private RelativeLayout layoutButtons;
    //private MaterialButton nextbtn;
    private boolean isOpen = false;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        login_page = (RelativeLayout) findViewById(R.id.login_page);
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
        layoutButtons = (RelativeLayout) findViewById(R.id.layoutButtons);
        txt=(TextView)findViewById(R.id.txtSignup);

        MaterialButton nextbtn = (MaterialButton) findViewById(R.id.next_button);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewMenu();
            }
        });
    }


    public void btnClickAct(View v){
    Intent i = new Intent(this, chat_page.class);
    startActivity(i);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void viewMenu (){

        if(!isOpen){
            txt.setVisibility(View.INVISIBLE);
            int x = layoutContent.getRight();
            int y = layoutContent.getBottom();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(login_page.getWidth(), login_page.getHeight());

            fab.getResources().getColorStateList(colorPrimaryDark);
            //fab.setBackgroundTintList(getResources().getColorStateList(colorAccent), null)));
            fab.setImageResource(R.drawable.ic_close_black_24dp);

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);

            layoutButtons.setVisibility(View.VISIBLE);
            anim.start();

            isOpen = true;
        }else{
            txt.setVisibility(View.VISIBLE);
            int x = layoutContent.getRight();
            int y = layoutContent.getBottom();

            int startRadius = Math.max(layoutContent.getWidth(), layoutContent.getHeight());
            int endRadius = 0;

            //fab.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),android.R.color.white, null)));
            fab.setImageResource(R.drawable.ic_add_black_24dp);

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layoutButtons.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationCancel(Animator animator) {


                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }


            });
            anim.start();
            isOpen = false;
        }
    }
}
