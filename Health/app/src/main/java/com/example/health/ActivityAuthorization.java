package com.example.health;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAuthorization extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
    }

    public void onClickSingUp(View v){
        Intent intent = new Intent(this, ActivityRegistration.class);
        startActivity(intent);
        finish();
    }
}
