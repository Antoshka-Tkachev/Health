package com.example.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserProfile userProfile;
    private TableUserProfiles tableUserProfiles;

    private TextView tv_titleHome;
    private TextView tv_nameInHeader;
    private ImageView iv_userPicture;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private FragmentProfile fragmentProfile;
    private FragmentWater fragmentWater;
    private FragmentSleep fragmentSleep;
    private FragmentWeight fragmentWeight;
    private FragmentNutritionControl fragmentNutritionControl;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userProfile = UserProfile.getInstance();
        tableUserProfiles = new TableUserProfiles(this);

        tv_titleHome = findViewById(R.id.tv_titleHome);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        fragmentProfile = new FragmentProfile();
        fragmentWater = new FragmentWater();
        fragmentSleep = new FragmentSleep();
        fragmentNutritionControl = new FragmentNutritionControl();
        fragmentWeight = new FragmentWeight();

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, fragmentProfile);
        transaction.commit();
        tv_titleHome.setText("Профиль");

        View header = navigationView.getHeaderView(0);
        tv_nameInHeader = header.findViewById(R.id.tv_nameInHeader);
        iv_userPicture = header.findViewById(R.id.iv_userPictureHead);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String nameInHeader = userProfile.getFirstName() + " " + userProfile.getLastName();
        tv_nameInHeader.setText(nameInHeader);
        iv_userPicture.setImageBitmap(userProfile.getUserPicture());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_profile) {
            tv_titleHome.setText("Профиль");
            transaction.replace(R.id.fragmentContainer, fragmentProfile);
        } else if (id == R.id.nav_menu) {
            transaction.replace(R.id.fragmentContainer, fragmentNutritionControl);
        } else if (id == R.id.nav_water) {
            tv_titleHome.setText("Вода");
            transaction.replace(R.id.fragmentContainer, fragmentWater);
        } else if (id == R.id.nav_sleep) {
            tv_titleHome.setText("Сон");
            transaction.replace(R.id.fragmentContainer, fragmentSleep);
        } else if (id == R.id.nav_weight) {
            tv_titleHome.setText("Контроль веса");
            transaction.replace(R.id.fragmentContainer, fragmentWeight);
        } else if (id == R.id.nav_log_out) {
            userProfile.setRemember(0);
            tableUserProfiles.logOut();
            tableUserProfiles.close();

            Intent intent = new Intent(ActivityHome.this, ActivityAuthorization.class);
            startActivity(intent);

            finish();
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
