package com.example.health;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

    public FragmentWater() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        water = Water.getInstance();

        View view = inflater.inflate(R.layout.fragment_water, container, false);
        pb_goalWater = view.findViewById(R.id.pb_goalWater);
        et_volumeWater = view.findViewById(R.id.et_volumeWater);
        tv_goalWater = view.findViewById(R.id.tv_goalWater);
        iv_waterDrop = view.findViewById(R.id.iv_waterDrop);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_plus:
                onClickPlus();
                break;
            case R.id.btn_minus:
                onClickMinus();
                break;
            case R.id.btn_statistics:
                onClickStatistics();
                break;
        }
    }

    private void onClickPlus() {

        water.setValue(water.getValue() + Integer.parseInt(et_volumeWater.getText().toString()));
        pb_goalWater.setProgress((water.getValue() * 100) / water.getGoalValue());
        tv_goalWater.setText("Цель: " + water.getValue() + "/" + water.getGoalValue());

        if (pb_goalWater.getProgress() >= 100) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_joyful);
        } else if (pb_goalWater.getProgress() >= 60) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_smile);
        } else if (pb_goalWater.getProgress() >= 30) {
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
        pb_goalWater.setProgress((water.getValue() * 100) / 2000);
        tv_goalWater.setText("Цель: " + water.getValue() + "/" + water.getGoalValue());

        if (pb_goalWater.getProgress() < 30) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_cry);
        } else if (pb_goalWater.getProgress() < 60) {
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

    @Override
    public void onStop() {
        super.onStop();
        //сохранять Water в бд
    }
}

