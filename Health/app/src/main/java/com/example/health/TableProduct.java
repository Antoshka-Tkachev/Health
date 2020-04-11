package com.example.health;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class TableProduct {
    private final String TABLE_NAME = "product";
    private final String COLUMN_ID = "id";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_CALORIES = "calories";
    private final String COLUMN_PROTEIN = "protein";
    private final String COLUMN_FAT = "fat";
    private final String COLUMN_CARBOHYDRATE = "carbohydrate";
    private final String COLUMN_USER_ID = "user_id";

    private int indexId;
    private int indexName;
    private int indexCalories;
    private int indexProtein;
    private int indexFat;
    private int indexCarbohydrate;
    private int indexUserId;

    private DBHelperProduct dbHelper;
    private SQLiteDatabase database;
    private ValueUserFoodMenu userFoodMenu;

    public TableProduct(Context context) {
        dbHelper = new DBHelperProduct(context);
        userFoodMenu = ValueUserFoodMenu.getInstance();

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME,null, null, null, null, null, null);

        indexId = cursor.getColumnIndex(COLUMN_ID);
        indexName = cursor.getColumnIndex(COLUMN_NAME);
        indexCalories = cursor.getColumnIndex(COLUMN_CALORIES);
        indexProtein = cursor.getColumnIndex(COLUMN_PROTEIN);
        indexFat = cursor.getColumnIndex(COLUMN_FAT);
        indexCarbohydrate = cursor.getColumnIndex(COLUMN_CARBOHYDRATE);
        indexUserId = cursor.getColumnIndex(COLUMN_USER_ID);

        cursor.close();
        database.close();
    }

    public void selectTable() {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                " WHERE " +
                        COLUMN_USER_ID + " = NULL OR " +
                        COLUMN_USER_ID + " = ?",
                new String[] { String.valueOf(userFoodMenu.getUserId()) });

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.d("TABLE_PRODUCT",
                    cursor.getInt(indexId) + " " +
                    cursor.getString(indexName) + " " +
                    cursor.getDouble(indexCalories) + " " +
                    cursor.getDouble(indexProtein) + " " +
                    cursor.getDouble(indexFat) + " " +
                    cursor.getDouble(indexCarbohydrate)
                    );

            cursor.moveToNext();
        }

        cursor.close();
        database.close();
    }

    public ArrayList<String> selectProductNames() {
        ArrayList<String> productNames = new ArrayList<>();

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " + COLUMN_NAME +
                " FROM " + TABLE_NAME +
                " WHERE " +
                    COLUMN_USER_ID + " IS NULL OR " +
                    COLUMN_USER_ID + " = ?",
                new String[] { String.valueOf(userFoodMenu.getUserId()) });
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            productNames.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return productNames;
    }

    public Product selectProduct(String productName) {
        Product product = new Product();

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT *  FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_NAME + " = ?  AND (" +
                        COLUMN_USER_ID + " IS NULL OR " +
                        COLUMN_USER_ID + " = ? ) ",
                new String[] { productName, String.valueOf(userFoodMenu.getUserId()) });
        cursor.moveToFirst();

        product.setName(cursor.getString(indexName));
        product.setCalories(Double.parseDouble(cursor.getString(indexCalories).replace(',', '.')));
        product.setProtein(Double.parseDouble(cursor.getString(indexProtein).replace(',', '.')));
        product.setFat(Double.parseDouble(cursor.getString(indexFat).replace(',', '.')));
        product.setCarbohydrate(Double.parseDouble(cursor.getString(indexCarbohydrate).replace(',', '.')));

        cursor.close();
        database.close();

        return product;
    }

    public boolean isExistRecord(String name) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_NAME + " = ?  AND (" +
                        COLUMN_USER_ID + " IS NULL OR " +
                        COLUMN_USER_ID + " = ? ) ",
                new String[] { name, String.valueOf(userFoodMenu.getUserId())});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            cursor.close();
            database.close();
            return false;
        } else {
            cursor.close();
            database.close();
            return true;
        }
    }

    public void insertProduct(Product product) {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, product.getName());
        cv.put(COLUMN_CALORIES, product.getCalories());
        cv.put(COLUMN_PROTEIN, product.getProtein());
        cv.put(COLUMN_FAT, product.getFat());
        cv.put(COLUMN_CARBOHYDRATE, product.getCarbohydrate());
        cv.put(COLUMN_USER_ID, userFoodMenu.getUserId());
        database.insert(TABLE_NAME, null, cv);

        database.close();
    }
}
