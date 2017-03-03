package com.opiumfive.noncha.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.opiumfive.noncha.R;

public class SplashActivity extends BaseActivity {
    private static final long SPLASH_SCREEN_TIMER = 2000L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, RoomsActivity.class));
                finish();
            }
        }, SPLASH_SCREEN_TIMER);
    }
}
