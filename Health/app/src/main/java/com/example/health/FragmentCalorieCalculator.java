package com.example.health;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentCalorieCalculator extends Fragment implements View.OnClickListener {

    private View view;
    private Button btn_nutritionControl;
    private Button btn_foodMenu;
    private Button btn_calculate;
    private EditText et_height;
    private EditText et_weight;
    private EditText et_age;
    private TextView tv_result;
    private RadioButton rbtn_genderBoy;
    private RadioButton rbtn_genderGirl;
    private Spinner s_physicalActivity;
    private ImageView iv_info;

    private String height;
    private String weight;
    private String age;
    private boolean genderBoy;
    private boolean genderGirl;
    private double physicalActivity;

    private FragmentNutritionControl fragmentNutritionControl;
    private FragmentFoodMenu fragmentFoodMenu;
    private FragmentTransaction transaction;

    private final String TEXT_RESULT = "Ваша суточная норма калорий: ";
    private final String TEXT_CALORIES = " кКал";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTheme(R.style.HomeActivity_CalorieCalculator);

        fragmentFoodMenu = new FragmentFoodMenu();
        fragmentNutritionControl = new FragmentNutritionControl();

        view = inflater.inflate(R.layout.fragment_calorie_calculator, container, false);
        btn_nutritionControl = view.findViewById(R.id.btn_nutritionControl_cc);
        btn_foodMenu = view.findViewById(R.id.btn_foodMenu_cc);
        btn_calculate = view.findViewById(R.id.btn_calculate_cc);
        et_height = view.findViewById(R.id.et_height_cc);
        et_weight = view.findViewById(R.id.et_weight_cc);
        et_age = view.findViewById(R.id.et_age_cc);
        tv_result = view.findViewById(R.id.tv_result_cc);
        rbtn_genderBoy = view.findViewById(R.id.rbtn_genderBoy_cc);
        rbtn_genderGirl = view.findViewById(R.id.rbtn_genderGirl_cc);
        s_physicalActivity = view.findViewById(R.id.s_physicalActivity);
        iv_info = getActivity().findViewById(R.id.iv_settingsProfile);

        iv_info.setImageResource(R.drawable.ic_info);

        btn_nutritionControl.setOnClickListener(this);
        btn_foodMenu.setOnClickListener(this);
        btn_calculate.setOnClickListener(this);
        iv_info.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                new String[] {"Физическая активность...", "Минимальная активность",
                        "Низкая активность", "Средняя активность",
                        "Высокая активность", "Экстримальная активность"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_physicalActivity.setAdapter(adapter);
        s_physicalActivity.setOnItemSelectedListener(itemSpinnerListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv_titleHome = getActivity().findViewById(R.id.tv_titleHome);
        tv_titleHome.setText("Калькулятор");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setTheme(R.style.HomeActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_foodMenu_cc:
                onClickFoodMenu();
                break;
            case R.id.btn_calculate_cc:
                onClickCalculate();
                break;
            case R.id.btn_nutritionControl_cc:
                onClickNutritionControl();
                break;
            case R.id.iv_settingsProfile:
                onClickInfo();
                break;
        }
    }

    private void onClickFoodMenu() {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragmentFoodMenu);
        transaction.commit();
    }

    private void onClickNutritionControl() {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragmentNutritionControl);
        transaction.commit();
    }

    private void onClickCalculate() {
        if (!checkOfEnteredData()) {
            return;
        }

        double result = 0;
        if (genderBoy) {
            result = (10 * Double.valueOf(weight) + 6.25 * Double.valueOf(height) -
                    5 * Double.valueOf(age) + 5) * physicalActivity;
        } else if (genderGirl) {
            result = (10 * Double.valueOf(weight) + 6.25 * Double.valueOf(height) -
                    5 * Double.valueOf(age) - 161) * physicalActivity;
        }

        if (result < 0) {
            result = 0;
        }

        String textResult = TEXT_RESULT + String.format("%.2f", result) + TEXT_CALORIES;
        tv_result.setText(textResult);
    }

    private void onClickInfo() {
        Intent intent = new Intent(getActivity(), ActivityDescription.class);
        intent.putExtra("mode", ModeDescription.CALORIE_CALCULATOR);
        startActivity(intent);
    }

    private boolean checkOfEnteredData() {

        boolean correctly = true;
        String errorMessage = "";

        height = String.valueOf(et_height.getText());
        weight = String.valueOf(et_weight.getText());
        age = String.valueOf(et_age.getText());
        genderBoy = rbtn_genderBoy.isChecked();
        genderGirl = rbtn_genderGirl.isChecked();

        if (!(height.matches("\\d{2,3}\\.\\d") || height.matches("\\d{2,3}"))) {
            correctly = false;
            errorMessage += "Рост должен быть введен в формате ХХХ.Х\n";
        }

        if (!(weight.matches("\\d{1,3}\\.\\d") || weight.matches("\\d{1,3}"))) {
            correctly = false;
            errorMessage += "Вес должен быть введен в формате XX.Х\n";
        }

        if (!age.matches("\\d{1,2}")){
            correctly = false;
            errorMessage += "Возраст должен быть целым числом от 1 до 100\n";
        }

        if(!(genderBoy || genderGirl)) {
            correctly = false;
            errorMessage += "Выберите пол\n";
        }

        if(!correctly) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ошибка")
                    .setMessage(errorMessage)
                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        }

        return correctly;
    }

    private double getPhysicalActivity(int position) {
        switch (position) {
            case 0: return 1;
            case 1: return 1.2;
            case 2: return 1.375;
            case 3: return 1.55;
            case 4: return 1.725;
            case 5: return 1.9;
        }
        return 0;
    }

    private AdapterView.OnItemSelectedListener itemSpinnerListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos,
        long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setTextSize(20);
            physicalActivity = getPhysicalActivity(pos);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

}