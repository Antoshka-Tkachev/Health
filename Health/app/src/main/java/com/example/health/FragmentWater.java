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

public class FragmentWater extends Fragment {

    private int volumeWater = 0;
    ProgressBar pb_goalWater;
    EditText et_volumeWater;
    TextView tv_goalWater;
    ImageView iv_waterDrop;

    public FragmentWater() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water, container, false);
        pb_goalWater = view.findViewById(R.id.pb_goalWater);
        et_volumeWater = view.findViewById(R.id.et_volumeWater);
        tv_goalWater = view.findViewById(R.id.tv_goalWater);
        iv_waterDrop = view.findViewById(R.id.iv_waterDrop);
        Button btn_plus = view.findViewById(R.id.btn_plus);
        Button btn_minus = view.findViewById(R.id.btn_minus);
        Button btn_statistics = view.findViewById(R.id.btn_statistics);

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volumeWater += Integer.parseInt(et_volumeWater.getText().toString());
                pb_goalWater.setProgress((volumeWater * 100) / 2000);
                tv_goalWater.setText("Цель: " + volumeWater + "/2000");

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
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volumeWater <= Integer.parseInt(et_volumeWater.getText().toString())){
                    volumeWater = 0;
                    pb_goalWater.setProgress(0);
                    tv_goalWater.setText("Цель: " + 0 + "/2000");
                    iv_waterDrop.setImageResource(R.drawable.water_drop_cry);
                    return;
                }

                volumeWater -= Integer.parseInt(et_volumeWater.getText().toString());
                pb_goalWater.setProgress((volumeWater * 100) / 2000);
                tv_goalWater.setText("Цель: " + volumeWater + "/2000");

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
        });

        btn_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityWaterStatistics.class);
                startActivity(intent);
            }
        });

        return view;
    }

}

