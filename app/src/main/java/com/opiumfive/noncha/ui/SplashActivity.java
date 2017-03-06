package com.opiumfive.noncha.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;
import com.opiumfive.noncha.R;

public class SplashActivity extends BaseActivity {

    private static final long SPLASH_SCREEN_TIMER = 2000L;

    private View mSplashImageView;
    private View mSplashTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashImageView = findViewById(R.id.splash_image);
        mSplashTextView = findViewById(R.id.splash_text);
        playUIanimation();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, RoomsActivity.class));
                finish();
            }
        }, SPLASH_SCREEN_TIMER);
    }

    private void playUIanimation() {
        final PropertyAction splashImageAction = PropertyAction.newPropertyAction(mSplashImageView).
                scaleX(0).
                scaleY(0).
                duration(750).
                interpolator(new AccelerateDecelerateInterpolator()).
                build();
        final PropertyAction textViewAction = PropertyAction.newPropertyAction(mSplashTextView).
                translationY(500).
                duration(550).
                alpha(0f).
                build();
        Player.init().
                animate(splashImageAction).
                then().
                animate(textViewAction).
                play();
    }
}
