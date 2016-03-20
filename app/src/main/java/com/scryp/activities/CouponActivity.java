package com.scryp.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.scryp.R;
import com.scryp.adapter.MyCouponFragmentPagerAdapter;

public class CouponActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        init();
        setupTab();
    }
    private void init(){
        setHeader("My Coupons");
        setFooterClick();
        setCouponSelected();
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
    }

    public void setupTab(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new MyCouponFragmentPagerAdapter(getSupportFragmentManager(),this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        tabLayout.setupWithViewPager(viewPager);
    }


}
