package com.example.health;

import android.content.Intent;
import android.os.AsyncTask;
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

        new LoadingAuthorizationActivity().execute();
    }

    class LoadingAuthorizationActivity extends AsyncTask <Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(SplashScreen.this, ActivityAuthorization.class);
            startActivity(intent);
            finish();
        }
    }
}