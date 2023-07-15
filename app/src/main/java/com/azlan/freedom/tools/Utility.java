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
//    private SignInClient oneTapClient;
//    private static FirebaseAuth mAuth;
//    CallbackManager callbackManager;
//    private static Utility connection = null;
//
//    public static Utility getInstance() {
//        if (connection == null) {
//            connection = new Utility();
//
//            mAuth = FirebaseAuth.getInstance();
//
//        }
//        return connection;
//    }
//
////    public boolean checkLogin() {
////        FirebaseUser currentUser = mAuth.getCurrentUser();
////        return currentUser != null;
////    }
//
//    public void createUser(String email, String password, Context context, Runnable runnableEndSingIn) {
//
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("LOG", "create User:success");
//                    updateUI(context, runnableEndSingIn);
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                    runnableEndSingIn.run();
//                    Log.w("LOG", "signInWithEmail:failure", task.getException());
//
//                    //updateUI(null);
//                }
//            }
//        });
//    }
//
//    public void login(String email, String password, Context context, Runnable runnableEndLogin) {
//        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("LOG", "signInWithEmail:success");
//                    updateUI(context, runnableEndLogin);
//
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                    runnableEndLogin.run();
//                    Log.w("LOG", "signInWithEmail:failure", task.getException());
//
//                    //updateUI(null);
//                }
//            }
//        });
//    }
//
//    public FirebaseUser getUser() {
//        return mAuth.getCurrentUser();
//    }
//




//
//    public void singinwithGoogle(Activity context) {
//
//        int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
//        oneTapClient = Identity.getSignInClient(context);
//        // Your server's client ID, not your Android client ID.
//        // Only show accounts previously used to sign in.
//        // Automatically sign in when exactly one credential is retrieved.
//        BeginSignInRequest signInRequest = BeginSignInRequest.builder().
//                setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder().setSupported(true).build()).
//                setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder().
//                        setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(context.getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(false).build())
//                // Automatically sign in when exactly one credential is retrieved.
//                .setAutoSelectEnabled(true).build();
//
//        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(context, new OnSuccessListener<BeginSignInResult>() {
//            @Override
//            public void onSuccess(BeginSignInResult result) {
//                try {
//                    context.startIntentSenderForResult(result.getPendingIntent().getIntentSender(), REQ_ONE_TAP, null, 0, 0, 0);
//                } catch (IntentSender.SendIntentException e) {
//                    Log.e("D", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
//                }
//            }
//        }).addOnFailureListener(context, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // No saved credentials found. Launch the One Tap sign-up flow, or
//                // do nothing and continue presenting the signed-out UI.
//                Log.d("D", e.getLocalizedMessage());
//
//            }
//        });
//
//    }
//
//
//    public void onGoogleSingInResult(Context context, Intent data, Runnable runnableEndGooglesingin) throws ApiException {
//        SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
//        String idToken = googleCredential.getGoogleIdToken();
//        if (idToken != null) {
//            // Got an ID token from Google. Use it to authenticate
//            // with Firebase.
//            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
//            mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d("TAG", "signInWithGoogleCredential:success");
//                        updateUI(context, runnableEndGooglesingin);
//
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.w("TAG", "signInWithGoogleCredential:failure", task.getException());
//
//                    }
//                }
//            });
//        }
//    }
//
//    public CallbackManager getfacebookloginresult(){
//        return callbackManager;
//    }
//    public void singinwithFacebook(Context context,Runnable runnableEndFacebookLogin){
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile"));
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                        Log.d("TAG", "signInWithFacebookCredential:success");
//                        handleFacebookAccessToken(loginResult.getAccessToken(),context,runnableEndFacebookLogin);
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        Log.d("TAG", "signInWithFacebookCredential:cancel");
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        Log.d("TAG", "signInWithFacebookCredential:failure: "+exception.getLocalizedMessage());
//                    }
//                });
//    }
//    private void handleFacebookAccessToken(AccessToken token,Context context,Runnable runnableEndFacebookLogin) {
//        Log.d("TAG", "handleFacebookAccessToken:" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("TAG", "signInWithCredential:success");
//                            updateUI(context,runnableEndFacebookLogin);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//
//                          //  updateUI(null);
//                        }
//                    }
//                });
//    }
//    private void updateUI(Context context, Runnable runnableEndUI) {
//        runnableEndUI.run();
//        loginRefresh(context);
//        Intent intent = new Intent(context, HomeActivity.class);
//        context.startActivity(intent);
//        ((Activity) context).finish();
//    }

}
