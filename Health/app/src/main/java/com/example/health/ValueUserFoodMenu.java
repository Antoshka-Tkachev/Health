package com.example.health;

import java.util.ArrayList;

public class ValueUserFoodMenu {

    private ArrayList<String> nameMenus;
    private ArrayList<ValueUserFoodMenuHelper> productsInFoodMenu;

    private String name;
    private int number;
    private long userId;

    private static ValueUserFoodMenu instance;

    private ValueUserFoodMenu(){}

    public static ValueUserFoodMenu getInstance(){
        if (instance == null){		//если объект еще не создан
            instance = new ValueUserFoodMenu();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }

    public ArrayList<String> getNameMenus() {
        return nameMenus;
    }

    public void setNameMenus(ArrayList<String> nameMenus) {
        this.nameMenus = nameMenus;
    }

    public ArrayList<ValueUserFoodMenuHelper> getProductsInFoodMenu() {
        return productsInFoodMenu;
    }

    public void setProductsInFoodMenu(ArrayList<ValueUserFoodMenuHelper> productsInFoodMenu) {
        this.productsInFoodMenu = productsInFoodMenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void createNewName() {
        String newName = "Новое меню";
        boolean nameIsFree = true;

        for (int i = 0; i < nameMenus.size(); i++) {
            if (nameMenus.get(i).equals(newName)) {
                nameIsFree = false;
                break;
            }
        }

        if (nameIsFree) {
            name = newName;
            return;
        }

        int count = 1;
        String bufferNewName = "";
        while (!nameIsFree) {
            bufferNewName = newName + count;
            nameIsFree = true;
            for (int i = 0; i < nameMenus.size(); i++) {
                if (nameMenus.get(i).equals(bufferNewName)) {
                    nameIsFree = false;
                    count++;
                    break;
                }
            }
        }

        name = bufferNewName;
    }
}
