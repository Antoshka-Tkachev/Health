package com.application.health;

public class ValueSleep {

    private int fallingAsleep;
    private int wakingUp;
    private int duringSleep;
    private int day;
    private int month;
    private int year;
    private long userId;

    private static ValueSleep instance;

    private ValueSleep(){}

    public static ValueSleep getInstance(){
        if(instance == null){		//если объект еще не создан
            instance = new ValueSleep();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }

    public int getFallingAsleep() {
        return fallingAsleep;
    }

    public void setFallingAsleep(int fallingAsleep) {
        this.fallingAsleep = fallingAsleep;
    }

    public int getWakingUp() {
        return wakingUp;
    }

    public void setWakingUp(int wakingUp) {
        this.wakingUp = wakingUp;
    }

    public int getDuringSleep() {
        return duringSleep;
    }

    public void setDuringSleep(int duringSleep) {
        this.duringSleep = duringSleep;
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
