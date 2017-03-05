package com.opiumfive.noncha.managers;


import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {

    private static DatabaseManager INSTANCE = new DatabaseManager();

    private FirebaseDatabase mDatabase;

    private DatabaseManager() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase getDatabase() {
        return mDatabase;
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }
}
