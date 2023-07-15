package com.azlan.freedom.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.azlan.freedom.models.SingInResult;
import com.azlan.freedom.models.User;
import com.azlan.freedom.repository.AuthRepository;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {
        private  AuthRepository authRepository;
        public MutableLiveData<SingInResult> authenticationResult = new MutableLiveData<>();
        public MutableLiveData<User> createdUserLiveData = new MutableLiveData<>();

        public MutableLiveData<FirebaseUser> firebaseUserLiveData=new MutableLiveData<>();


    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository=new AuthRepository();
    }


   public  void signUpWithEmailPassword(String name, String email, String password) {
        authenticationResult = authRepository.signUpWithEmailPassword(name, email, password);
    }

    public void signInWithCredentials(AuthCredential googleAuthCredential) {
                authenticationResult = authRepository.signInWithCredentials(googleAuthCredential);
            }

        public void signInWithEmailPassword(String email, String password) {
            authenticationResult = authRepository.signInWithEmailPassword(email, password);
        }



        public void createUser(User authenticatedUser) {
            createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser);
        }

        public void getFirbaseUser(){
            firebaseUserLiveData=authRepository.firebaseUser();
        }
}


