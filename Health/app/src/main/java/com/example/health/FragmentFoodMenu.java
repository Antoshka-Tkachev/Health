package com.example.health;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentFoodMenu extends Fragment implements View.OnClickListener {

    private Button btn_calorieCalc;
    private Button btn_nutritionControl;
    private Button btn_createMenu;
    private Button btn_yourMenus;
    private Button btn_recommendedMenus;
    private RecyclerView rv_menus;

    private boolean isModeYourMenu;

    private FragmentTransaction transaction;
    private FragmentCalorieCalculator fragmentCalorieCalculator;
    private FragmentNutritionControl fragmentNutritionControl;

    private LinearLayoutManager layoutManager;
    private AdapterFoodMenus adapterYourMenus;
    private AdapterFoodMenus adapterRecommendedMenus;
    private ArrayList<String> namesYourMenus;
    private ValueUserFoodMenu valueMenu;
    private ValueRecommendedMenu valueRecomMenu;
    private UserProfile userProfile;
    private TableUserFoodMenu tableUserFoodMenu;
    private TableRecommendedMenu tableRecommendedMenu;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tableUserFoodMenu = new TableUserFoodMenu(getActivity());
        tableRecommendedMenu = new TableRecommendedMenu(getActivity());
        valueRecomMenu = new ValueRecommendedMenu();
        fragmentCalorieCalculator = new FragmentCalorieCalculator();
        fragmentNutritionControl = new FragmentNutritionControl();
        layoutManager = new LinearLayoutManager(getActivity());
        userProfile = UserProfile.getInstance();
        valueMenu = ValueUserFoodMenu.getInstance();
        valueMenu.setUserId(userProfile.getId());

        isModeYourMenu = true;

        valueRecomMenu.setNameMenus(tableRecommendedMenu.selectNamesMenu());
        adapterRecommendedMenus = new AdapterFoodMenus(getActivity(), valueRecomMenu.getNameMenus(), false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_menu, container, false);

        btn_calorieCalc = view.findViewById(R.id.btn_calorieCalculator_fm);
        btn_nutritionControl = view.findViewById(R.id.btn_nutritionControl_fm);
        btn_createMenu = view.findViewById(R.id.btn_createMenu);
        btn_yourMenus = view.findViewById(R.id.btn_yourMenus);
        btn_recommendedMenus = view.findViewById(R.id.btn_recommendedMenus);
        rv_menus = view.findViewById(R.id.rv_foodMenus);

        btn_calorieCalc.setOnClickListener(this);
        btn_nutritionControl.setOnClickListener(this);
        btn_createMenu.setOnClickListener(this);
        btn_yourMenus.setOnClickListener(this);
        btn_recommendedMenus.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv_titleHome = getActivity().findViewById(R.id.tv_titleHome);
        tv_titleHome.setText("Меню питания");
    }

    @Override
    public void onResume() {
        super.onResume();
        setInitialInformation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_calorieCalculator_fm:
                onClickCalorieCalc();
                break;
            case R.id.btn_nutritionControl_fm:
                onClickNutritionControl();
                break;
            case R.id.btn_createMenu:
                onClickCreateMenu();
                break;
            case R.id.btn_recommendedMenus:
                onClickRecommendedMenus();
                break;
            case R.id.btn_yourMenus:
                onClickYourMenus();
                break;
        }
    }

    private void setInitialInformation() {

        tableUserFoodMenu.selectNamesMenu();
        namesYourMenus = valueMenu.getNameMenus();

        adapterYourMenus = new AdapterFoodMenus(getActivity(), namesYourMenus, true);
        rv_menus.setLayoutManager(layoutManager);
        rv_menus.setHasFixedSize(true); //Тк знаем размер списка
        rv_menus.setAdapter(adapterYourMenus);
        itemTouchHelper.attachToRecyclerView(rv_menus);

        if (isModeYourMenu) {
            onClickYourMenus();
        } else {
            onClickRecommendedMenus();
        }
    }

    private void onClickCalorieCalc() {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragmentCalorieCalculator);
        transaction.commit();
    }

    private void onClickNutritionControl() {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragmentNutritionControl);
        transaction.commit();
    }

    private void onClickCreateMenu() {
        valueMenu.createNewName();
        tableUserFoodMenu.addNewMenu();
        namesYourMenus.add(0, valueMenu.getName());
        adapterYourMenus.notifyDataSetChanged();
    }

    private void onClickRecommendedMenus() {
        isModeYourMenu = false;
        btn_createMenu.setEnabled(false);
        btn_createMenu.setBackgroundResource(R.drawable.background_button_enabled);
        btn_yourMenus.setBackgroundResource(R.drawable.background_button_food_menu_left);
        btn_recommendedMenus.setBackgroundResource(R.drawable.background_button_food_menu_right_sel);

        rv_menus.setAdapter(adapterRecommendedMenus);
        itemTouchHelper.attachToRecyclerView(null);
    }

    private void onClickYourMenus() {
        isModeYourMenu = true;
        btn_createMenu.setEnabled(true);
        btn_createMenu.setBackgroundResource(R.drawable.background_button_water);
        btn_yourMenus.setBackgroundResource(R.drawable.background_button_food_menu_left_sel);
        btn_recommendedMenus.setBackgroundResource(R.drawable.background_button_food_menu_right);

        rv_menus.setAdapter(adapterYourMenus);
        itemTouchHelper.attachToRecyclerView(rv_menus);
    }

    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
        new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(namesYourMenus, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(namesYourMenus, i, i - 1);
                    }
                }
                adapterYourMenus.notifyItemMoved(fromPosition, toPosition);

                tableUserFoodMenu.updateAllPosition();
                return true;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Подтверждение")
                        .setMessage("Вы точно хотите удалить меню?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tableUserFoodMenu.deleteRecord(position);
                                tableUserFoodMenu.updatePosition(position);
                                namesYourMenus.remove(position);
                                adapterYourMenus.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adapterYourMenus.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        }).show();
                adapterYourMenus.notifyDataSetChanged();
            }
        }
    );
}