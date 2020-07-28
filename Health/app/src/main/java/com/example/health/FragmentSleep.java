package com.example.health;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentSleep extends Fragment implements View.OnClickListener {

    private TextView tv_fallingAsleep;
    private TextView tv_wakingUp;
    private TextView tv_duringSleep;
    private Button btn_save;
    private Button btn_date;
    private Button btn_statistics;
    private Button btn_recommendations;
    private ImageView iv_info;

    private boolean correctly;
    private String errorMessage;

    private boolean flagFallingAsleep;
    private boolean flagWakingUp;

    private int fallingAsleep;
    private int wakingUp;
    private int duringSleep;

    private Calendar fallingAsleepTime;
    private Calendar wakingUpTime;
    private Calendar duringSleepTime;

    private TableValueSleep tableValueSleep;
    private ValueSleep valueSleep;
    private UserProfile userProfile;

    private DatePickerDialog dateDialog;
    private TimePickerDialog timeDialog;

    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat formatCountTime = new SimpleDateFormat("H:mm");
    private final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tableValueSleep = new TableValueSleep(getActivity());
        valueSleep = ValueSleep.getInstance();
        userProfile = UserProfile.getInstance();

        initialValueSleep();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTheme(R.style.HomeActivity_Sleep);

        View view = inflater.inflate(R.layout.fragment_sleep, container, false);

        tv_fallingAsleep = view.findViewById(R.id.tv_fallingAsleep);
        tv_wakingUp = view.findViewById(R.id.tv_wakingUp);
        tv_duringSleep = view.findViewById(R.id.tv_duringSleep);

        btn_save = view.findViewById(R.id.btn_saveTimeSleep);
        btn_date = view.findViewById(R.id.btn_dateRecordsSleep);
        btn_statistics = view.findViewById(R.id.btn_statisticsSleep);
        btn_recommendations = view.findViewById(R.id.btn_recommendationsSleep);
        iv_info = getActivity().findViewById(R.id.iv_settingsProfile);

        iv_info.setImageResource(R.drawable.ic_info);

        tv_fallingAsleep.setOnClickListener(this);
        tv_wakingUp.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_date.setOnClickListener(this);
        btn_statistics.setOnClickListener(this);
        btn_recommendations.setOnClickListener(this);
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
        if (flagFallingAsleep && flagWakingUp && (fallingAsleep != wakingUp)) {
            if (tableValueSleep.isRecordExist()) {
                tableValueSleep.updateRecord();
            } else {
                tableValueSleep.insertRecord();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setTheme(R.style.HomeActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_fallingAsleep:
                onClickFallingAsleep();
                break;
            case R.id.tv_wakingUp:
                onClickWakingUp();
                break;
            case R.id.btn_saveTimeSleep:
                if (!checkOfEnteredData()) {
                    return;
                }
                onClickSave();
                break;
            case R.id.btn_dateRecordsSleep:
                onClickDate();
                break;
            case R.id.btn_statisticsSleep:
                onClickStatistics();
                break;
            case R.id.btn_recommendationsSleep:
                onClickRecommendations();
                break;
            case R.id.iv_settingsProfile:
                onClickInfo();
                break;
        }
    }

    private boolean checkOfEnteredData() {

        correctly = true;
        errorMessage = "";

        if (!(flagFallingAsleep && flagWakingUp)) {
            correctly = false;
            errorMessage += "Укажите данные полностью";
        } else if (fallingAsleep == wakingUp) {
            correctly = false;
            errorMessage += "Время сна должно быть больше 0";
        } else if (fallingAsleep <= 12 * 60 && fallingAsleep > wakingUp) {
            correctly = false;
            errorMessage += "Время пробуждения долно быть указано в день засыпания";
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

    private void onClickSave() {

        if (wakingUpTime.before(fallingAsleepTime)) {
            duringSleep = wakingUp - fallingAsleep + 24 * 60;
        } else {
            duringSleep = wakingUp - fallingAsleep;
        }
        duringSleepTime = Calendar.getInstance();
        duringSleepTime.clear();

        duringSleepTime.set(Calendar.MINUTE, duringSleep);

        tv_duringSleep.setText(formatCountTime.format(duringSleepTime.getTime()));

        valueSleep.setFallingAsleep(fallingAsleep);
        valueSleep.setWakingUp(wakingUp);
        valueSleep.setDuringSleep(duringSleep);
    }

    private void onClickStatistics() {
        Intent intent = new Intent(getActivity(), ActivitySleepStatistics.class);
        startActivity(intent);
    }

    private void onClickFallingAsleep() {
        Calendar thisTime = Calendar.getInstance();
        timeDialog = new TimePickerDialog(getActivity(), fallingAsleepListener, thisTime.get(Calendar.HOUR_OF_DAY), thisTime.get(Calendar.MINUTE), true);
        timeDialog.show();
    }

    private void onClickWakingUp() {
        Calendar thisTime = Calendar.getInstance();
        timeDialog = new TimePickerDialog(getActivity(), wakingUpListener, thisTime.get(Calendar.HOUR_OF_DAY), thisTime.get(Calendar.MINUTE), true);
        timeDialog.show();
    }

    private void onClickDate() {
        dateDialog = new DatePickerDialog(getActivity(), dateDialogListener, valueSleep.getYear(), valueSleep.getMonth() - 1, valueSleep.getDay());
        dateDialog.show();
    }

    private void onClickRecommendations() {
        Intent intent = new Intent(getActivity(), ActivityRecommendationSleep.class);
        startActivity(intent);
    }

    private void onClickInfo() {
        Intent intent = new Intent(getActivity(), ActivityDescription.class);
        intent.putExtra("mode", ModeDescription.SLEEP);
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

            if (flagFallingAsleep && flagWakingUp && (fallingAsleep != wakingUp)) {
                if (tableValueSleep.isRecordExist()) {
                    tableValueSleep.updateRecord();
                } else {
                    tableValueSleep.insertRecord();
                }
            }

            setValueWater(day, month + 1, year);

            printInitialInformation();

        }
    };

    private TimePickerDialog.OnTimeSetListener fallingAsleepListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            flagFallingAsleep = true;

            fallingAsleep = hourOfDay * 60 + minute;

            fallingAsleepTime = Calendar.getInstance();
            fallingAsleepTime.clear();
            fallingAsleepTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            fallingAsleepTime.set(Calendar.MINUTE, minute);

            tv_fallingAsleep.setText(formatTime.format(fallingAsleepTime.getTime()));
        }

    };

    private TimePickerDialog.OnTimeSetListener wakingUpListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            flagWakingUp = true;

            wakingUp = hourOfDay * 60 + minute;

            wakingUpTime = Calendar.getInstance();
            wakingUpTime.clear();
            wakingUpTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            wakingUpTime.set(Calendar.MINUTE, minute);

            tv_wakingUp.setText(formatTime.format(wakingUpTime.getTime()));
        }

    };


    private void initialValueSleep() {

        Calendar thisDate = Calendar.getInstance();
        int day = thisDate.get(Calendar.DAY_OF_MONTH);
        int month = thisDate.get(Calendar.MONTH) + 1;
        int year = thisDate.get(Calendar.YEAR);

        valueSleep.setFallingAsleep(0);
        valueSleep.setWakingUp(0);
        valueSleep.setDuringSleep(0);
        valueSleep.setDay(day);
        valueSleep.setMonth(month);
        valueSleep.setYear(year);
        valueSleep.setUserId(userProfile.getId());

        if (tableValueSleep.isRecordExist()) {
            tableValueSleep.selectRecord();
            flagFallingAsleep = true;
            flagWakingUp = true;
        } else {
            flagFallingAsleep = false;
            flagWakingUp = false;
        }

        fallingAsleep = valueSleep.getFallingAsleep();
        wakingUp = valueSleep.getWakingUp();
        duringSleep = valueSleep.getDuringSleep();
    }

    private void setValueWater(int day, int month, int year) {

        valueSleep.setFallingAsleep(0);
        valueSleep.setWakingUp(0);
        valueSleep.setDuringSleep(0);
        valueSleep.setDay(day);
        valueSleep.setMonth(month);
        valueSleep.setYear(year);
        valueSleep.setUserId(userProfile.getId());

        if (tableValueSleep.isRecordExist()) {
            tableValueSleep.selectRecord();
            flagFallingAsleep = true;
            flagWakingUp = true;
        } else {
            flagFallingAsleep = false;
            flagWakingUp = false;
        }

        fallingAsleep = valueSleep.getFallingAsleep();
        wakingUp = valueSleep.getWakingUp();
        duringSleep = valueSleep.getDuringSleep();
    }

    private void printInitialInformation() {

        if (valueSleep.getDuringSleep() == 0) {
            tv_duringSleep.setText("0:00");
            tv_fallingAsleep.setText("Время засыпания");
            tv_wakingUp.setText("Время пробуждения");
        } else {
            fallingAsleepTime = Calendar.getInstance();
            fallingAsleepTime.clear();
            fallingAsleepTime.set(Calendar.MINUTE, valueSleep.getFallingAsleep());

            wakingUpTime = Calendar.getInstance();
            wakingUpTime.clear();
            wakingUpTime.set(Calendar.MINUTE, valueSleep.getWakingUp());

            duringSleepTime = Calendar.getInstance();
            duringSleepTime.clear();
            if (wakingUpTime.before(fallingAsleepTime)) {
                duringSleepTime.set(Calendar.MINUTE, valueSleep.getDuringSleep() + 24 * 60);
            } else {
                duringSleepTime.set(Calendar.MINUTE, valueSleep.getDuringSleep());
            }

            tv_duringSleep.setText(formatCountTime.format(duringSleepTime.getTime()));
            tv_fallingAsleep.setText(formatTime.format(fallingAsleepTime.getTime()));
            tv_wakingUp.setText(formatTime.format(wakingUpTime.getTime()));
        }

        Calendar date = Calendar.getInstance();
        date.set(valueSleep.getYear(), valueSleep.getMonth() - 1, valueSleep.getDay());
        btn_date.setText(formatDate.format(date.getTime()));
    }

}