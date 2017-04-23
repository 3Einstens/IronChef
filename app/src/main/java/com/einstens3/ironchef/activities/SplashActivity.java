package com.einstens3.ironchef.activities;

/**
 * Created by raprasad on 3/19/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.einstens3.ironchef.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    @BindView(R.id.imgLogo)
    ImageView imgFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
