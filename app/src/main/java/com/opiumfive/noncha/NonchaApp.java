package com.opiumfive.noncha;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class NonchaApp extends Application {

    public final static String FONT_PATH_DEFAULT = "fonts/font.ttf";

    private static NonchaApp mInstance;

    public NonchaApp() {
        mInstance = this;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(FONT_PATH_DEFAULT)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
