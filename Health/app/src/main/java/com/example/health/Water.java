package com.example.health;

public class Water {
    private int value;
    private int goalValue;

    private static Water instance;

    private Water(){}

    public static Water getInstance(){
        if(instance == null){		//если объект еще не создан
            instance = new Water();	//создать новый объект
            instance.value = 0;
            instance.goalValue = 2000;
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
}
