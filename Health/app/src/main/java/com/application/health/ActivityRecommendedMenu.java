package com.application.health;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityRecommendedMenu extends AppCompatActivity {

    private RecyclerView rv_infoCaloriesPFC;
    private TextView tv_nameMenu;
    private ValueRecommendedMenu valueRecomMenu;
    private TableRecommendedMenu tableRecomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_menu);
        rv_infoCaloriesPFC = findViewById(R.id.rv_infoCaloriesPFC_RecMenu);
        tv_nameMenu = findViewById(R.id.tv_nameMenu_RecMenu);

        Intent intent = getIntent();
        valueRecomMenu = new ValueRecommendedMenu();
        tableRecomMenu = new TableRecommendedMenu(this);
        valueRecomMenu = tableRecomMenu.selectMenu(intent.getIntExtra("position", 0));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        AdapterRecomMenuForDays adapterRecomMenuForDays = new AdapterRecomMenuForDays(valueRecomMenu);
        rv_infoCaloriesPFC.setLayoutManager(layoutManager);
        rv_infoCaloriesPFC.setHasFixedSize(true); //Тк знаем размер списка
        rv_infoCaloriesPFC.setAdapter(adapterRecomMenuForDays);

        tv_nameMenu.setText(valueRecomMenu.getNameMenu());
    }
}
