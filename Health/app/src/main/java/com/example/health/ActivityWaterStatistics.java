package com.example.health;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class ActivityWaterStatistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_statistics);

        ImageView iv_backscape = findViewById(R.id.iv_backscape);
        iv_backscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityWaterStatistics.super.onBackPressed();
            }
        });



        BarChart barChart = findViewById(R.id.bc_water);

        barChart.setDrawBarShadow(false); //добавляет фон для колонок
        barChart.setDrawValueAboveBar(true); // делает надписи над колонками
        barChart.setMaxVisibleValueCount(250);
        barChart.setPinchZoom(true); // добавляет зум
        barChart.setDrawGridBackground(false); // Добавляет серый фон сетки
        barChart.setScaleEnabled(true); // Убирает растягивания (можно отдельно по осям )
        barChart.setTouchEnabled(true); //уберает возможность нажатий

        Description description = barChart.getDescription(); //подпись(описание)
        description.setEnabled(false); //убирает подпись
        //barChart.setBackgroundResource(R.drawable.white);


        ArrayList<BarEntry> barEntries = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            barEntries.add(new BarEntry(i, random.nextInt(2000)));
        }
//        barEntries.add(new BarEntry(1, 20f));
//        barEntries.add(new BarEntry(2, 35f));
//        barEntries.add(new BarEntry(3, 30f));
//        barEntries.add(new BarEntry(6, 40f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Количество выпитой воды в мл"); //название диараммы
//        barDataSet.setColors(ColorTemplate.LIBERTY_COLORS); //установка цвета

        barDataSet.setColors(getResources().getColor(R.color.blueMiddle));
        barDataSet.setHighLightColor(getResources().getColor(R.color.black));
        barDataSet.setDrawValues(false);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.5f); //толщина колонки диаграммы
        barChart.setData(data);

        String[] day = new String[] {"Понедельник", "Вторник",
                "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};

        String[] day1 = new String[] {"Пн", "Вт",
                "Ср", "Чт", "Пт", "Сб", "Вс", "Пн"};
        String[] month = new String[] {"1", "2",
                "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16",
                "17", "18", "19", "20", "21", "22" , "23",
                "24", "25", "26", "27", "28", "29", "30", "31"};

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValue(month));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setLabelRotationAngle(45f); //угол
        xAxis.setTextSize(12f);
        //xAxis.setTextColor(getResources().getColor(R.color.colorPrimaryDark)) ;
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTypeface(Typeface.create("default", Typeface.BOLD));

        barChart.getAxisRight().setEnabled(false);
        YAxis yAxis = barChart.getAxisLeft() ;
        //yAxis.setTypeface(); // установить другой шрифт
        yAxis.setTextSize ( 12f ) ; // установить размер текста
        yAxis.setAxisMinimum(0f) ; // начинаем с нуля
        //yAxis.setAxisMaximum ( 100f ) ; // максимальная ось равна 100
        //yAxis.setTextColor ( Color.BLACK ) ;
        yAxis.setTypeface(Typeface.create("default", Typeface.BOLD));
        yAxis.setDrawGridLines(true);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        e.getY() + "\n" + e.getX(),
                        Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    public class MyXAxisValue extends ValueFormatter  {

        private  String[] dayValues;
        public MyXAxisValue(String[] values) {
            this.dayValues = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return dayValues[(int)value];
        }
    }
}
