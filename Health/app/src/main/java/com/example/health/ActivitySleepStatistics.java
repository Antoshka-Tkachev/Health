package com.example.health;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

public class ActivitySleepStatistics extends AppCompatActivity {

    private ToggleButton tbnt_week;
    private ToggleButton tbnt_month;
    private ToggleButton tbnt_year;
    private DateDiapason dateDiapason;
    private String modeBarChart;

    private FragmentBarChart fragmentBarChart;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_statistics);

        dateDiapason = new DateDiapason();

        tbnt_week = findViewById(R.id.tbnt_weekSleepStat);
        tbnt_month = findViewById(R.id.tbnt_monthSleepStat);
        tbnt_year = findViewById(R.id.tbnt_yearSleepStat);

        tbnt_week.setChecked(true);
        modeBarChart = "Week";

        //Устанавливать начальную диаграмму
        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_barChartSleepStat, fragmentBarChart);
        transaction.commit();

        dateDiapason.calculatingWeekDates();
        Calendar[] date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        new LoadingBarChartSleep(this, date).execute(modeBarChart);
    }


    public void onClickBackscapeSleepStat(View v) {
        ActivitySleepStatistics.super.onBackPressed();
    }

    public void onClickWeekSleepStat(View v) {
        if (modeBarChart.equals("Week")) {
            tbnt_week.setChecked(true);
            return;
        }
        modeBarChart = "Week";
        tbnt_week.setChecked(true);
        tbnt_month.setChecked(false);
        tbnt_year.setChecked(false);

        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_barChartSleepStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        new LoadingBarChartSleep(this, date).execute(modeBarChart);
    }

    public void onClickMonthSleepStat(View v) {
        if (modeBarChart.equals("Month")) {
            tbnt_month.setChecked(true);
            return;
        }
        modeBarChart = "Month";
        tbnt_week.setChecked(false);
        tbnt_month.setChecked(true);
        tbnt_year.setChecked(false);

        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_barChartSleepStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStatByMonth() };
        new LoadingBarChartSleep(this, date).execute(modeBarChart);
    }

    public void onClickYearSleepStat(View v) {
        if (modeBarChart.equals("Year")) {
            tbnt_year.setChecked(true);
            return;
        }
        modeBarChart = "Year";
        tbnt_week.setChecked(false);
        tbnt_month.setChecked(false);
        tbnt_year.setChecked(true);

        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_barChartSleepStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStatByYear() };
        new LoadingBarChartSleep(this, date).execute(modeBarChart);
    }

    public void onClickNextSleepStat(View v) {
        Calendar[] date;
        switch (modeBarChart) {
            case "Week":
                dateDiapason.calculatingNextWeekDates();
                date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartSleep(this, date).execute(modeBarChart);
                break;
            case "Month":
                date  = new Calendar[] { dateDiapason.calculatingNextMonth() };
                new LoadingBarChartSleep(this, date).execute(modeBarChart);
                break;
            case "Year":
                date  = new Calendar[] { dateDiapason.calculatingNextYear() };
                new LoadingBarChartSleep(this, date).execute(modeBarChart);
                break;
            default:
                break;
        }
    }

    public void onClickLastSleepStat(View v) {
        Calendar[] date;
        switch (modeBarChart) {
            case "Week":
                dateDiapason.calculatingLastWeekDates();
                date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartSleep(this, date).execute(modeBarChart);
                break;
            case "Month":
                date  = new Calendar[] { dateDiapason.calculatingLastMonth() };
                new LoadingBarChartSleep(this, date).execute(modeBarChart);
                break;
            case "Year":
                date  = new Calendar[] { dateDiapason.calculatingLastYear() };
                new LoadingBarChartSleep(this, date).execute(modeBarChart);
                break;
            default:
                break;
        }
    }
}
