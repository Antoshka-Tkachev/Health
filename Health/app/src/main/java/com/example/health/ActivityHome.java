package com.example.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserProfile userProfile;


    private TextView tv_nameInHeader;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private FragmentProfile fragmentProfile;
    private FragmentWater fragmentWater;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        fragmentProfile = new FragmentProfile();
        fragmentWater = new FragmentWater();

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, fragmentProfile);
        transaction.commit();


        View header = navigationView.getHeaderView(0);
        userProfile = UserProfile.getInstance();
        tv_nameInHeader = header.findViewById(R.id.tv_nameInHeader);
        String nameInHeader = userProfile.getFirstName() + " " + userProfile.getLastName();
        tv_nameInHeader.setText(nameInHeader);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        transaction = getSupportFragmentManager().beginTransaction();

        if(id == R.id.nav_profile) {
            transaction.replace(R.id.fragmentContainer, fragmentProfile);
        } else if(id == R.id.nav_water) {
            transaction.replace(R.id.fragmentContainer, fragmentWater);
        }

        transaction.commit();

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onClickMenu(View v) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
