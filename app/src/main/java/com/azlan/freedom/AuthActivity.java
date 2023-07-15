package com.azlan.freedom;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.azlan.freedom.databinding.ActivityLoginBinding;
import com.azlan.freedom.models.User;
import com.azlan.freedom.tools.Utility;
import com.azlan.freedom.viewmodels.AuthViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class AuthActivity extends AppCompatActivity implements LoginFragment.Listener,registrationFragment.Listener{
    private ActivityLoginBinding binding;
    AuthViewModel authViewModel;
    private GoogleSignInClient googleSignInClient;
    private BeginSignInRequest oneTapSignInRequest;
    private SignInClient oneTapClient;
    NavController navController;

    AlertDialog alertDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //create viewmodel for loginactivity
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
         navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_auth);


        binding.txtsingin.setOnClickListener(v ->{
            if(((TextView) v).getText().toString().equals(getString(R.string.singin))){

                navController.navigate(R.id.action_call_registration);
                ( (TextView) v ).setText(R.string.singup);
            }
            else{
                navController.navigateUp();
                ( (TextView) v ).setText(R.string.singin);
            }


        });




        handleGoogleSignIn();
        handleFacebookLogin();
    }




    private void handleSingUpWithEmailPassword(String username,String email, String password){
        startloginUi(false);
        authViewModel.signUpWithEmailPassword(username,email,password);
        authViewModel.authenticationResult.observe(this, authenticationResult -> {
            String errorCode = authenticationResult.getErrorMessage();
            if (authenticationResult.getErrorMessage() != null) {
                showLoginFailed(authenticationResult.getErrorMessage());
                startloginUi(true);
                return;
            }
            User authenticatedUser = authenticationResult.getSuccess();
            if (authenticatedUser.isNew) {
                createNewUser(authenticatedUser);
                updateUiWithUser(authenticatedUser);
                return;
            } else {
                updateUiWithUser(authenticatedUser);
            }
        });
    }
    private void handleSinginWithEmailPassword(String email, String password){
            startloginUi(false);
            authViewModel.signInWithEmailPassword(email,password);
            authViewModel.authenticationResult.observe(this, authenticationResult -> {
                String errorCode = authenticationResult.getErrorMessage();
                if (authenticationResult.getErrorMessage() != null) {
                    showLoginFailed(authenticationResult.getErrorMessage());
                    startloginUi(true);
                    return;
                }
                User authenticatedUser = authenticationResult.getSuccess();
                if (authenticatedUser.isNew) {
                    createNewUser(authenticatedUser);
                    updateUiWithUser(authenticatedUser);
                    return;
                } else {
                    updateUiWithUser(authenticatedUser);
                }
            });
    }

    private void handleGoogleSignIn(){
        ActivityResultLauncher<Intent> googleAuthWithDeviceLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                         Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                            if (googleSignInAccount != null) {
                                String googleTokenId = googleSignInAccount.getIdToken();
                                AuthCredential authCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
                                signInWithAuthCredential(authCredential);
                            }
                        } catch (ApiException e) {
                            showLoginFailed(e.getMessage());
                        }
                    }
                });


        ActivityResultLauncher<IntentSenderRequest> oneTapLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        try {
                            SignInCredential signInCredential = oneTapClient.getSignInCredentialFromIntent(data);
                            String idToken = signInCredential.getGoogleIdToken();
                            if(idToken != null) {
                                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                                signInWithAuthCredential(credential);
                            } else {
                                Log.d("idToken", "is null");
                            }
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        oneTapClient = Identity.getSignInClient(this);
        oneTapSignInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        ImageButton googleLoginButton = binding.btngoogle;
        googleLoginButton.setOnClickListener(v -> {
            startloginUi(false);
            oneTapClient.beginSignIn(oneTapSignInRequest).
                    addOnSuccessListener(this, result -> {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest
                                .Builder(result.getPendingIntent().getIntentSender())
                                .build();
                        oneTapLauncher.launch(intentSenderRequest); }).
                    addOnFailureListener(this, e -> {
                        //TODO: check more cases
                        startloginUi(true);
                        //this may be called if the device is not authenticated with any google account
                        //if this happens, launch an activity that will authenticate a google account with the device
                        Intent signInIntent = googleSignInClient.getSignInIntent();
                        googleAuthWithDeviceLauncher.launch(signInIntent);
                    });
        });
    }

    private void handleFacebookLogin() {
        CallbackManager callbackManager = CallbackManager.Factory.create();
        ImageButton facebookLoginButton = binding.btnfacebook;
        facebookLoginButton.setOnClickListener(v -> {
            startloginUi(false);
            LoginManager loginManager = LoginManager.getInstance();
            loginManager.logInWithReadPermissions(this, callbackManager, Arrays.asList("email", "public_profile"));
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AuthCredential authCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                    signInWithAuthCredential(authCredential);
                }

                @Override
                public void onCancel() {
                    //TODO(Michal): better onCancel handling
                    startloginUi(true);
                }

                @Override
                public void onError(@NonNull FacebookException e) {
                    //TODO(Michal): better onError handling
                    showLoginFailed(e.getMessage());
                }
            });
        });
    }


    //General functions for signing in / up
    private void signInWithAuthCredential(AuthCredential authCredential) {
        authViewModel.signInWithCredentials(authCredential);
        authViewModel.authenticationResult.observe(this, authenticationResult -> {
            if(authenticationResult.getErrorMessage() != null ) {
                showLoginFailed(authenticationResult.getErrorMessage());
                startloginUi(true);
                return;

            }

            User authenticatedUser = authenticationResult.getSuccess();
            if (authenticatedUser.isNew) {
                createNewUser(authenticatedUser);
            } else {
               updateUiWithUser(authenticatedUser);
            }
        });
    }
    private void createNewUser(User authenticatedUser) {
        authViewModel.createUser(authenticatedUser);
        authViewModel.createdUserLiveData.observe(this, user -> {
                updateUiWithUser(user);
        });
    }
    private void updateUiWithUser(User user) {
        startloginUi(true);
        authViewModel.getFirbaseUser();
        authViewModel.firebaseUserLiveData.observe(this,firebaseUser -> {
            try {
                if(firebaseUser!=null)
                    Utility.loginRefresh(this,firebaseUser);
                if(Utility.tokenCheck(this)){
                    if(user.isNew){
                        String welcome = "Welcome! " + user.name;
                        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                    }
                    else {
                        String welcome = "Welcome Back! " + user.name;
                        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                    }

                    Intent ient=new Intent(this,HomeActivity.class);
                    startActivity(ient);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Somethings happened please try again!", Toast.LENGTH_SHORT).show();
                    Log.d("token error","not login yet");
                }

            }
            catch (Exception e){
                Toast.makeText(this, "Somethings happened please try again!", Toast.LENGTH_SHORT).show();
                Log.d("token error",e.getLocalizedMessage());
            }
        });




    }
    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    private void startloginUi(boolean isEnd) {

        if (isEnd) {
            alertDialog.dismiss();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogStyle);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_progress, null);
            builder.setView(dialogView);

             alertDialog = builder.create();
            alertDialog.setCancelable(false); // Prevent dismissing the dialog on outside touch or back button
            alertDialog.show();


        }
    }


    @Override
    public void onSomethingHappened(String email, String password) {
        handleSinginWithEmailPassword(email,password);
    }

    @Override
    public void onSomethingHappened(String username, String email, String password) {
        handleSingUpWithEmailPassword(username,email,password);
    }
}
