package com.opiumfive.noncha.model;



public class Room {

    public String mId = null;
    public String mName = null;
    public boolean mPublic = true;
    public String mCode = null;

    public Room(String name, String code) {
        mName = name;
        if (code != null && code.isEmpty()) {
            mCode = null;
        } else {
            mCode = code;
        }
        mPublic = (mCode == null);
    }

    public Room() {
    }
}
