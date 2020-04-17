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

public class LoadingBarChartNurtControl extends AsyncTask<Void, Void, Void> {

    private Activity activityNutrControlStat;
    private BarChart barChart;
    private TextView tv_sum;
    private TextView tv_average;
    private TextView tv_date;
    private TextView tv_value;
    private TextView tv_diapason;

    private ModePeriod modePeriod;
    private ModeMicroelement modeElement;
    private TableNutrControl tableNutrControl;
    private List<ValueUserFoodMenuHelper> listFullValue;
    private List<Float> listValue;
    private List<InfoAboutDay> infoAboutDays;
    private Calendar[] date;
    private ArrayList<BarEntry> barEntries;
    private float sum;
    private float average;
    private float count;

    private class InfoAboutDay {
        private int day;
        private int month;
        private int year;
        private float value;

        public InfoAboutDay(int day, int month, int year, float value) {
            this.day = day;
            this.month = month;
            this.year = year;
            this.value = value;
        }
    }

    LoadingBarChartNurtControl(Activity activity, Calendar[] date, ModePeriod modePeriod, ModeMicroelement modeElement) {
        this.activityNutrControlStat = activity;
        tableNutrControl = new TableNutrControl(activityNutrControlStat.getApplicationContext());
        this.date = date;
        this.modePeriod = modePeriod;
        this.modeElement = modeElement;
        barEntries = new ArrayList<>();
        sum = 0;
        average = 0;
        count = 0;
    }

    @Override
    protected Void doInBackground(Void... params) {

        switch (modePeriod) {

            case WEEK:
                listFullValue = tableNutrControl.selectWeekInfo(date[0], date[1]);
                listValue = getListValue();
                infoAboutDays = getInfoAboutDays();

                Calendar cursor = Calendar.getInstance();
                cursor.set(date[0].get(Calendar.YEAR), date[0].get(Calendar.MONTH), date[0].get(Calendar.DAY_OF_MONTH));

                for (int i = 0; i < 7; i++) {
                    barEntries.add(new BarEntry(i, 0));
                }

                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < infoAboutDays.size(); j++) {
                        if (cursor.get(Calendar.DAY_OF_MONTH) == infoAboutDays.get(j).day &&
                                infoAboutDays.get(j).value != 0) {
                            barEntries.set(i, new BarEntry(i, infoAboutDays.get(j).value));
                            sum += infoAboutDays.get(j).value;
                            count++;
                        }
                    }
                    cursor.add(Calendar.DAY_OF_MONTH, 1);
                }
                average = sum / count;
                break;

            case MONTH:
                listFullValue = tableNutrControl.selectMonthInfo(date[0]);
                listValue = getListValue();
                infoAboutDays = getInfoAboutDays();

                for (int i = 0; i < date[0].getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    barEntries.add(new BarEntry(i, 0));
                }

                for (int i = 0; i < infoAboutDays.size(); i++) {
                    if (infoAboutDays.get(i).value != 0) {
                        barEntries.set(infoAboutDays.get(i).day - 1, new BarEntry(infoAboutDays.get(i).day - 1, infoAboutDays.get(i).value));
                        sum += infoAboutDays.get(i).value;
                        count++;
                    }
                }
                average = sum / count;
                break;

            case YEAR:
                listFullValue = tableNutrControl.selectYearInfo(date[0]);
                listValue = getListValue();
                infoAboutDays = getInfoAboutDays();

                int sums[] = new int[12];
                int counts[] = new int[12];

