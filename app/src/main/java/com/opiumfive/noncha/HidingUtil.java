package com.opiumfive.noncha;


public class HidingUtil {

    static {
        System.loadLibrary("hidingutil");
    }

    public native String get();
}
