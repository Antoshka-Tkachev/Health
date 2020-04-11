package com.example.health;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AdapterFoodMenuPager extends FragmentStatePagerAdapter {

    public AdapterFoodMenuPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FragmentFoodMenuFirstItem();
        }
        return FragmentFoodMenuItems.newInstance(position);
    }

    @Override
    public int getCount() {
        return 8;
    }



}