                for (int i = 0; i < infoAboutDays.size(); i++) {
                    if (infoAboutDays.get(i).value != 0) {
                        sums[infoAboutDays.get(i).month - 1] += infoAboutDays.get(i).value;
                        counts[infoAboutDays.get(i).month - 1]++;

                        sum += infoAboutDays.get(i).value;
                        count++;
                    }
                }
                ArrayList<Integer> averages = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    averages.add((int) ((float) sums[i] / (float) counts[i]));
                    barEntries.add(new BarEntry(i, averages.get(i)));
                }
                average = sum / count;
                break;

            default:
                break;
        }
        return null;
    }

    private List<InfoAboutDay> getInfoAboutDays() {
        infoAboutDays = new ArrayList<>();

        if (listFullValue.size() != 0) {
            int day = listFullValue.get(0).getDayOfMonth();
            int month = listFullValue.get(0).getMonth();
            int year = listFullValue.get(0).getYear();
            float value = 0;

            for (int i = 0; i < listFullValue.size(); i++) {
                if (day == listFullValue.get(i).getDayOfMonth() &&
                        month == listFullValue.get(i).getMonth() &&
                        year == listFullValue.get(i).getYear()) {

                    day = listFullValue.get(i).getDayOfMonth();
                    month = listFullValue.get(i).getMonth();
                    year = listFullValue.get(i).getYear();

                    value += listValue.get(i);
                } else {
                    infoAboutDays.add(new InfoAboutDay(day, month, year, value));

                    day = listFullValue.get(i).getDayOfMonth();
                    month = listFullValue.get(i).getMonth();
                    year = listFullValue.get(i).getYear();

                    value = listValue.get(i);
                }
            }

            infoAboutDays.add(new InfoAboutDay(day, month, year, value));
        }

        return infoAboutDays;
    }

    private List<Float> getListValue() {
        listValue = new ArrayList<>();
        switch (modeElement) {
            case PROTEIN:
                for (int i = 0; i < listFullValue.size(); i++) {
                    listValue.add((float) (listFullValue.get(i).getProductProtein() * listFullValue.get(i).getWeight() / 100));
                }
                break;

            case FAT:
                for (int i = 0; i < listFullValue.size(); i++) {
                    listValue.add((float) (listFullValue.get(i).getProductFat() * listFullValue.get(i).getWeight() / 100));
                }
                break;

            case CARBOHYDRATE:
                for (int i = 0; i < listFullValue.size(); i++) {
                    listValue.add((float) (listFullValue.get(i).getProductCarbohyd() * listFullValue.get(i).getWeight() / 100));
                }
                break;

            case CALORIES:
                for (int i = 0; i < listFullValue.size(); i++) {
                    listValue.add((float) (listFullValue.get(i).getProductCalories()) * listFullValue.get(i).getWeight() / 100);
                }
                break;

            default:
                break;
        }
        return listValue;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        drawBarChart();
    }

    private void drawBarChart() {
        barChart = activityNutrControlStat.findViewById(R.id.bc_water);

        tv_sum = activityNutrControlStat.findViewById(R.id.tv_sumNCStat);
        tv_average = activityNutrControlStat.findViewById(R.id.tv_averageNCStat);
        tv_value = activityNutrControlStat.findViewById(R.id.tv_valueNCStat);
        tv_diapason = activityNutrControlStat.findViewById(R.id.tv_diapasonNCStat);
        tv_date = activityNutrControlStat.findViewById(R.id.tv_dateNCStat);

        tv_sum.setText(String.valueOf((int)sum));
        tv_average.setText(String.valueOf((int)average));

        String tv_diapasonText;
        String tv_dateText;
        String tv_valueText;
        BarEntry lastColumn = barEntries.get(barEntries.size() - 1);

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

                tv_valueText = String.valueOf((int) lastColumn.getY());
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

                tv_valueText = String.valueOf((int) lastColumn.getY());
                tv_value.setText(tv_valueText);

                barChart.setOnChartValueSelectedListener(listenerModeMonth);
                break;

            case YEAR:
                tv_diapasonText  = String.valueOf(date[0].get(Calendar.YEAR));
                tv_diapason.setText(tv_diapasonText);

                tv_dateText = CalendarText.getNameMonth((int)lastColumn.getX()) + " " + date[0].get(Calendar.YEAR);
                tv_date.setText(tv_dateText);

                tv_valueText = String.valueOf((int) lastColumn.getY());
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

        BarDataSet barDataSet = new BarDataSet(barEntries, getLabel()); //название диараммы

        barDataSet.setColors(activityNutrControlStat.getResources().getColor(R.color.blueMiddle));
        barDataSet.setHighLightColor(activityNutrControlStat.getResources().getColor(R.color.black));
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

    private String getLabel() {
        switch (modeElement) {
            case PROTEIN:
                return "Кол-во потребленных белков в граммах";
            case FAT:
                return "Кол-во потребленных жиров в граммах";
            case CARBOHYDRATE:
                return "Кол-во потребленных углеводов в граммах";
            case CALORIES:
                return "Кол-во потребленных килокалорий";
        }
        return "";
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

            String tv_valueText = String.valueOf((int) e.getY());
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

            String tv_valueText = String.valueOf((int) e.getY());
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

            String tv_valueText = String.valueOf((int) e.getY());
            tv_value.setText(tv_valueText);
        }

        @Override
        public void onNothingSelected() {

        }
    };
}