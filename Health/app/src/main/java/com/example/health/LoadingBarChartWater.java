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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LoadingBarChartWater extends AsyncTask<String, Void, String> {

    private Activity activityWaterStatistics;
    private BarChart barChart;
    private TextView tv_sum;
    private TextView tv_average;
    private TextView tv_date;
    private TextView tv_value;
    private TextView tv_diapason;

    private TableValueWater tableValueWater;
    private List<ValueWaterHelper> listValue;
    private Calendar[] date;
    private ArrayList<BarEntry> barEntries;
    private int sum;
    private int average;
    private int count;

    LoadingBarChartWater(Activity activityWaterStatistics, Calendar[] date) {
        this.activityWaterStatistics = activityWaterStatistics;
        tableValueWater = new TableValueWater(activityWaterStatistics.getApplicationContext());
        this.date = date;
        barEntries = new ArrayList<>();
        sum = 0;
        average = 0;
        count = 0;
    }

    @Override
    protected String doInBackground(String... diapason) {

        switch (diapason[0]) {

            case "Week":
                listValue = tableValueWater.selectWeekInfo(date[0], date[1]);

                Calendar cursor = Calendar.getInstance();
                cursor.set(date[0].get(Calendar.YEAR), date[0].get(Calendar.MONTH), date[0].get(Calendar.DAY_OF_MONTH));

                for (int i = 0; i < 7; i++) {
                    barEntries.add(new BarEntry(i, 0));
                }

                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < listValue.size(); j++) {
                        if (cursor.get(Calendar.DAY_OF_MONTH) == listValue.get(j).getDay() &&
                            listValue.get(j).getValue() != 0) {
                            barEntries.set(i, new BarEntry(i, listValue.get(j).getValue()));
                            sum += listValue.get(j).getValue();
                            count++;
                        }
                    }
                    cursor.add(Calendar.DAY_OF_MONTH, 1);
                }
                average = (int) ((float) sum / (float) count);
                return "Week";

            case "Month":
                listValue = tableValueWater.selectMonthInfo(date[0]);

                for (int i = 0; i < date[0].getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    barEntries.add(new BarEntry(i, 0));
                }

                for (int i = 0; i < listValue.size(); i++) {
                    if (listValue.get(i).getValue() != 0) {
                        barEntries.set(listValue.get(i).getDay() - 1, new BarEntry(listValue.get(i).getDay() - 1, listValue.get(i).getValue()));
                        sum += listValue.get(i).getValue();
                        count++;
                    }
                }
                average = (int) ((float) sum / (float) count);
                return "Month";

            case "Year":
                listValue = tableValueWater.selectYearInfo(date[0]);

                int sums[] = new int[12];
                int counts[] = new int[12];

                for (int i = 0; i < listValue.size(); i++) {
                    if (listValue.get(i).getValue() != 0) {
                        sums[listValue.get(i).getMonth() - 1] += listValue.get(i).getValue();
                        counts[listValue.get(i).getMonth() - 1]++;

                        sum += listValue.get(i).getValue();
                        count++;
                    }
                }
                ArrayList<Integer> averages = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    averages.add((int) ((float) sums[i] / (float) counts[i]));
                    barEntries.add(new BarEntry(i, averages.get(i)));
                }
                average = (int) ((float) sum / (float) count);
                return "Year";

            default:
                break;
        }
        return "0";
    }

    @Override
    protected void onPostExecute(String modeBarChart) {
        super.onPostExecute(modeBarChart);
        drawBarChart(modeBarChart);
    }

    private String getNameMonthGenitive(int month) {
        switch (month) {
            case 0:
                return "января";
            case 1:
                return "февраля";
            case 2:
                return "марта";
            case 3:
                return "апреля";
            case 4:
                return "мая";
            case 5:
                return "июня";
            case 6:
                return "июля";
            case 7:
                return "августа";
            case 8:
                return "сентября";
            case 9:
                return "октября";
            case 10:
                return "ноября";
            case 11:
                return "декабря";
            default:
                return "";
        }
    }

    private String getNameMonth(int month) {
        switch (month) {
            case 0:
                return "Январь";
            case 1:
                return "Февраль";
            case 2:
                return "Март";
            case 3:
                return "Апрель";
            case 4:
                return "Май";
            case 5:
                return "Июнь";
            case 6:
                return "Июль";
            case 7:
                return "Август";
            case 8:
                return "Сентябрь";
            case 9:
                return "Октябрь";
            case 10:
                return "Ноябрь";
            case 11:
                return "Декабрь";
            default:
                return "";
        }
    }

    private void drawBarChart(String modeBarChart) {
        barChart = activityWaterStatistics.findViewById(R.id.bc_water);

        tv_sum = activityWaterStatistics.findViewById(R.id.tv_sumWatStat);
        tv_average = activityWaterStatistics.findViewById(R.id.tv_averageWatStat);
        tv_value = activityWaterStatistics.findViewById(R.id.tv_valueWatStat);
        tv_diapason = activityWaterStatistics.findViewById(R.id.tv_diapasonWatStat);
        tv_date = activityWaterStatistics.findViewById(R.id.tv_dateWatStat);

        tv_sum.setText(String.valueOf(sum));
        tv_average.setText(String.valueOf(average));

        String tv_diapasonText;
        String tv_dateText;
        String tv_valueText;
        BarEntry lastColumn = barEntries.get(barEntries.size() - 1);

        switch (modeBarChart) {
            case "Week":
                if (date[0].get(Calendar.YEAR) == date[1].get(Calendar.YEAR)) {
                    if (date[0].get(Calendar.MONTH) == date[1].get(Calendar.MONTH)) {
                        tv_diapasonText =
                                date[0].get(Calendar.DAY_OF_MONTH) + " - " +
                                date[1].get(Calendar.DAY_OF_MONTH) + " " +
                                getNameMonthGenitive(date[1].get(Calendar.MONTH)) + " " +
                                date[1].get(Calendar.YEAR);
                    } else {
                        tv_diapasonText =
                                date[0].get(Calendar.DAY_OF_MONTH) + " " +
                                getNameMonthGenitive(date[0].get(Calendar.MONTH)) + " - " +
                                date[1].get(Calendar.DAY_OF_MONTH) + " " +
                                getNameMonthGenitive(date[1].get(Calendar.MONTH)) + "\n" +
                                date[1].get(Calendar.YEAR);
                    }
                } else {
                    tv_diapasonText =
                            date[0].get(Calendar.DAY_OF_MONTH) + " " +
                            getNameMonthGenitive(date[0].get(Calendar.MONTH)) + " " +
                            date[0].get(Calendar.YEAR) + " -\n" +
                            date[1].get(Calendar.DAY_OF_MONTH) + " " +
                            getNameMonthGenitive(date[1].get(Calendar.MONTH)) + " " +
                            date[1].get(Calendar.YEAR);
                }
                tv_diapason.setText(tv_diapasonText);

                tv_dateText = date[1].get(Calendar.DAY_OF_MONTH) + " " +
                        getNameMonthGenitive(date[1].get(Calendar.MONTH)) + " " +
                        date[1].get(Calendar.YEAR);
                tv_date.setText(tv_dateText);

                tv_valueText = (int) lastColumn.getY() + " мл";
                tv_value.setText(tv_valueText);

                barChart.setOnChartValueSelectedListener(listenerModeWeek);
                break;

            case "Month":
                tv_diapasonText  = getNameMonth(date[0].get(Calendar.MONTH)) + " " +
                        date[0].get(Calendar.YEAR);
                tv_diapason.setText(tv_diapasonText);

                tv_dateText = (int)(lastColumn.getX() + 1) + " " +
                        getNameMonthGenitive(date[0].get(Calendar.MONTH)) + " " +
                        date[0].get(Calendar.YEAR);
                tv_date.setText(tv_dateText);

                tv_valueText = (int) lastColumn.getY() + " мл";
                tv_value.setText(tv_valueText);

                barChart.setOnChartValueSelectedListener(listenerModeMonth);
                break;

            case "Year":
                tv_diapasonText  = String.valueOf(date[0].get(Calendar.YEAR));
                tv_diapason.setText(tv_diapasonText);

                tv_dateText = getNameMonth((int)lastColumn.getX()) + " " + date[0].get(Calendar.YEAR);
                tv_date.setText(tv_dateText);

                tv_valueText = (int) lastColumn.getY() + " мл";
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

        BarDataSet barDataSet = new BarDataSet(barEntries, "Количество выпитой воды в мл"); //название диараммы

        barDataSet.setColors(activityWaterStatistics.getResources().getColor(R.color.blueMiddle));
        barDataSet.setHighLightColor(activityWaterStatistics.getResources().getColor(R.color.black));
        barDataSet.setDrawValues(false);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.6f); //толщина колонки диаграммы
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new XAxisDate(modeBarChart));
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
                    getNameMonthGenitive(cursor.get(Calendar.MONTH)) + " " +
                    cursor.get(Calendar.YEAR);
            tv_date.setText(tv_dateText);

            String tv_valueText = (int) e.getY() + " мл";
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
                    getNameMonthGenitive(date[0].get(Calendar.MONTH)) + " " +
                    date[0].get(Calendar.YEAR);
            tv_date.setText(tv_dateText);

            String tv_valueText = (int) e.getY() + " мл";
            tv_value.setText(tv_valueText);

        }

        @Override
        public void onNothingSelected() {

        }
    };

    private OnChartValueSelectedListener listenerModeYear = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

            String tv_dateText = getNameMonth((int)(e.getX())) + " " + date[0].get(Calendar.YEAR);
            tv_date.setText(tv_dateText);

            String tv_valueText = (int) e.getY() + " мл";
            tv_value.setText(tv_valueText);
        }

        @Override
        public void onNothingSelected() {

        }
    };

}