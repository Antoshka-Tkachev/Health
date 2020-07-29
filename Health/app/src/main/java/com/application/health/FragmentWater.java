package com.application.health;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentWater extends Fragment implements View.OnClickListener {

    private ProgressBar pb_goalWater;
    private EditText et_volumeWater;
    private TextView tv_goalWater;
    private ImageView iv_waterDrop;
    private ImageView iv_info;
    private Button btn_plus;
    private Button btn_minus;
    private Button btn_statistics;
    private Button btn_data;
    private DatePickerDialog dateDialog;

    private boolean correctly;
    private String errorMessage;
    private String volumeWater;

    private UserProfile userProfile;
    private ValueWater valueWater;
    private TableValueWater tableValueWater;

    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public FragmentWater() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tableValueWater = new TableValueWater(getActivity());
        valueWater = ValueWater.getInstance();
        userProfile = UserProfile.getInstance();

        initialValueWater();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_water, container, false);
        pb_goalWater = view.findViewById(R.id.pb_goalWater);
        et_volumeWater = view.findViewById(R.id.et_volumeWater);
        tv_goalWater = view.findViewById(R.id.tv_goalWater);
        iv_waterDrop = view.findViewById(R.id.iv_waterDrop);

        btn_plus = view.findViewById(R.id.btn_plus);
        btn_minus = view.findViewById(R.id.btn_minus);
        btn_statistics = view.findViewById(R.id.btn_statisticsWater);
        btn_data = view.findViewById(R.id.btn_dateRecordsWater);
        iv_info = getActivity().findViewById(R.id.iv_settingsProfile);

        iv_info.setImageResource(R.drawable.ic_info);

        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_statistics.setOnClickListener(this);
        btn_data.setOnClickListener(this);
        iv_info.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        printInitialInformation();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (tableValueWater.isRecordExist()) {
            tableValueWater.updateRecord();
        } else {
            tableValueWater.insertRecord();
        }
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
            case R.id.btn_dateRecordsWater:
                onClickDate();
                break;
            case R.id.btn_statisticsWater:
                onClickStatistics();
                break;
            case R.id.iv_settingsProfile:
                onClickInfo();
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
        valueWater.setValue(valueWater.getValue() + Integer.parseInt(et_volumeWater.getText().toString()));
        pb_goalWater.setProgress((valueWater.getValue() * 100) / valueWater.getGoalValue());
        tv_goalWater.setText("Цель: " + valueWater.getValue() + "/" + valueWater.getGoalValue());

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
        if (valueWater.getValue() <= Integer.parseInt(et_volumeWater.getText().toString())) {
            valueWater.setValue(0);
            pb_goalWater.setProgress(0);
            tv_goalWater.setText("Цель: " + 0 + "/" + valueWater.getGoalValue());
            iv_waterDrop.setImageResource(R.drawable.water_drop_cry);
            return;
        }

        valueWater.setValue(valueWater.getValue() - Integer.parseInt(et_volumeWater.getText().toString()));
        pb_goalWater.setProgress((valueWater.getValue() * 100) / valueWater.getGoalValue());
        tv_goalWater.setText("Цель: " + valueWater.getValue() + "/" + valueWater.getGoalValue());

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

    private void onClickDate() {
        dateDialog = new DatePickerDialog(getActivity(), dateDialogListener, valueWater.getYear(), valueWater.getMonth() - 1, valueWater.getDay());
        dateDialog.show();
    }

    private void onClickInfo() {
        Intent intent = new Intent(getActivity(), ActivityDescription.class);
        intent.putExtra("mode", ModeDescription.WATER);
        startActivity(intent);
    }

    private DatePickerDialog.OnDateSetListener dateDialogListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month,
                              int day) {
            //проверка на ввод даты
            Calendar thisDate = Calendar.getInstance();
            Calendar chooseDate = Calendar.getInstance();
            chooseDate.set(year, month, day, 0, 0, 0);;

            if (chooseDate.after(thisDate)) {
                dateDialog.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Ошибка")
                        .setMessage("Указана дата в будущем")
                        .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
                return;
            }

            if (tableValueWater.isRecordExist()) {
                tableValueWater.updateRecord();
            } else {
                tableValueWater.insertRecord();
            }

            setValueWater(day, month + 1, year);

            printInitialInformation();

        }
    };


    private void initialValueWater() {

        Calendar thisDate = Calendar.getInstance();
        int day = thisDate.get(Calendar.DAY_OF_MONTH);
        int month = thisDate.get(Calendar.MONTH) + 1;
        int year = thisDate.get(Calendar.YEAR);

        if (userProfile.getGender().equals("М")) {
            valueWater.setGoalValue((int) (userProfile.getWeight() * 35));
        } else {
            valueWater.setGoalValue((int) (userProfile.getWeight() * 30));
        }

        valueWater.setValue(0);
        valueWater.setDay(day);
        valueWater.setMonth(month);
        valueWater.setYear(year);
        valueWater.setUserId(userProfile.getId());

        if (tableValueWater.isRecordExist()) {
            tableValueWater.selectValue();
        }
    }

    private void setValueWater(int day, int month, int year) {

        if (userProfile.getGender().equals("М")) {
            valueWater.setGoalValue((int) (userProfile.getWeight() * 35));
        } else {
            valueWater.setGoalValue((int) (userProfile.getWeight() * 30));
        }

        valueWater.setValue(0);
        valueWater.setDay(day);
        valueWater.setMonth(month);
        valueWater.setYear(year);
        valueWater.setUserId(userProfile.getId());

        if (tableValueWater.isRecordExist()) {
            tableValueWater.selectRecord();
        }
    }

    private void printInitialInformation() {

        et_volumeWater.setText("");
        pb_goalWater.setProgress((valueWater.getValue() * 100) / valueWater.getGoalValue());
        tv_goalWater.setText("Цель: " + valueWater.getValue() + "/" + valueWater.getGoalValue());

        if (pb_goalWater.getProgress() >= 100) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_joyful);
        } else if (pb_goalWater.getProgress() >= 70) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_smile);
        } else if (pb_goalWater.getProgress() >= 35) {
            iv_waterDrop.setImageResource(R.drawable.water_drop_cheerless);
        } else {
            iv_waterDrop.setImageResource(R.drawable.water_drop_cry);
        }

        Calendar date = Calendar.getInstance();
        date.set(valueWater.getYear(), valueWater.getMonth() - 1, valueWater.getDay());
        btn_data.setText(format.format(date.getTime()));

    }

}