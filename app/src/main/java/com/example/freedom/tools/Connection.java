package com.example.freedom.tools;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.freedom.HomeActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

import java.lang.reflect.Executable;
import java.util.function.Function;

public class Connection   {


    private static FirebaseAuth mAuth;
    private static FirebaseUser user;

    private static Connection connection = null;

    public static Connection getInstance() {
        if (connection == null) {
            connection = new Connection();

            mAuth = FirebaseAuth.getInstance();

        }
        return connection;
    }

    public boolean checkLogin() {


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
           //updateUi()
            // reload();
            return true;
        }
        else
            return false;
    }

    public void createUser(String email, String password, Context context, Runnable runnableEndLogin) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOG", "create User:success");
                            loginRefresh(context);
                            // updateUI(user);
                            Intent intent=new Intent(context, HomeActivity.class);
                            runnableEndLogin.run();
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            runnableEndLogin.run();
                            Log.w("LOG", "signInWithEmail:failure", task.getException());

                            //updateUI(null);
                        }
                    }
                });
    }

    public void login(String email,String password,Context context, Runnable runnableEndLogin){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOG", "signInWithEmail:success");
                            loginRefresh(context);
                           // updateUI(user);
                            Intent intent=new Intent(context, HomeActivity.class);
                            runnableEndLogin.run();
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            runnableEndLogin.run();
                            Log.w("LOG", "signInWithEmail:failure", task.getException());

                            //updateUI(null);
                        }
                    }
                });
    }

    public FirebaseUser getUser(){
        return mAuth.getCurrentUser();
    }

    public  boolean tokenCheck(Context context){
        boolean ischeck=false;
        String token=context.getSharedPreferences("application", Context.MODE_PRIVATE).getString("tokenId",null);
        if(token!=null){
            ischeck=true;
        }
        return ischeck;
    }
   public void loginRefresh(Context context) {

      SharedPreferences sharedPref = context.getSharedPreferences("application", Context.MODE_PRIVATE);
      if(sharedPref.getString("tokenId",null)!=null){
          String token=getUser().getIdToken(true).getResult().getToken();
        sharedPref.edit().putString("tokenId",token).apply();


      }
      else if(getUser()!=null) {
          String token=getUser().getIdToken(false).getResult().getToken();
          sharedPref.edit().putString("tokenId",token).apply();
      }

   }
}
