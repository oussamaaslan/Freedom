package com.azlan.freedom.tools;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Utility {


    public static void loginRefresh(Context context, FirebaseUser user) {

        SharedPreferences sharedPref = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        if (sharedPref.getString("tokenId", null) != null) {
            String token = user.getIdToken(true).getResult().getToken();
            sharedPref.edit().putString("tokenId", token).apply();


        } else if (user != null) {
            String token = user.getIdToken(false).getResult().getToken();
            sharedPref.edit().putString("tokenId", token).apply();
        }
    }
    public static boolean tokenCheck(Context context) {
        boolean ischeck = false;
        String token = context.getSharedPreferences("application", Context.MODE_PRIVATE).getString("tokenId", null);
        if (token != null) {
            ischeck = true;
        }
        return ischeck;
    }

}
