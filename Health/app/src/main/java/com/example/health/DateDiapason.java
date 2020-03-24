package com.example.health;

import java.util.Calendar;

public class DateDiapason {

    private Calendar date;

    private Calendar dateStartWeek;
    private Calendar dateEndWeek;

    private Calendar dateStatByMonth;
    private Calendar dateStatByYear;

    public DateDiapason() {

        date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 12);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);

        dateStatByMonth = Calendar.getInstance();
        dateStatByMonth.set(Calendar.MONTH, date.get(Calendar.MONTH));
        dateStatByMonth.set(Calendar.YEAR, date.get(Calendar.YEAR));

        dateStatByYear = Calendar.getInstance();
        dateStatByYear.set(Calendar.YEAR, date.get(Calendar.YEAR));

    }

    public Calendar getDateStartWeek() {
        return dateStartWeek;
    }

    public Calendar getDateEndWeek() {
        return dateEndWeek;
    }

    public Calendar getDateStatByMonth() {
        return dateStatByMonth;
    }

    public Calendar getDateStatByYear() {
        return dateStatByYear;
    }

    public Calendar calculatingNextYear() {
        dateStatByYear.add(Calendar.YEAR, 1);
        return dateStatByYear;
    }

    public Calendar calculatingLastYear() {
        dateStatByYear.add(Calendar.YEAR, -1);
        return dateStatByYear;

    }

    public Calendar calculatingNextMonth() {
        dateStatByMonth.add(Calendar.MONTH, 1);
        return dateStatByMonth;

    }

    public Calendar calculatingLastMonth() {
        dateStatByMonth.add(Calendar.MONTH, -1);
        return dateStatByMonth;
    }

    public void calculatingNextWeekDates() {
        dateStartWeek.add(Calendar.DAY_OF_MONTH, 7);
        dateEndWeek.add(Calendar.DAY_OF_MONTH, 7);
    }

    public void calculatingLastWeekDates() {
        dateStartWeek.add(Calendar.DAY_OF_MONTH, -7);
        dateEndWeek.add(Calendar.DAY_OF_MONTH, -7);
    }

    public void calculatingWeekDates() {

        //устанавливаем дату первого дня этого месяца
        Calendar firstDayOfMonth = Calendar.getInstance();
        firstDayOfMonth.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);

        //устанавливаем число и день недели первого возможного понедельника
        int dayOfMonthOfFirstMonday = 1;
        int dayOfWeekOfFirstMonday = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);

        //ищем число первого понедельника
        while (dayOfWeekOfFirstMonday != Calendar.MONDAY) {
            dayOfMonthOfFirstMonday++;
            dayOfWeekOfFirstMonday++;
            if (dayOfWeekOfFirstMonday == 8) {
                dayOfWeekOfFirstMonday = 1;
            }
        }
        //найден dayOfMonthOfFirstMonday - число первого понедельника

        dateStartWeek = Calendar.getInstance();
        dateStartWeek.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), dayOfMonthOfFirstMonday, 0, 0, 0);
        dateStartWeek.add(Calendar.DAY_OF_MONTH, -7);

        dateEndWeek = Calendar.getInstance();
        dateEndWeek.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), dayOfMonthOfFirstMonday, 0, 0, 0);

        while (!(date.after(dateStartWeek) && date.before(dateEndWeek))) {
            dateStartWeek.add(Calendar.DAY_OF_MONTH, 7);
            dateEndWeek.add(Calendar.DAY_OF_MONTH, 7);
        }
        dateEndWeek.add(Calendar.DAY_OF_MONTH, -1);
    }
}
