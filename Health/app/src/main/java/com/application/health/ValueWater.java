package com.application.health;

public class ValueWater {

    private int value;
    private int goalValue;
    private int day;
    private int month;
    private int year;
    private long userId;

    private static ValueWater instance;

    private ValueWater(){}

    public static ValueWater getInstance(){
        if(instance == null){		//если объект еще не создан
            instance = new ValueWater();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(int goalValue) {
        this.goalValue = goalValue;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
