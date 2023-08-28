package com.azlan.freedom.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.azlan.freedom.R;
import com.azlan.freedom.databinding.ActivityAddPhotoBinding;
import com.azlan.freedom.viewmodels.AuthViewModel;
import com.azlan.freedom.viewmodels.FeedViewModel;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddPhotoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 2;
    ActivityAddPhotoBinding binding;

    FeedViewModel feedViewModel;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);

       View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        };
       binding.addPhotoButton.setOnClickListener(clickListener);
        binding.photoPreview.setOnClickListener(clickListener);

        binding.publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addpost(selectedImageUri,binding.descriptionEditText.getText().toString());
            }
        });

    }
    // Open a file picker to select an image from internal storage
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the selected image in onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                 selectedImageUri = data.getData();

                // Load the selected image into the ImageView
                displaySelectedImage(selectedImageUri);
            }
        }
    }
    private void displaySelectedImage(Uri imageUri) {

        Picasso.get()
                .load(imageUri)
                .into(binding.photoPreview);
    }

    private void addpost(Uri image,String desc){
        Bitmap bitmap=createBitmap(image);
        binding.publishButton.setVisibility(View.GONE);
        binding.progressBarFeed.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Posting..... please wait!", Toast.LENGTH_SHORT).show();
        feedViewModel.addFeed(bitmap,desc);
        feedViewModel.feedId.observe(this,s -> {
            if(s==null){
                Toast.makeText(this, "something happened pls train again", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "feed created with successful", Toast.LENGTH_SHORT).show();
                this.finish();
            }
            binding.publishButton.setVisibility(View.VISIBLE);
            binding.progressBarFeed.setVisibility(View.GONE);
        });
    }

    private Bitmap createBitmap(Uri selectedImageUri){
        Bitmap imageBitmap;
        try {
            // Open an InputStream from the image Uri
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

            // Decode the InputStream into a Bitmap
             imageBitmap = BitmapFactory.decodeStream(inputStream);

            // Now you can use the 'imageBitmap' in your app
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            imageBitmap =null;
        }
        return imageBitmap;
    }
}