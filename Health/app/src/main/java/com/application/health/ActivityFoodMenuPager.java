package com.application.health;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class ActivityFoodMenuPager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_pager);

        ViewPager viewPager = findViewById(R.id.vp_foodMenu);
        AdapterFoodMenuPager pagerAdapter = new AdapterFoodMenuPager(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
    }
}
