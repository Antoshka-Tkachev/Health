package com.example.health;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

public class ActivitySleepStatistics extends AppCompatActivity {

    private ToggleButton tbtn_week;
    private ToggleButton tbtn_month;
    private ToggleButton tbtn_year;
    private DateDiapason dateDiapason;
    private ModePeriod modePeriod;

    private FragmentBarChart fragmentBarChart;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_statistics);

        dateDiapason = new DateDiapason();

        tbtn_week = findViewById(R.id.tbtn_weekSleepStat);
        tbtn_month = findViewById(R.id.tbtn_monthSleepStat);
        tbtn_year = findViewById(R.id.tbtn_yearSleepStat);

        tbtn_week.setChecked(true);
        modePeriod = ModePeriod.WEEK;

        //Устанавливать начальную диаграмму
        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_barChartSleepStat, fragmentBarChart);
        transaction.commit();

        dateDiapason.calculatingWeekDates();
        Calendar[] date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        new LoadingBarChartSleep(this, date, modePeriod).execute();
    }


    public void onClickBackscapeSleepStat(View v) {
        ActivitySleepStatistics.super.onBackPressed();
    }

    public void onClickWeekSleepStat(View v) {
        if (modePeriod == ModePeriod.WEEK) {
            tbtn_week.setChecked(true);
            return;
        }
        modePeriod = ModePeriod.WEEK;
        tbtn_week.setChecked(true);
        tbtn_month.setChecked(false);
        tbtn_year.setChecked(false);

        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_barChartSleepStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        new LoadingBarChartSleep(this, date, modePeriod).execute();
    }

    public void onClickMonthSleepStat(View v) {
        if (modePeriod == ModePeriod.MONTH) {
            tbtn_month.setChecked(true);
            return;
        }
        modePeriod = ModePeriod.MONTH;
        tbtn_week.setChecked(false);
        tbtn_month.setChecked(true);
        tbtn_year.setChecked(false);

        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_barChartSleepStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStatByMonth() };
        new LoadingBarChartSleep(this, date, modePeriod).execute();
    }

    public void onClickYearSleepStat(View v) {
        if (modePeriod == ModePeriod.YEAR) {
            tbtn_year.setChecked(true);
            return;
        }
        modePeriod = ModePeriod.YEAR;
        tbtn_week.setChecked(false);
        tbtn_month.setChecked(false);
        tbtn_year.setChecked(true);

        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_barChartSleepStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStatByYear() };
        new LoadingBarChartSleep(this, date, modePeriod).execute();
    }

    public void onClickNextSleepStat(View v) {
        Calendar[] date;
        switch (modePeriod) {
            case WEEK:
                dateDiapason.calculatingNextWeekDates();
                date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartSleep(this, date, modePeriod).execute();
                break;

            case MONTH:
                date  = new Calendar[] { dateDiapason.calculatingNextMonth() };
                new LoadingBarChartSleep(this, date, modePeriod).execute();
                break;

            case YEAR:
                date  = new Calendar[] { dateDiapason.calculatingNextYear() };
                new LoadingBarChartSleep(this, date, modePeriod).execute();
                break;

            default:
                break;
        }
    }

    public void onClickLastSleepStat(View v) {
        Calendar[] date;
        switch (modePeriod) {
            case WEEK:
                dateDiapason.calculatingLastWeekDates();
                date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartSleep(this, date, modePeriod).execute();
                break;
            case MONTH:
                date  = new Calendar[] { dateDiapason.calculatingLastMonth() };
                new LoadingBarChartSleep(this, date, modePeriod).execute();
                break;
            case YEAR:
                date  = new Calendar[] { dateDiapason.calculatingLastYear() };
                new LoadingBarChartSleep(this, date, modePeriod).execute();
                break;
            default:
                break;
        }
    }
}
