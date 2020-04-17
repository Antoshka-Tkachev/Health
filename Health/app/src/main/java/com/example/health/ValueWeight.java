package com.example.health;

public class ValueWeight {

    private double weight;
    private int day;
    private int month;
    private int year;
    private long userId;

    private static ValueWeight instance;

    private ValueWeight(){}

    public static ValueWeight getInstance(){
        if(instance == null){		//если объект еще не создан
            instance = new ValueWeight();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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
