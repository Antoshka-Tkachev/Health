package com.example.health;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentFoodMenuFirstItem extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tv_nameMenu;
    private RecyclerView rv_infoCaloriesPFC;

    private ValueUserFoodMenu userFoodMenu;
    private TableUserFoodMenu tableUserFoodMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food_menu_first_item, parent, false);
        tv_nameMenu = view.findViewById(R.id.tv_nameMenu_fmfi);
        rv_infoCaloriesPFC = view.findViewById(R.id.rv_infoCaloriesPFC_fmfi);

        userFoodMenu = ValueUserFoodMenu.getInstance();
        tableUserFoodMenu = new TableUserFoodMenu(getActivity());
         int numberMenu = getActivity().getIntent().getIntExtra("position", 0);

        tableUserFoodMenu.selectMenu(numberMenu);
        tv_nameMenu.setText(userFoodMenu.getName());

        tv_nameMenu.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        AdapterFoodMenuForDays adapterFoodMenuForDays = new AdapterFoodMenuForDays();
        rv_infoCaloriesPFC.setLayoutManager(layoutManager);
        rv_infoCaloriesPFC.setHasFixedSize(true); //Тк знаем размер списка
        rv_infoCaloriesPFC.setAdapter(adapterFoodMenuForDays);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_nameMenu_fmfi:
                onClickNameMenu();
                break;
        }
    }

    private void onClickNameMenu() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_update_name_menu, null);
        final EditText et_newNameMenu = dialogView.findViewById(R.id.et_newNameMenu);
        final TextView tv_error = dialogView.findViewById(R.id.tv_errorUpdateNameMenu);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView)
            .setPositiveButton("Ок", null);
        et_newNameMenu.setText(tv_nameMenu.getText());

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNameMenu = String.valueOf(et_newNameMenu.getText());
                if (!newNameMenu.matches(".+")) {
                    tv_error.setText("Название должно содержать минимум 1 символ");
                    return;
                } else if (tableUserFoodMenu.isExistMenu(newNameMenu)){
                    tv_error.setText("Меню с таким именем уже существует");
                    return;
                }
                userFoodMenu.getNameMenus().set(userFoodMenu.getNumber(), newNameMenu);
                userFoodMenu.setName(newNameMenu);
                tv_nameMenu.setText(newNameMenu);
                tableUserFoodMenu.updateNameMenu();
                dialog.dismiss();
            }
        });
    }
}