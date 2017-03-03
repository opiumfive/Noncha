package com.opiumfive.noncha;


import com.google.firebase.auth.FirebaseAuth;

public class AuthManager {

    private static AuthManager INSTANCE = new AuthManager();

    private FirebaseAuth mAuth;

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    private AuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static AuthManager getInstance() {
        return INSTANCE;
    }
}
