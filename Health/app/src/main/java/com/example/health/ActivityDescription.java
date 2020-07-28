package com.example.health;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityDescription extends AppCompatActivity {

    private TextView tv_textDescription;
    private ModeDescription mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        tv_textDescription = findViewById(R.id.tv_textDescription);
        mode = (ModeDescription) getIntent().getSerializableExtra("mode");

        if (mode == null) {
            mode = ModeDescription.DEFAULT;
        }

        switch (mode) {
            case WATER:
                tv_textDescription.setText(R.string.waterDescription);
                break;
            case SLEEP:
                tv_textDescription.setText(R.string.sleepDescription);
                break;
            case WEIGHT:
                tv_textDescription.setText(R.string.weightDescription);
                break;
            case FOOD_MENU:
                tv_textDescription.setText(R.string.foodMenuDescription);
                break;
            case NUTRITION_CONTROL:
                tv_textDescription.setText(R.string.nutritionControlDescription);
                break;
            case CALORIE_CALCULATOR:
                tv_textDescription.setText(R.string.calorieCalculatorDescription);
                break;
            default:
                tv_textDescription.setText(R.string.defaultDescription);
                break;
        }
    }

    public void onClickBackscapeDescription(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
