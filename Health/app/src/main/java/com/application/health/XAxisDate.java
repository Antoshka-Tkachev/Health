package com.application.health;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class XAxisDate extends ValueFormatter {

    private  String[] values;

    public XAxisDate(ModePeriod mode) {
        switch (mode) {
            case WEEK:
                this.values = new String[] {"Пн", "Вт",
                    "Ср", "Чт", "Пт", "Сб", "Вс", "Пн"};
                break;
            case MONTH:
                this.values = new String[] {"1", "2",
                    "3", "4", "5", "6", "7", "8", "9",
                    "10", "11", "12", "13", "14", "15", "16",
                    "17", "18", "19", "20", "21", "22" , "23",
                    "24", "25", "26", "27", "28", "29", "30", "31"};
                break;
            case YEAR:
                this.values = new String[] {"Янв", "Февр",
                        "Март", "Апр", "Май", "Июнь", "Июль", "Авг", "Сент", "Окт", "Нояб", "Дек"};
                break;
        }
    }

    @Override
    public String getFormattedValue(float value) {
        return values[(int)value];
    }
}
