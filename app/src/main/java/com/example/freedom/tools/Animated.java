package com.example.freedom.tools;

import android.animation.ObjectAnimator;
import android.view.View;

public class Animated {
   public static  void fadeInAnimator(View view){
        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeInAnimator.setDuration(2000); // Adjust the duration as needed (in milliseconds)

// Set the visibility of the ImageView to visible before starting the animation
        view.setVisibility(View.VISIBLE);

// Start the animation
        fadeInAnimator.start();
    }

}
