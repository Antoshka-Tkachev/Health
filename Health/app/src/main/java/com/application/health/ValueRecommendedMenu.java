package com.application.health;

import java.util.ArrayList;

public class ValueRecommendedMenu {
    private ArrayList<String> nameMenus;
    private ArrayList<ValueUserFoodMenuHelper> productsInFoodMenu;
    private String nameMenu;

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

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }
}
