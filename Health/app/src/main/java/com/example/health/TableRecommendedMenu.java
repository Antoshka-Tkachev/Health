package com.example.health;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class TableRecommendedMenu {

    private final String TABLE_NAME = "RecommendedMenu";
    private final String COLUMN_ID = "id";
    private final String COLUMN_NUMBER = "number";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_DAY_OF_WEEK = "day_of_week";
    private final String COLUMN_EATING = "eating";
    private final String COLUMN_WEIGHT = "weight";
    private final String COLUMN_PRODUCT_NAME = "product_name";
    private final String COLUMN_PRODUCT_CALORIES = "product_calories";
    private final String COLUMN_PRODUCT_PROTEIN = "product_protein";
    private final String COLUMN_PRODUCT_FAT = "product_fat";
    private final String COLUMN_PRODUCT_CARBOHYDRATE = "product_carbohydrate";

    private int indexId;
    private int indexNumber;
    private int indexName;
    private int indexDayOfWeek;
    private int indexEating;
    private int indexWeight;
    private int indexProductName;
    private int indexProductCalories;
    private int indexProductProtein;
    private int indexProductFat;
    private int indexProductCarbohydrate;

    private DBHelperRecommendedMenu dbHelper;
    private SQLiteDatabase database;
    private ValueRecommendedMenu valueRecomMenu;

    public TableRecommendedMenu(Context context) {

        dbHelper = new DBHelperRecommendedMenu(context);

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(TABLE_NAME,null, null, null, null, null, null);

        indexId = cursor.getColumnIndex(COLUMN_ID);
        indexNumber = cursor.getColumnIndex(COLUMN_NUMBER);
        indexName = cursor.getColumnIndex(COLUMN_NAME);
        indexDayOfWeek = cursor.getColumnIndex(COLUMN_DAY_OF_WEEK);
        indexEating = cursor.getColumnIndex(COLUMN_EATING);
        indexWeight = cursor.getColumnIndex(COLUMN_WEIGHT);
        indexProductName = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
        indexProductCalories = cursor.getColumnIndex(COLUMN_PRODUCT_CALORIES);
        indexProductProtein = cursor.getColumnIndex(COLUMN_PRODUCT_PROTEIN);
        indexProductFat = cursor.getColumnIndex(COLUMN_PRODUCT_FAT);
        indexProductCarbohydrate = cursor.getColumnIndex(COLUMN_PRODUCT_CARBOHYDRATE);

        cursor.close();
        database.close();
    }

    public void selectTable() {
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Log.d("TABLE_FOOD_MENU", cursor.getInt(indexId) + " " +
                    cursor.getString(indexNumber).replace(',', '.') + " " +
                    cursor.getString(indexName).replace(',', '.') + " " +
                    cursor.getString(indexDayOfWeek).replace(',', '.') + " " +
                    cursor.getString(indexEating).replace(',', '.') + " " +
                    cursor.getString(indexWeight).replace(',', '.') + " " +
                    cursor.getString(indexProductName).replace(',', '.') + " " +
                    cursor.getString(indexProductCalories).replace(',', '.') + " " +
                    cursor.getString(indexProductProtein).replace(',', '.') + " " +
                    cursor.getString(indexProductFat).replace(',', '.') + " " +
                    cursor.getString(indexProductCarbohydrate).replace(',', '.'));
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
    }

    public ValueRecommendedMenu selectMenu(int numberMenu) {
        valueRecomMenu = new ValueRecommendedMenu();
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_NUMBER + " = ? ",
                new String[] {String.valueOf(numberMenu) });
        cursor.moveToFirst();

        valueRecomMenu.setNameMenu(cursor.getString(indexName));

        ArrayList<ValueUserFoodMenuHelper> productsInRecomMenu = new ArrayList<>();
        ValueUserFoodMenuHelper buffer;
        while (!cursor.isAfterLast()) {
            buffer = new ValueUserFoodMenuHelper();

            buffer.setDayOfWeek(Integer.parseInt(cursor.getString(indexDayOfWeek).replace(',', '.')));
            buffer.setEating(Integer.parseInt(cursor.getString(indexEating).replace(',', '.')));
            buffer.setWeight(Integer.parseInt(cursor.getString(indexWeight).replace(',', '.')));
            buffer.setProductName(cursor.getString(indexProductName).replace(',', '.'));
            buffer.setProductCalories(Double.parseDouble(cursor.getString(indexProductCalories).replace(',', '.')));
            buffer.setProductProtein(Double.parseDouble(cursor.getString(indexProductProtein).replace(',', '.')));
            buffer.setProductFat(Double.parseDouble(cursor.getString(indexProductFat).replace(',', '.')));
            buffer.setProductCarbohyd(Double.parseDouble(cursor.getString(indexProductCarbohydrate).replace(',', '.')));

            productsInRecomMenu.add(buffer);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        valueRecomMenu.setProductsInFoodMenu(productsInRecomMenu);

        return valueRecomMenu;
    }

    public ArrayList<String> selectNamesMenu() {
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT DISTINCT " + COLUMN_NAME +
                        " FROM " + TABLE_NAME +
                        " ORDER BY " + COLUMN_NUMBER, null);
        cursor.moveToFirst();

        ArrayList<String> namesMenu = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            namesMenu.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return namesMenu;
    }

}