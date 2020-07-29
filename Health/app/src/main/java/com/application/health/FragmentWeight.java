package com.application.health;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class FragmentWeight extends Fragment implements View.OnClickListener {

    private CustomGauge cg_weight;
    private TextView tv_valueWeight;
    private TextView tv_kilograms;
    private Button btn_statistics;
    private RecyclerView rv_history;
    private ImageView iv_info;

    private LinearLayoutManager layoutManager;
    private AdapterWeightHistory adapterWeightHistory;
    private List<ValueWeightHelper> history;
    private UserProfile userProfile;
    private ValueWeight valueWeight;
    private TableValueWeight tableValueWeight;
    private TableUserProfiles tableUserProfiles;

    public FragmentWeight() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tableValueWeight = new TableValueWeight(getActivity());
        tableUserProfiles = new TableUserProfiles(getActivity());
        valueWeight = ValueWeight.getInstance();
        userProfile = UserProfile.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weight, container, false);

        cg_weight = view.findViewById(R.id.cg_weight);
        tv_valueWeight = view.findViewById(R.id.tv_valueWeight);
        tv_kilograms = view.findViewById(R.id.tv_kilograms);
        btn_statistics = view.findViewById(R.id.btn_statisticsWeight);
        rv_history = view.findViewById(R.id.rv_historyWeight);
        iv_info = getActivity().findViewById(R.id.iv_settingsProfile);

        iv_info.setImageResource(R.drawable.ic_info);

        btn_statistics.setOnClickListener(this);
        tv_valueWeight.setOnClickListener(this);
        iv_info.setOnClickListener(this);

        initialValueWeight();
        updateHistory();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        printInitialInformation();
        updateHistory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_statisticsWeight:
                onClickStatistics();
                break;
            case R.id.tv_valueWeight:
                onClickValueWeight();
                break;
            case R.id.iv_settingsProfile:
                onClickInfo();
                break;
        }
    }

    private void onClickStatistics() {
        Intent intent = new Intent(getActivity(), ActivityWeightStatistics.class);
        startActivity(intent);
    }

    private void onClickValueWeight() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_update_weight, null);
        final EditText et_newWeight = dialogView.findViewById(R.id.et_newWeight);
        final TextView tv_error = dialogView.findViewById(R.id.tv_errorUpdateWeight);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView)
                .setNegativeButton("Отмена",  null)
                .setPositiveButton("Ок", null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newWeight = String.valueOf(et_newWeight.getText());
                if (!(newWeight.matches("\\d{1,3}\\.\\d") || newWeight.matches("\\d{1,3}"))){
                    tv_error.setText("Вес должен быть введен в формате XX.Х");
                    return;
                }

                userProfile.setWeight(Float.parseFloat(newWeight));
                valueWeight.setWeight(Float.parseFloat(newWeight));

                if (tableValueWeight.isRecordExist()) {
                    tableValueWeight.updateRecord();
                } else {
                    tableValueWeight.insertRecord();
                }
                tableUserProfiles.updateWeight(Float.parseFloat(newWeight));

                printInitialInformation();
                updateHistory();

                dialog.dismiss();
            }
        });
    }

    private void onClickInfo() {
        Intent intent = new Intent(getActivity(), ActivityDescription.class);
        intent.putExtra("mode", ModeDescription.WEIGHT);
        startActivity(intent);
    }

    private void initialValueWeight() {

        Calendar thisDate = Calendar.getInstance();
        int day = thisDate.get(Calendar.DAY_OF_MONTH);
        int month = thisDate.get(Calendar.MONTH) + 1;
        int year = thisDate.get(Calendar.YEAR);

        valueWeight.setWeight(userProfile.getWeight());
        valueWeight.setDay(day);
        valueWeight.setMonth(month);
        valueWeight.setYear(year);
        valueWeight.setUserId(userProfile.getId());

    }

    private void printInitialInformation() {

        if (userProfile.getWeight() % 1 == 0) {
            tv_valueWeight.setText(String.valueOf((int)valueWeight.getWeight()));
        } else {
            tv_valueWeight.setText(String.format("%.1f", valueWeight.getWeight()));
        }
        float indexWeight = userProfile.getWeight() / (userProfile.getHeight() * userProfile.getHeight() / 10000);
        if (indexWeight > 45) {
            indexWeight = 45;
        }
        cg_weight.setValue((int) indexWeight);

        if (indexWeight < 16) {
            //Красный
            tv_valueWeight.setTextColor(getResources().getColor(R.color.red));
            tv_kilograms.setTextColor(getResources().getColor(R.color.red));
            cg_weight.setPointStartColor(getResources().getColor(R.color.red));
            cg_weight.setPointEndColor(getResources().getColor(R.color.red));
        } else if (indexWeight >= 16 && indexWeight < 17) {
            //оранжеый
            tv_valueWeight.setTextColor(getResources().getColor(R.color.orange));
            tv_kilograms.setTextColor(getResources().getColor(R.color.orange));
            cg_weight.setPointStartColor(getResources().getColor(R.color.orange));
            cg_weight.setPointEndColor(getResources().getColor(R.color.orange));
        } else if (indexWeight >= 17 && indexWeight < 18.5) {
            //желтый
            tv_valueWeight.setTextColor(getResources().getColor(R.color.yellow));
            tv_kilograms.setTextColor(getResources().getColor(R.color.yellow));
            cg_weight.setPointStartColor(getResources().getColor(R.color.yellow));
            cg_weight.setPointEndColor(getResources().getColor(R.color.yellow));
        } else if (indexWeight >= 18.5 && indexWeight < 25) {
            //зеленый
            tv_valueWeight.setTextColor(getResources().getColor(R.color.green));
            tv_kilograms.setTextColor(getResources().getColor(R.color.green));
            cg_weight.setPointStartColor(getResources().getColor(R.color.green));
            cg_weight.setPointEndColor(getResources().getColor(R.color.green));
        } else if (indexWeight >= 25 && indexWeight < 30) {
            //желтый
            tv_valueWeight.setTextColor(getResources().getColor(R.color.yellow));
            tv_kilograms.setTextColor(getResources().getColor(R.color.yellow));
            cg_weight.setPointStartColor(getResources().getColor(R.color.yellow));
            cg_weight.setPointEndColor(getResources().getColor(R.color.yellow));
        } else if (indexWeight >= 30 && indexWeight < 35) {
            //оранжевый
            tv_valueWeight.setTextColor(getResources().getColor(R.color.orange));
            tv_kilograms.setTextColor(getResources().getColor(R.color.orange));
            cg_weight.setPointStartColor(getResources().getColor(R.color.orange));
            cg_weight.setPointEndColor(getResources().getColor(R.color.orange));
        } else if (indexWeight >= 35 && indexWeight < 40) {
            //красный
            tv_valueWeight.setTextColor(getResources().getColor(R.color.red));
            tv_kilograms.setTextColor(getResources().getColor(R.color.red));
            cg_weight.setPointStartColor(getResources().getColor(R.color.red));
            cg_weight.setPointEndColor(getResources().getColor(R.color.red));
        } else if (indexWeight >= 40) {
            //бордовый
            tv_valueWeight.setTextColor(getResources().getColor(R.color.redDark));
            tv_kilograms.setTextColor(getResources().getColor(R.color.redDark));
            cg_weight.setPointStartColor(getResources().getColor(R.color.redDark));
            cg_weight.setPointEndColor(getResources().getColor(R.color.redDark));
        }

    }

    private void updateHistory() {
        history = tableValueWeight.selectLimit5();
        layoutManager = new LinearLayoutManager(getActivity());
        adapterWeightHistory = new AdapterWeightHistory(history, getActivity());
        rv_history.setLayoutManager(layoutManager);
        rv_history.setHasFixedSize(true); //Тк знаем размер списка
        rv_history.setAdapter(adapterWeightHistory);
    }
}
