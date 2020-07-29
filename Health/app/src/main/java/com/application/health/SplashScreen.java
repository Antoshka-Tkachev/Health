package com.application.health;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_authorization);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView heart = findViewById(R.id.imageHeart);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.heart_size);
        heart.startAnimation(anim);

        new LaunchingFirstActivity(this).execute();
    }
}