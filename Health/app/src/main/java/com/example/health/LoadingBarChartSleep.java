package com.example.health;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LoadingBarChartSleep extends AsyncTask<Void, Void, Void> {

    private Activity activitySleepStat;
    private BarChart barChart;
    private TextView tv_sum;
    private TextView tv_avgFallingAsleep;
    private TextView tv_avgWakingUp;
    private TextView tv_avgDuringSleep;
    private TextView tv_date;
    private TextView tv_value;
    private TextView tv_diapason;

    private TableValueSleep tableValueSleep;
    private List<ValueSleepHelper> listValue;
    private Calendar[] date;
    private ArrayList<BarEntry> barEntries;
    private ArrayList<Integer> finalValues;
    private int sumDuringSleep;
    private int sumFallingAsleep;
    private int sumWakingUp;
    private String sumDuringSleepText;
    private String avgDuringSleep;
    private String avgFallingAsleep;
    private String avgWakingUp;
    private int count;
    private ModePeriod modePeriod;

    private final SimpleDateFormat formatCountTime = new SimpleDateFormat("H:mm");
    private final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat formatMinutes = new SimpleDateFormat("mm");

    LoadingBarChartSleep(Activity activitySleepStat, Calendar[] date, ModePeriod modePeriod) {
        this.activitySleepStat = activitySleepStat;
        tableValueSleep = new TableValueSleep(activitySleepStat.getApplicationContext());
        this.date = date;
        this.modePeriod = modePeriod;
        barEntries = new ArrayList<>();
        finalValues = new ArrayList<>();
        sumDuringSleep = 0;
        sumFallingAsleep = 0;
        sumWakingUp = 0;
        avgDuringSleep = "";
        avgFallingAsleep = "";
        avgWakingUp = "";
        count = 0;
    }

    @Override
    protected Void doInBackground(Void... diapason) {

        Calendar temp = Calendar.getInstance();
        switch (modePeriod) {

            case WEEK:
                listValue = tableValueSleep.selectWeekInfo(date[0], date[1]);

                Calendar cursor = Calendar.getInstance();
                cursor.set(date[0].get(Calendar.YEAR), date[0].get(Calendar.MONTH), date[0].get(Calendar.DAY_OF_MONTH));

                for (int i = 0; i < 7; i++) {
                    barEntries.add(new BarEntry(i, 0));
                    finalValues.add(0);
                }

                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < listValue.size(); j++) {
                        if (cursor.get(Calendar.DAY_OF_MONTH) == listValue.get(j).getDay()) {
                            barEntries.set(i, new BarEntry(i, (float)listValue.get(j).getDuringSleep() / 60));
                            finalValues.set(i, listValue.get(j).getDuringSleep());

                            sumDuringSleep += listValue.get(j).getDuringSleep();

                            //если засыпание от 00:00 до 12:00 то для корректного подсчета
                            //среднего времени засыпания прибавляем 24 часа
                            if (listValue.get(j).getFallingAsleep() > 12 * 60) {
                                sumFallingAsleep += listValue.get(j).getFallingAsleep();
                            } else {
                                sumFallingAsleep += 24 * 60 + listValue.get(j).getFallingAsleep();
                            }

                            sumWakingUp += listValue.get(j).getWakingUp();
                            count++;
                        }
                    }
                    cursor.add(Calendar.DAY_OF_MONTH, 1);
                }

                temp.clear();
                temp.set(Calendar.MINUTE, sumDuringSleep);
                sumDuringSleepText =
                        (temp.get(Calendar.DAY_OF_MONTH) - 1) * 24 +
                        temp.get(Calendar.HOUR_OF_DAY) + ":" +
                        formatMinutes.format(temp.getTime());

                temp.clear();
                temp.set(Calendar.MINUTE, (int)((float) sumDuringSleep / (float) count));
                avgDuringSleep = formatCountTime.format(temp.getTime());

                //если среднее время засыпания получилось больше 24 часов, то вычитаем 24 часа
                temp.clear();
                if (((int)((float) sumFallingAsleep / (float) count) >= 24 * 60)) {
                    temp.set(Calendar.MINUTE, ((int)((float) sumFallingAsleep / (float) count)) - 24 * 60);
                } else {
                    temp.set(Calendar.MINUTE, (int)((float) sumFallingAsleep / (float) count));
                }
                avgFallingAsleep = formatTime.format(temp.getTime());

                temp.clear();
                temp.set(Calendar.MINUTE, (int)((float) sumWakingUp / (float) count));
                avgWakingUp = formatTime.format(temp.getTime());

                break;

            case MONTH:
                listValue = tableValueSleep.selectMonthInfo(date[0]);

                for (int i = 0; i < date[0].getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    barEntries.add(new BarEntry(i, 0));
                    finalValues.add(0);
                }

                for (int i = 0; i < listValue.size(); i++) {
                    barEntries.set(listValue.get(i).getDay() - 1,
                            new BarEntry(listValue.get(i).getDay() - 1,
                                    (float) listValue.get(i).getDuringSleep() / 60));
                    finalValues.set(listValue.get(i).getDay() - 1, listValue.get(i).getDuringSleep());

                    sumDuringSleep += listValue.get(i).getDuringSleep();

                    //если засыпание от 00:00 до 12:00 то для корректного подсчета
                    //среднего времени засыпания прибавляем 24 часа
                    if (listValue.get(i).getFallingAsleep() > 12 * 60) {
                        sumFallingAsleep += listValue.get(i).getFallingAsleep();
                    } else {
                        sumFallingAsleep += 24 * 60 + listValue.get(i).getFallingAsleep();
                    }

                    sumWakingUp += listValue.get(i).getWakingUp();
                    count++;
                }

                temp.clear();
                temp.set(Calendar.MINUTE, sumDuringSleep);
                sumDuringSleepText =
                        (temp.get(Calendar.DAY_OF_MONTH) - 1) * 24 +
                        temp.get(Calendar.HOUR_OF_DAY) + ":" +
                        formatMinutes.format(temp.getTime());

                temp.clear();
                temp.set(Calendar.MINUTE, (int)((float) sumDuringSleep / (float) count));
                avgDuringSleep = formatCountTime.format(temp.getTime());

                //если среднее время засыпания получилось больше 24 часов, то вычитаем 24 часа
                temp.clear();
                if (((int)((float) sumFallingAsleep / (float) count) >= 24 * 60)) {
                    temp.set(Calendar.MINUTE, ((int)((float) sumFallingAsleep / (float) count)) - 24 * 60);
                } else {
                    temp.set(Calendar.MINUTE, (int)((float) sumFallingAsleep / (float) count));
                }
                avgFallingAsleep = formatTime.format(temp.getTime());

                temp.clear();
                temp.set(Calendar.MINUTE, (int)((float) sumWakingUp / (float) count));
                avgWakingUp = formatTime.format(temp.getTime());

                break;

            case YEAR:
                listValue = tableValueSleep.selectYearInfo(date[0]);

                int sumsDuringSleep[] = new int[12];
                int counts[] = new int[12];

                for (int i = 0; i < listValue.size(); i++) {
                    sumsDuringSleep[listValue.get(i).getMonth() - 1] += listValue.get(i).getDuringSleep();
                    counts[listValue.get(i).getMonth() - 1]++;

                    //если засыпание от 00:00 до 12:00 то для корректного подсчета
                    //среднего времени засыпания прибавляем 24 часа
                    if (listValue.get(i).getFallingAsleep() > 12 * 60) {
                        sumFallingAsleep += listValue.get(i).getFallingAsleep();
                    } else {
                        sumFallingAsleep += 24 * 60 + listValue.get(i).getFallingAsleep();
                    }

                    sumWakingUp += listValue.get(i).getWakingUp();
                    sumDuringSleep += listValue.get(i).getDuringSleep();
                    count++;
                }

                ArrayList<Integer> averages = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    averages.add((int) ((float) sumsDuringSleep[i] / (float) counts[i]));
                    barEntries.add(new BarEntry(i, (float) averages.get(i) / 60));
                    finalValues.add(averages.get(i));
                }

                temp.clear();
                temp.set(Calendar.MINUTE, sumDuringSleep);
                sumDuringSleepText =
                        (temp.get(Calendar.DAY_OF_YEAR) - 1) * 24 +
                        temp.get(Calendar.HOUR_OF_DAY) + ":" +
                        formatMinutes.format(temp.getTime());

                temp.clear();
                temp.set(Calendar.MINUTE, (int)((float) sumDuringSleep / (float) count));
                avgDuringSleep = formatCountTime.format(temp.getTime());

                //если среднее время засыпания получилось больше 24 часов, то вычитаем 24 часа
                temp.clear();
                if (((int)((float) sumFallingAsleep / (float) count) >= 24 * 60)) {
                    temp.set(Calendar.MINUTE, ((int)((float) sumFallingAsleep / (float) count)) - 24 * 60);
                } else {
                    temp.set(Calendar.MINUTE, (int)((float) sumFallingAsleep / (float) count));
                }
                avgFallingAsleep = formatTime.format(temp.getTime());

                temp.clear();
                temp.set(Calendar.MINUTE, (int)((float) sumWakingUp / (float) count));
                avgWakingUp = formatTime.format(temp.getTime());

                break;

            default:
                break;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        drawBarChart();
    }

    private void drawBarChart() {
        barChart = activitySleepStat.findViewById(R.id.bc_water);

        tv_diapason = activitySleepStat.findViewById(R.id.tv_diapasonSleepStat);
        tv_date = activitySleepStat.findViewById(R.id.tv_dateSleepStat);
        tv_value = activitySleepStat.findViewById(R.id.tv_valueSleepStat);
        tv_sum = activitySleepStat.findViewById(R.id.tv_sumDuringSleep);
        tv_avgFallingAsleep = activitySleepStat.findViewById(R.id.tv_averageFallingAsleep);
        tv_avgWakingUp = activitySleepStat.findViewById(R.id.tv_averageWakingUp);
        tv_avgDuringSleep = activitySleepStat.findViewById(R.id.tv_averageDuringSleep);

        tv_sum.setText(sumDuringSleepText);
        tv_avgDuringSleep.setText(avgDuringSleep);
        tv_avgFallingAsleep.setText(avgFallingAsleep);
        tv_avgWakingUp.setText(avgWakingUp);

        String tv_diapasonText;
        String tv_dateText;
        String tv_valueText;
        BarEntry lastColumn = barEntries.get(barEntries.size() - 1);
        Calendar temp = Calendar.getInstance();

        switch (modePeriod) {
            case WEEK:
                if (date[0].get(Calendar.YEAR) == date[1].get(Calendar.YEAR)) {
                    if (date[0].get(Calendar.MONTH) == date[1].get(Calendar.MONTH)) {
                        tv_diapasonText =
                                date[0].get(Calendar.DAY_OF_MONTH) + " - " +
                                date[1].get(Calendar.DAY_OF_MONTH) + " " +
                                CalendarText.getNameMonthGenitive(date[1].get(Calendar.MONTH)) + " " +
                                date[1].get(Calendar.YEAR);
                    } else {
                        tv_diapasonText =
                                date[0].get(Calendar.DAY_OF_MONTH) + " " +
                                CalendarText.getNameMonthGenitive(date[0].get(Calendar.MONTH)) + " - " +
                                date[1].get(Calendar.DAY_OF_MONTH) + " " +
                                CalendarText.getNameMonthGenitive(date[1].get(Calendar.MONTH)) + "\n" +
                                date[1].get(Calendar.YEAR);
                    }
                } else {
                    tv_diapasonText =
                            date[0].get(Calendar.DAY_OF_MONTH) + " " +
                            CalendarText.getNameMonthGenitive(date[0].get(Calendar.MONTH)) + " " +
                            date[0].get(Calendar.YEAR) + " -\n" +
                            date[1].get(Calendar.DAY_OF_MONTH) + " " +
                            CalendarText.getNameMonthGenitive(date[1].get(Calendar.MONTH)) + " " +
                            date[1].get(Calendar.YEAR);
                }
                tv_diapason.setText(tv_diapasonText);

                tv_dateText = date[1].get(Calendar.DAY_OF_MONTH) + " " +
                        CalendarText.getNameMonthGenitive(date[1].get(Calendar.MONTH)) + " " +
                        date[1].get(Calendar.YEAR);
                tv_date.setText(tv_dateText);

                temp.clear();
                temp.set(Calendar.MINUTE, finalValues.get((int)lastColumn.getX()));
                tv_valueText = formatCountTime.format(temp.getTime());
                tv_value.setText(tv_valueText);

                barChart.setOnChartValueSelectedListener(listenerModeWeek);
                break;

            case MONTH:
                tv_diapasonText  = CalendarText.getNameMonth(date[0].get(Calendar.MONTH)) + " " +
                        date[0].get(Calendar.YEAR);
                tv_diapason.setText(tv_diapasonText);

                tv_dateText = (int)(lastColumn.getX() + 1) + " " +
                        CalendarText.getNameMonthGenitive(date[0].get(Calendar.MONTH)) + " " +
                        date[0].get(Calendar.YEAR);
                tv_date.setText(tv_dateText);

                temp.clear();
                temp.set(Calendar.MINUTE, finalValues.get((int)lastColumn.getX()));
                tv_valueText = formatCountTime.format(temp.getTime());
                tv_value.setText(tv_valueText);

                barChart.setOnChartValueSelectedListener(listenerModeMonth);
                break;

            case YEAR:
                tv_diapasonText  = String.valueOf(date[0].get(Calendar.YEAR));
                tv_diapason.setText(tv_diapasonText);

                tv_dateText = CalendarText.getNameMonth((int)lastColumn.getX()) + " " + date[0].get(Calendar.YEAR);
                tv_date.setText(tv_dateText);

                temp.clear();
                temp.set(Calendar.MINUTE, finalValues.get((int)lastColumn.getX()));
                tv_valueText = formatCountTime.format(temp.getTime());
                tv_value.setText(tv_valueText);

                barChart.setOnChartValueSelectedListener(listenerModeYear);
                break;

            default:
                break;
        }

        barChart.setDrawBarShadow(false); //добавляет фон для колонок
        barChart.setDrawValueAboveBar(true); // делает надписи над колонками
        barChart.setMaxVisibleValueCount(250);
        barChart.setDrawGridBackground(false); // Добавляет серый фон сетки
        barChart.setScaleEnabled(false); // Убирает растягивания (можно отдельно по осям )
        barChart.setTouchEnabled(true); //уберает возможность нажатий

        Description description = barChart.getDescription(); //подпись(описание)
        description.setEnabled(false); //убирает подпись

        BarDataSet barDataSet = new BarDataSet(barEntries, "Время сна в часах"); //название диараммы

        barDataSet.setColors(activitySleepStat.getResources().getColor(R.color.blueMiddle));
        barDataSet.setHighLightColor(activitySleepStat.getResources().getColor(R.color.black));
        barDataSet.setDrawValues(false);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.6f); //толщина колонки диаграммы
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new XAxisDate(modePeriod));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTypeface(Typeface.create("default", Typeface.BOLD));

        barChart.getAxisRight().setEnabled(false);
        YAxis yAxis = barChart.getAxisLeft() ;
        yAxis.setTextSize ( 12f ) ; // установить размер текста
        yAxis.setAxisMinimum(0f) ; // начинаем с нуля
        yAxis.setTypeface(Typeface.create("default", Typeface.BOLD));
        yAxis.setDrawGridLines(true);

        barChart.highlightValue(null, false);
        barChart.notifyDataSetChanged();
        barChart.animateY(1000);

    }

    private OnChartValueSelectedListener listenerModeWeek = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

            Calendar cursor = Calendar.getInstance();
            cursor.set(date[0].get(Calendar.YEAR), date[0].get(Calendar.MONTH), date[0].get(Calendar.DAY_OF_MONTH));

            cursor.add(Calendar.DAY_OF_MONTH, (int)(e.getX()));
            String tv_dateText = cursor.get(Calendar.DAY_OF_MONTH) + " " +
                    CalendarText.getNameMonthGenitive(cursor.get(Calendar.MONTH)) + " " +
                    cursor.get(Calendar.YEAR);
            tv_date.setText(tv_dateText);

            Calendar temp = Calendar.getInstance();
            temp.clear();
            temp.set(Calendar.MINUTE, finalValues.get((int)e.getX()));
            String tv_valueText = formatCountTime.format(temp.getTime());
            tv_value.setText(tv_valueText);

        }

        @Override
        public void onNothingSelected() {

        }
    };

    private OnChartValueSelectedListener listenerModeMonth = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

            String tv_dateText = (int)(e.getX() + 1) + " " +
                    CalendarText.getNameMonthGenitive(date[0].get(Calendar.MONTH)) + " " +
                    date[0].get(Calendar.YEAR);
            tv_date.setText(tv_dateText);

            Calendar temp = Calendar.getInstance();
            temp.clear();
            temp.set(Calendar.MINUTE, finalValues.get((int)e.getX()));
            String tv_valueText = formatCountTime.format(temp.getTime());
            tv_value.setText(tv_valueText);
        }

        @Override
        public void onNothingSelected() {

        }
    };

    private OnChartValueSelectedListener listenerModeYear = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

            String tv_dateText = CalendarText.getNameMonth((int)(e.getX())) + " " + date[0].get(Calendar.YEAR);
            tv_date.setText(tv_dateText);

            Calendar temp = Calendar.getInstance();
            temp.clear();
            temp.set(Calendar.MINUTE, finalValues.get((int)e.getX()));
            String tv_valueText = formatCountTime.format(temp.getTime());
            tv_value.setText(tv_valueText);
        }

        @Override
        public void onNothingSelected() {

        }
    };

}