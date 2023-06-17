package com.example.freedom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freedom.tools.Animated;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //animated all view in welcome screen
        ImageView imageView = findViewById(R.id.imageView);
        TextView tvWelcome=findViewById(R.id.tvWelcome);
        TextView tvfree=findViewById(R.id.tvfree);
        TextView txskip=findViewById(R.id.txskip);
        Animated.fadeInAnimator(imageView);
        Animated.fadeInAnimator(tvWelcome);
        Animated.fadeInAnimator(tvfree);
        Animated.fadeInAnimator(txskip);
// Create the ObjectAnimator for alpha animation

    }
}