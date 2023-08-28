package com.azlan.freedom.tools;


import android.content.Context;
import android.content.SharedPreferences;

import com.azlan.freedom.MyApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Utility {


    public static void loginRefresh(Context context,String token) {
        SharedPreferences sharedPref = context.getSharedPreferences("application", Context.MODE_PRIVATE);
            sharedPref.edit().putString("tokenId", token).apply();
    }
    public static boolean tokenCheck(Context context) {
        boolean ischeck = false;
        String token = context.getSharedPreferences("application", Context.MODE_PRIVATE).getString("tokenId", null);
        if (token != null) {
            ischeck = true;
        }
        return ischeck;
    }
     public static   void tokenLogout(Context context){
         context.getSharedPreferences("application", Context.MODE_PRIVATE).edit().putString("tokenId",null).apply();
    }
    public  static String token(){
        return MyApp.getAppContext().getSharedPreferences("application", Context.MODE_PRIVATE).getString("tokenId", null);

    }
}
