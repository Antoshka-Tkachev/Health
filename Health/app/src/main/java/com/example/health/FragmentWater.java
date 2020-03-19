package com.example.health;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentWater extends Fragment implements View.OnClickListener {

    private Water water;
    private ProgressBar pb_goalWater;
    private EditText et_volumeWater;
    private TextView tv_goalWater;
    private ImageView iv_waterDrop;

    private boolean correctly;
    private String errorMessage;

    private String volumeWater;

    public FragmentWater() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        water = Water.getInstance();

        View view = inflater.inflate(R.layout.fragment_water, container, false);
        pb_goalWater = view.findViewById(R.id.pb_goalWater);
        et_volumeWater = view.findViewById(R.id.et_volumeWater);
        tv_goalWater = view.findViewById(R.id.tv_goalWater);
        iv_waterDrop = view.findViewById(R.id.iv_waterDrop);

        Button btn_plus = view.findViewById(R.id.btn_plus);
        Button btn_minus = view.findViewById(R.id.btn_minus);
        Button btn_statistics = view.findViewById(R.id.btn_statistics);

        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_statistics.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setInitialInformation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_plus:
                if (!checkOfEnteredData()) {
                    return;
                }
                onClickPlus();
                break;
            case R.id.btn_minus:
                if (!checkOfEnteredData()) {
                    return;
                }
                onClickMinus();
                break;
            case R.id.btn_statistics:
                onClickStatistics();
                break;
        }
    }

    private boolean checkOfEnteredData() {

        correctly = true;
        errorMessage = "";

        volumeWater = et_volumeWater.getText().toString();

        if (!volumeWater.matches("\\d{1,4}")) {
            correctly = false;
            errorMessage += "Введите целое число от 0 до 9999\n";
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

    private void onClickPlus() {
        water.setValue(water.getValue() + Integer.parseInt(et_volumeWater.getText().toString()));
        pb_goalWater.setProgress((water.getValue() * 100) / water.getGoalValue());
        tv_goalWater.setText("Цель: " + water.getValue() + "/" + water.getGoalValue());

        if (pb_goalWater.getProgress() >= 100) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_joyful);
        } else if (pb_goalWater.getProgress() >= 70) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_smile);
        } else if (pb_goalWater.getProgress() >= 35) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_cheerless);
        } else {
            iv_waterDrop.setImageResource(R.drawable.water_drop_cry);
        }
    }

    private void onClickMinus() {
        if (water.getValue() <= Integer.parseInt(et_volumeWater.getText().toString())) {
            water.setValue(0);
            pb_goalWater.setProgress(0);
            tv_goalWater.setText("Цель: " + 0 + "/" + water.getGoalValue());
            iv_waterDrop.setImageResource(R.drawable.water_drop_cry);
            return;
        }

        water.setValue(water.getValue() - Integer.parseInt(et_volumeWater.getText().toString()));
        pb_goalWater.setProgress((water.getValue() * 100) / water.getGoalValue());
        tv_goalWater.setText("Цель: " + water.getValue() + "/" + water.getGoalValue());

        if (pb_goalWater.getProgress() < 35) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_cry);
        } else if (pb_goalWater.getProgress() < 70) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_cheerless);
        } else if (pb_goalWater.getProgress() < 100) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_smile);
        } else {
            iv_waterDrop.setImageResource(R.drawable.water_drop_joyful);
        }
    }

    private void onClickStatistics() {
        Intent intent = new Intent(getActivity(), ActivityWaterStatistics.class);
        startActivity(intent);
    }

    private void setInitialInformation() {
        //установить цель относительно веса
        water.setGoalValue(2500);

        et_volumeWater.setText("");
        pb_goalWater.setProgress((water.getValue() * 100) / water.getGoalValue());
        tv_goalWater.setText("Цель: " + water.getValue() + "/" + water.getGoalValue());

        if (pb_goalWater.getProgress() >= 100) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_joyful);
        } else if (pb_goalWater.getProgress() >= 70) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_smile);
        } else if (pb_goalWater.getProgress() >= 35) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_cheerless);
        } else {
            iv_waterDrop.setImageResource(R.drawable.water_drop_cry);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //сохранять Water в бд
    }
}

