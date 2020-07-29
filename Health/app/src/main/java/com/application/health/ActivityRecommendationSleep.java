package com.application.health;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ActivityRecommendationSleep extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_sleep);
        Toolbar toolbar = findViewById(R.id.toolBarRecomSleep);
        setSupportActionBar(toolbar);
    }
}
