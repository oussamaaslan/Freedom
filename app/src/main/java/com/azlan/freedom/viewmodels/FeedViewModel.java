package com.azlan.freedom.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.azlan.freedom.MyApp;
import com.azlan.freedom.models.Feed;
import com.azlan.freedom.repository.AuthRepository;
import com.azlan.freedom.repository.FeedRepository;
import com.azlan.freedom.tools.Utility;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;

import java.security.PrivateKey;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class FeedViewModel extends AndroidViewModel {

    private final FeedRepository feedRepository;
    private AuthRepository authRepository;

    public  MutableLiveData<String> imageurl=new MutableLiveData<>();
    public MutableLiveData<String> feedId=new MutableLiveData<>();


    public FeedViewModel(@NonNull Application application) {
        super(application);
        feedRepository = FeedRepository.getInstance();
        authRepository= AuthRepository.getInstance();
    }
    public Query getFeedListLiveData() {
       return feedRepository.getFeedFromFirestore();
    }


    public  void addFeed(Bitmap selectedImageuri, String description){

        MutableLiveData<String> feedData=new MutableLiveData<String>();
        feedRepository.addImage(selectedImageuri).observeForever( s -> {
            if(s != null){
                authRepository.firebaseUser().observeForever(user -> {
                    if(user!=null){
                        Feed feed = new Feed(user.getUid(),s,user.getDisplayName(), FieldValue.serverTimestamp().toString(),
                                0,0,description,new Date(System.currentTimeMillis()));
                        feedRepository.addFeed(feed).observeForever(s1 -> {
                            feedId.postValue(s1);
                        });

                    }
                });
            }
            else feedId.postValue(null);
        });

    }


}
