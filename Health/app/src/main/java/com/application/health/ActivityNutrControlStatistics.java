package com.application.health;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import java.util.Calendar;

public class ActivityNutrControlStatistics extends AppCompatActivity {

    private ToggleButton tbtn_week;
    private ToggleButton tbtn_month;
    private ToggleButton tbtn_year;
    private ToggleButton tbtn_protein;
    private ToggleButton tbtn_fat;
    private ToggleButton tbtn_carbohydrate;
    private ToggleButton tbtn_calories;

    private DateDiapason dateDiapason;
    private FragmentBarChart fragmentBarChart;
    private FragmentTransaction transaction;
    private ModeMicroelement modeElement;
    private ModePeriod modePeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutr_control_statistics);

        dateDiapason = new DateDiapason();

        tbtn_week = findViewById(R.id.tbtn_weekNCStat);
        tbtn_month = findViewById(R.id.tbtn_monthNCStat);
        tbtn_year = findViewById(R.id.tbtn_yearNCStat);
        tbtn_protein = findViewById(R.id.tbtn_proteinNCStat);
        tbtn_fat = findViewById(R.id.tbtn_fatNCStat);
        tbtn_carbohydrate = findViewById(R.id.tbtn_carbohydrateNCStat);
        tbtn_calories = findViewById(R.id.tbtn_caloriesNCStat);

        modeElement = ModeMicroelement.CALORIES;
        modePeriod = ModePeriod.WEEK;
        tbtn_calories.setChecked(true);
        tbtn_week.setChecked(true);

        //Устанавливать начальную диаграмму
        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_barChartNCStat, fragmentBarChart);
        transaction.commit();

        dateDiapason.calculatingWeekDates();
        Calendar[] date  = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        new LoadingBarChartNurtControl(this, date, modePeriod, modeElement).execute();
    }

    private void createBarChar() {
        fragmentBarChart = new FragmentBarChart();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_barChartNCStat, fragmentBarChart);
        transaction.commit();
        Calendar[] date = null;
        if (modePeriod == ModePeriod.WEEK) {
            date = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
        } else if (modePeriod == ModePeriod.MONTH) {
            date = new Calendar[] { dateDiapason.getDateStatByMonth() };
        } else if (modePeriod == ModePeriod.YEAR) {
            date = new Calendar[] { dateDiapason.getDateStatByYear() };
        }
        new LoadingBarChartNurtControl(this, date, modePeriod, modeElement).execute();
    }

    public void onClickProteinNCStat(View v) {
        if (modeElement == ModeMicroelement.PROTEIN) {
            tbtn_protein.setChecked(true);
            return;
        }
        modeElement = ModeMicroelement.PROTEIN;
        tbtn_protein.setChecked(true);
        tbtn_fat.setChecked(false);
        tbtn_carbohydrate.setChecked(false);
        tbtn_calories.setChecked(false);

        createBarChar();
    }

    public void onClickFatNCStat(View v) {
        if (modeElement == ModeMicroelement.FAT) {
            tbtn_fat.setChecked(true);
            return;
        }
        modeElement = ModeMicroelement.FAT;
        tbtn_protein.setChecked(false);
        tbtn_fat.setChecked(true);
        tbtn_carbohydrate.setChecked(false);
        tbtn_calories.setChecked(false);

        createBarChar();
    }

    public void onClickCarbohydrateNCStat(View v) {
        if (modeElement == ModeMicroelement.CARBOHYDRATE) {
            tbtn_carbohydrate.setChecked(true);
            return;
        }
        modeElement = ModeMicroelement.CARBOHYDRATE;

        tbtn_protein.setChecked(false);
        tbtn_fat.setChecked(false);
        tbtn_carbohydrate.setChecked(true);
        tbtn_calories.setChecked(false);

        createBarChar();
    }

    public void onClickCaloriesNCStat(View v) {
        if (modeElement == ModeMicroelement.CALORIES) {
            tbtn_calories.setChecked(true);
            return;
        }
        modeElement = ModeMicroelement.CALORIES;
        tbtn_protein.setChecked(false);
        tbtn_fat.setChecked(false);
        tbtn_carbohydrate.setChecked(false);
        tbtn_calories.setChecked(true);

        createBarChar();
    }

    public void onClickYearNCStat(View v) {
        if (modePeriod == ModePeriod.YEAR) {
            tbtn_year.setChecked(true);
            return;
        }
        modePeriod = ModePeriod.YEAR;
        tbtn_week.setChecked(false);
        tbtn_month.setChecked(false);
        tbtn_year.setChecked(true);

        createBarChar();
    }

    public void onClickMonthNCStat(View v) {
        if (modePeriod == ModePeriod.MONTH) {
            tbtn_month.setChecked(true);
            return;
        }
        modePeriod = ModePeriod.MONTH;
        tbtn_week.setChecked(false);
        tbtn_month.setChecked(true);
        tbtn_year.setChecked(false);

        createBarChar();
    }

    public void onClickWeekNCStat(View v) {
        if (modePeriod == ModePeriod.WEEK) {
            tbtn_week.setChecked(true);
            return;
        }
        modePeriod = ModePeriod.WEEK;
        tbtn_week.setChecked(true);
        tbtn_month.setChecked(false);
        tbtn_year.setChecked(false);

       createBarChar();
    }

    public void onClickLastNCStat(View v) {
        Calendar[] date;
        switch (modePeriod) {
            case WEEK:
                dateDiapason.calculatingLastWeekDates();
                date = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartNurtControl(this, date, modePeriod, modeElement).execute();
                break;
            case MONTH:
                date = new Calendar[] { dateDiapason.calculatingLastMonth() };
                new LoadingBarChartNurtControl(this, date, modePeriod, modeElement).execute();
                break;
            case YEAR:
                date = new Calendar[] { dateDiapason.calculatingLastYear() };
                new LoadingBarChartNurtControl(this, date, modePeriod, modeElement).execute();
                break;
            default:
                break;
        }
    }

    public void onClickNextNCStat(View v) {
        Calendar[] date;
        switch (modePeriod) {
            case WEEK:
                dateDiapason.calculatingNextWeekDates();
                date = new Calendar[] { dateDiapason.getDateStartWeek(), dateDiapason.getDateEndWeek() };
                new LoadingBarChartNurtControl(this, date, modePeriod, modeElement).execute();
                break;
            case MONTH:
                date = new Calendar[] { dateDiapason.calculatingNextMonth() };
                new LoadingBarChartNurtControl(this, date, modePeriod, modeElement).execute();
                break;
            case YEAR:
                date = new Calendar[] { dateDiapason.calculatingNextYear() };
                new LoadingBarChartNurtControl(this, date, modePeriod, modeElement).execute();
                break;
            default:
                break;
        }
    }

    public void onClickBackscapeNCStat(View v) {
        ActivityNutrControlStatistics.super.onBackPressed();
    }
}
