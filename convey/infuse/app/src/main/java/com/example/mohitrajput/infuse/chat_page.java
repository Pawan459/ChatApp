package com.example.mohitrajput.infuse;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

public class chat_page extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter((getSupportFragmentManager()));

        //Add fragment here

        adapter.AddFragment(new FragmentCall(),"Chat");
        adapter.AddFragment(new FragmentContact(),"Active");
        adapter.AddFragment(new FragmentFav(),"Chat Dialog");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        /*
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_call);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_group);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_star);


        remove shadow from the action bar
        */

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);


    }
}
