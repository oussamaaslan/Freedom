package com.example.freedom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freedom.databinding.ActivityMainBinding;
import com.example.freedom.tools.Animated;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private  SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////inslall Splash Screen here
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        if(!sharedPref.getBoolean("First_Use",false)){
            //adding binding for mais activity
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            //animated all view in welcome screen
            Animated.fadeInAnimator(binding.imageView);
            Animated.fadeInAnimator(binding.tvWelcome);
            Animated.fadeInAnimator(binding.tvfree);
            Animated.fadeInAnimator(binding.txskip);

            binding.txskip.setOnClickListener(v -> {

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("First_Use", true);
                editor.apply();
                Intent intent=new Intent(this,LoginActivity.class);

                startActivity(intent);
                finish();

            });
        }
        else {

            Intent intent=new Intent(this,LoginActivity.class);

            startActivity(intent);
            finish();
        }





    }


}

      /*  SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("LAST_BUTTON", buttonNumber);
        editor.apply();*/
