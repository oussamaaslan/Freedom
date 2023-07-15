package com.azlan.freedom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.azlan.freedom.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {


    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}