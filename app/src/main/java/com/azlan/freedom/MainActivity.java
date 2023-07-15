package com.azlan.freedom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.azlan.freedom.Services.InternetCheckService;
import com.azlan.freedom.tools.Animated;
import com.azlan.freedom.databinding.ActivityMainBinding;
import com.azlan.freedom.tools.Utility;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////inslall Splash Screen here
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        // Start the InternetCheckService
        Intent serviceIntent = new Intent(this, InternetCheckService.class);
        startService(serviceIntent);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);

        if (!sharedPref.getBoolean("First_Use", false)&&!Utility.tokenCheck(this)) {
            setContentView(binding.getRoot());
            Animated.fadeInAnimator(binding.imageView);
            Animated.fadeInAnimator(binding.tvWelcome);
            Animated.fadeInAnimator(binding.tvfree);
            Animated.fadeInAnimator(binding.txskip);


            binding.txskip.setOnClickListener(v -> {

                sharedPref.edit().putBoolean("First_Use", true).apply();
                Intent intent = new Intent(this,
                        AuthActivity.class);
                startActivity(intent);
                finish();
            });
        }
        else if(sharedPref.getBoolean("First_Use", false)&& Utility.tokenCheck(this)){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        }
    }
}


// if (!Utility.getInstance().tokenCheck(this)) {
//         binding.txskip.setOnClickListener(v -> {
//         Animated.fadeInAnimator(binding.imageView);
//         Animated.fadeInAnimator(binding.tvWelcome);
//         Animated.fadeInAnimator(binding.tvfree);
//         Animated.fadeInAnimator(binding.txskip);
//         SharedPreferences.Editor editor = sharedPref.edit();
//         editor.putBoolean("First_Use", true);
//         editor.apply();
//         Intent intent = new Intent(this,
//         AuthActivity.class);
//        startActivity(intent);
//        finish();
//        });
//        }
//        else {

//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
//        }
