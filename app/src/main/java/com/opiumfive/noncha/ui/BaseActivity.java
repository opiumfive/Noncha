package com.opiumfive.noncha.ui;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void playUIanimation(@Nullable View view1, @Nullable View view2) {
        PropertyAction action1 = null;
        PropertyAction action2 = null;
        if (view1 != null) {
            action1 = PropertyAction.newPropertyAction(view1)
                    .translationY(500)
                    .duration(550)
                    .alpha(0f)
                    .build();
        }
        if (view2 != null) {
            action2 = PropertyAction.newPropertyAction(view2)
                    .duration(550)
                    .alpha(0f)
                    .build();
        }
        Player player = Player.init();
        if (action1 != null) {
            player.animate(action1);
            if (action2 != null) player.then();
        }
        if (action2 != null) player.animate(action2);
        player.play();
    }

}
