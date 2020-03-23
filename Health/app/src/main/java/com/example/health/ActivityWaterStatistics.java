package com.example.health;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import java.util.Calendar;

public class ActivityWaterStatistics extends AppCompatActivity {

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
        setContentView(R.layout.activity_water_statistics);

        dateDiapason = new DateDiapason();

        tbnt_week = findViewById(R.id.tbnt_weekWatStat);
        tbnt_month = findViewById(R.id.tbnt_monthWatStat);
        tbnt_year = findViewById(R.id.tbnt_yearWatStat);

        tbnt_week.setChecked(true);
        modeBarChart = "Week";

        //Устанавливать начальную диаграмму
        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_barChartWatStat, fragmentBarChart);
        transaction.commit();

        dateDiapason.calculatingWeekDates();
        Calendar[] date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        new LoadingBarChartWater(this, date).execute(modeBarChart);
    }


    public void onClickBackscape(View v) {
        ActivityWaterStatistics.super.onBackPressed();
    }

    public void onClickWeekWatStat(View v) {
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
        transaction.replace(R.id.fl_barChartWatStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        new LoadingBarChartWater(this, date).execute(modeBarChart);
    }

    public void onClickMonthWatStat(View v) {
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
        transaction.replace(R.id.fl_barChartWatStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStatByMonth() };
        new LoadingBarChartWater(this, date).execute(modeBarChart);
    }

    public void onClickYearWatStat(View v) {
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
        transaction.replace(R.id.fl_barChartWatStat, fragmentBarChart);
        transaction.commit();

        Calendar[] date  = new Calendar[] { dateDiapason.getDateStatByYear() };
        new LoadingBarChartWater(this, date).execute(modeBarChart);
    }

    public void onClickNextWatStat(View v) {
        Calendar[] date;
        switch (modeBarChart) {
            case "Week":
                dateDiapason.calculatingNextWeekDates();
                date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartWater(this, date).execute(modeBarChart);
                break;
            case "Month":
                date  = new Calendar[] { dateDiapason.calculatingNextMonth() };
                new LoadingBarChartWater(this, date).execute(modeBarChart);
                break;
            case "Year":
                date  = new Calendar[] { dateDiapason.calculatingNextYear() };
                new LoadingBarChartWater(this, date).execute(modeBarChart);
                break;
            default:
                break;
        }
    }

    public void onClickLastWatStat(View v) {
        Calendar[] date;
        switch (modeBarChart) {
            case "Week":
                dateDiapason.calculatingLastWeekDates();
                date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartWater(this, date).execute(modeBarChart);
                break;
            case "Month":
                date  = new Calendar[] { dateDiapason.calculatingLastMonth() };
                new LoadingBarChartWater(this, date).execute(modeBarChart);
                break;
            case "Year":
                date  = new Calendar[] { dateDiapason.calculatingLastYear() };
                new LoadingBarChartWater(this, date).execute(modeBarChart);
                break;
            default:
                break;
        }
    }
}
