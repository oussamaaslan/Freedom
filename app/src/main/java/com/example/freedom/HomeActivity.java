package com.example.freedom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.freedom.databinding.ActivityHomeBinding;
import com.example.freedom.tools.Connection;

public class HomeActivity extends AppCompatActivity {


    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
            binding.txtuser.setText(Connection.getInstance().getUser().getIdToken(false).getResult().getToken());


    }
}