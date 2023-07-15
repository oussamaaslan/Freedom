package com.azlan.freedom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.azlan.freedom.databinding.ActivityHomeBinding;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {


    ActivityHomeBinding binding;
    private BroadcastReceiver internetConnectionReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Register the broadcast receiver to listen for internet connection lost
        internetConnectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Display a Snackbar when the internet connection is lost
                if (intent.getAction() != null) {
                    if (intent.getAction().equals("INTERNET_CONNECTION_LOST")) {
                        // Display a Snackbar when the internet connection is lost
                        View rootView = findViewById(android.R.id.content);
                        Snackbar.make(rootView, "Internet connection lost", Snackbar.LENGTH_LONG).show();
                    } else if (intent.getAction().equals("INTERNET_CONNECTION_RESTORED")) {
                        // Display a Snackbar when the internet connection is restored
                        View rootView = findViewById(android.R.id.content);
                        Snackbar.make(rootView, "Internet connection restored", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("INTERNET_CONNECTION_LOST");
        intentFilter.addAction("INTERNET_CONNECTION_RESTORED");
        registerReceiver(internetConnectionReceiver, intentFilter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the broadcast receiver
        unregisterReceiver(internetConnectionReceiver);
    }
}