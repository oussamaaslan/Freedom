package com.azlan.freedom.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.azlan.freedom.models.SingInResult;
import com.azlan.freedom.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AuthRepository {

        private static volatile AuthRepository instance;
        private  FirebaseAuth firebaseAuth;
        private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        private final CollectionReference usersRef = rootRef.collection("users");

        // private constructor : singleton access
        public AuthRepository() {}

        public static AuthRepository getInstance() {
            if (instance == null) {
                instance = new AuthRepository();
            }
            return instance;
        }

        public MutableLiveData<SingInResult> signInWithCredentials(AuthCredential authCredential) {
            firebaseAuth= FirebaseAuth.getInstance();
            MutableLiveData<SingInResult> authenticationResultMutableLiveData = new MutableLiveData<>();
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(authTask -> {
                if (authTask.isSuccessful()) {
                    boolean isNewUser = Objects.requireNonNull(authTask.getResult().getAdditionalUserInfo()).isNewUser();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        String uid = firebaseUser.getUid();
                        String name = firebaseUser.getDisplayName();
                        String email = firebaseUser.getEmail();
                        User user = new User(uid, name, email,firebaseUser.getProviderId(),firebaseUser.getPhotoUrl().toString());
                        user.isNew = isNewUser;
                        authenticationResultMutableLiveData.postValue(new SingInResult(user));
                    }
                } else {
                    authenticationResultMutableLiveData.postValue(new SingInResult(Objects.requireNonNull(authTask.getException()).getMessage()));
                    logErrorMessage(Objects.requireNonNull(authTask.getException()).getMessage());
                }
            });
            return authenticationResultMutableLiveData;
        }

        public MutableLiveData<SingInResult> signInWithEmailPassword(String email, String password) {
            firebaseAuth= FirebaseAuth.getInstance();
            MutableLiveData<SingInResult> authenticatedUserMutableLiveData = new MutableLiveData<>();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
                if (authTask.isSuccessful()) {
                    boolean isNewUser = Objects.requireNonNull(authTask.getResult().getAdditionalUserInfo()).isNewUser();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(),firebaseUser.getProviderId(),
                                (firebaseUser.getPhotoUrl()!=null)?firebaseUser.getPhotoUrl().toString():null);
                        user.isNew = isNewUser;
                        authenticatedUserMutableLiveData.postValue(new SingInResult(user));
                    }
                } else {
                    authenticatedUserMutableLiveData.postValue(new SingInResult(Objects.requireNonNull(authTask.getException()).getMessage()));
                    logErrorMessage(Objects.requireNonNull(authTask.getException()).getMessage());
                }
            });
            return authenticatedUserMutableLiveData;
        }

        public MutableLiveData<SingInResult> signUpWithEmailPassword(String name, String email, String password) {
            firebaseAuth= FirebaseAuth.getInstance();
            MutableLiveData<SingInResult> authenticationResultMutableLiveData = new MutableLiveData<>();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
                if (authTask.isSuccessful()) {
                    boolean isNewUser = Objects.requireNonNull(authTask.getResult().getAdditionalUserInfo()).isNewUser();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build());

                        User user = new User(firebaseUser.getUid(), name, firebaseUser.getEmail(),"Email",
                                (firebaseUser.getPhotoUrl()!=null)?firebaseUser.getPhotoUrl().toString():null);
                        user.isNew = isNewUser;
                        authenticationResultMutableLiveData.postValue(new SingInResult(user));
                    }
                } else {
                    authenticationResultMutableLiveData.postValue(new SingInResult(Objects.requireNonNull(authTask.getException()).getMessage()));
                    logErrorMessage(Objects.requireNonNull(authTask.getException()).getMessage());
                }
            });
            return authenticationResultMutableLiveData;
        }

        public MutableLiveData<User> createUserInFirestoreIfNotExists(User authenticatedUser) {
            MutableLiveData<User> newUserMutableLiveData = new MutableLiveData<>();
            DocumentReference uidRef = usersRef.document(authenticatedUser.uid);
            uidRef.get().addOnCompleteListener(uidTask -> {
                if (uidTask.isSuccessful()) {
                    DocumentSnapshot document = uidTask.getResult();
                    if (!document.exists()) {
                        uidRef.set(authenticatedUser).addOnCompleteListener(userCreationTask -> {
                            if (userCreationTask.isSuccessful()) {
                                authenticatedUser.isCreated = true;
                                newUserMutableLiveData.postValue(authenticatedUser);
                            } else {
                                logErrorMessage(Objects.requireNonNull(userCreationTask.getException()).getMessage());
                            }
                        });
                    } else {
                        newUserMutableLiveData.postValue(authenticatedUser);
                    }
                } else {
                    logErrorMessage(Objects.requireNonNull(uidTask.getException()).getMessage());
                }
            });
            return newUserMutableLiveData;
        }

        public MutableLiveData<FirebaseUser> firebaseUser(){
            firebaseAuth= FirebaseAuth.getInstance();
            MutableLiveData<FirebaseUser> userMutableLiveData = new MutableLiveData<>();
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if(user!=null){
                userMutableLiveData.postValue(user);
            }
            else userMutableLiveData.postValue(null);
            return userMutableLiveData;
        }

    public MutableLiveData<FirebaseAuth> firebaseAuth(){
        FirebaseAuth mfirebaseAuth = FirebaseAuth.getInstance();
        MutableLiveData<FirebaseAuth> authMutableLiveData = new MutableLiveData<>();
        FirebaseAuth auth=mfirebaseAuth;
        if(auth!=null){
            authMutableLiveData.postValue(auth);
        }
        else authMutableLiveData.postValue(null);
        return authMutableLiveData;
    }

    public MutableLiveData<String> firebaseToken(){
        firebaseAuth= FirebaseAuth.getInstance();
        MutableLiveData<String> tokenMutableLiveData = new MutableLiveData<>();
        if(firebaseAuth.getCurrentUser()!=null){
             firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        // The token has been successfully retrieved
                         tokenMutableLiveData.postValue(task.getResult().getToken());
                        // Use the token as needed (e.g., send it to your server)

                    } else {
                        // Failed to get the token
                        tokenMutableLiveData.postValue(null);
                        Log.w("FCM Token", "Error fetching FCM token: " + task.getException());
                    }
                }
            });

        }
        else tokenMutableLiveData.postValue(null);
        return tokenMutableLiveData;
    }


    private void logErrorMessage(String errorMessage) {
            Log.d("AuthRepository", errorMessage);
        }

    public MutableLiveData<String> firebaseUserUid() {
        firebaseAuth= FirebaseAuth.getInstance();
        MutableLiveData<String> data= new MutableLiveData<String>();
          data.postValue(firebaseAuth.getUid());
          return data;
    }
}
