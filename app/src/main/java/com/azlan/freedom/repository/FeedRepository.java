package com.azlan.freedom.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.azlan.freedom.models.Feed;
import com.azlan.freedom.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedRepository {
    private static volatile FeedRepository instance;
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;


    public FeedRepository() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static FeedRepository getInstance() {
        if (instance == null) {
            instance = new FeedRepository();
        }
        return instance;
    }

    public Query getFeedFromFirestore() {
        Query query = db.collection("feeds").orderBy("publish_date", Query.Direction.DESCENDING);
        if (query != null) {
            return query;
        } else {

            logErrorMessage("Could not find");
            return null;
        }
    }

    private void logErrorMessage(String errorMessage) {
        Log.d("Feed Repository.", errorMessage);
    }


    public MutableLiveData<String> addImage(Bitmap selectedImageUri) {
        MutableLiveData<String> imagename = new MutableLiveData<>();
        long timestamp = System.currentTimeMillis();
        String uniqueId = Long.toString(timestamp);
        // Get a reference to the Firebase Storage instance and specify the storage location
        StorageReference storageRef = storage.getReference().child("freedom." + uniqueId);// Load your image into a Bitmap (Replace with your own logic)
        // Assuming you have the image Uri stored in a variable named 'imageUri'
        // Convert Bitmap to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImageUri.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Upload the image data to Firebase Storage
        storageRef.putBytes(imageData)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload successful
                    imagename.postValue(storageRef.getName());
                })
                .addOnFailureListener(exception -> {
                    // Handle the upload failure
                    imagename.postValue(null);
                });

        return imagename;
    }

    public MutableLiveData<String> addFeed(Feed feed){
        MutableLiveData<String> data = new MutableLiveData<>();
        db.collection("feeds").add(feed)
                .addOnSuccessListener(documentReference -> {
                    // Post uploaded successfully
                   data.postValue(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Handle upload failure
                    data.postValue(e.getMessage());
                });
        return  data;
    }
}