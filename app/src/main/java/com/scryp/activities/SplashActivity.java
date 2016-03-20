package com.scryp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.scryp.R;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends BaseActivity {

    private long splashDelay = 3000;
    Timer timer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = null;
                if(!Utils.getBooleanFromPref(SplashActivity.this, Constant.PREF_DONT_SHOW, false))
                    i = new Intent(SplashActivity.this, TutorialActivity.class);
                else
                    i = new Intent(SplashActivity.this, HomeActivity.class);
                finish();
                startActivity(i);
            }
        };

        timer = new Timer();
        timer.schedule(task, splashDelay);
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
    }
}
