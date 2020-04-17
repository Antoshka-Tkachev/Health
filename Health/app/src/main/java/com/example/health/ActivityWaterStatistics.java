package com.example.health;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import java.util.Calendar;

public class ActivityWaterStatistics extends AppCompatActivity {

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
        setContentView(R.layout.activity_water_statistics);

        dateDiapason = new DateDiapason();

        tbtn_week = findViewById(R.id.tbtn_weekWatStat);
        tbtn_month = findViewById(R.id.tbtn_monthWatStat);
        tbtn_year = findViewById(R.id.tbtn_yearWatStat);

        tbtn_week.setChecked(true);
        modePeriod = ModePeriod.WEEK;

        //Устанавливать начальную диаграмму
        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_barChartWatStat, fragmentBarChart);
        transaction.commit();

        dateDiapason.calculatingWeekDates();
        Calendar[] date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        new LoadingBarChartWater(this, date, modePeriod).execute();
    }


    public void onClickBackscapeWatStat(View v) {
        ActivityWaterStatistics.super.onBackPressed();
    }

    public void onClickWeekWatStat(View v) {
        if (modePeriod.equals("Week")) {
            tbtn_week.setChecked(true);
            return;
        }
        modePeriod = ModePeriod.WEEK;
        tbtn_week.setChecked(true);
        tbtn_month.setChecked(false);
        tbtn_year.setChecked(false);

        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_barChartWatStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        new LoadingBarChartWater(this, date, modePeriod).execute();
    }

    public void onClickMonthWatStat(View v) {
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
        transaction.replace(R.id.fl_barChartWatStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStatByMonth() };
        new LoadingBarChartWater(this, date, modePeriod).execute();
    }

    public void onClickYearWatStat(View v) {
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
        transaction.replace(R.id.fl_barChartWatStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStatByYear() };
        new LoadingBarChartWater(this, date, modePeriod).execute();
    }

    public void onClickNextWatStat(View v) {
        Calendar[] date;
        switch (modePeriod) {
            case WEEK:
                dateDiapason.calculatingNextWeekDates();
                date = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartWater(this, date, modePeriod).execute();
                break;
            case MONTH:
                date = new Calendar[] { dateDiapason.calculatingNextMonth() };
                new LoadingBarChartWater(this, date, modePeriod).execute();
                break;
            case YEAR:
                date = new Calendar[] { dateDiapason.calculatingNextYear() };
                new LoadingBarChartWater(this, date, modePeriod).execute();
                break;
            default:
                break;
        }
    }

    public void onClickLastWatStat(View v) {
        Calendar[] date;
        switch (modePeriod) {
            case WEEK:
                dateDiapason.calculatingLastWeekDates();
                date = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartWater(this, date, modePeriod).execute();
                break;
            case MONTH:
                date = new Calendar[] { dateDiapason.calculatingLastMonth() };
                new LoadingBarChartWater(this, date, modePeriod).execute();
                break;
            case YEAR:
                date = new Calendar[] { dateDiapason.calculatingLastYear() };
                new LoadingBarChartWater(this, date, modePeriod).execute();
                break;
            default:
                break;
        }
    }
}
