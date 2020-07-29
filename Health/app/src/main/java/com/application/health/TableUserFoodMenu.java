package com.application.health;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

public class TableUserFoodMenu {

    private final String TABLE_NAME = "UserFoodMenu";
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
    private final String COLUMN_USER_ID = "user_id";

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
    private int indexUserId;

    private ValueUserFoodMenu userFoodMenu;

    private DBHelperUserFoodMenu dbHelper;
    private SQLiteDatabase database;

    public TableUserFoodMenu(Context context) {

        dbHelper = new DBHelperUserFoodMenu(context);
        userFoodMenu = ValueUserFoodMenu.getInstance();

        database = dbHelper.getWritableDatabase();
        createTable();

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
        indexUserId = cursor.getColumnIndex(COLUMN_USER_ID);

        cursor.close();
        database.close();
    }

    private void createTable() {

        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NUMBER + " INTEGER, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DAY_OF_WEEK + " INTEGER, " +
                COLUMN_EATING + " INTEGER, " +
                COLUMN_WEIGHT + " INTEGER, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PRODUCT_CALORIES + " DOUBLE, " +
                COLUMN_PRODUCT_PROTEIN + " DOUBLE, " +
                COLUMN_PRODUCT_FAT + " DOUBLE, " +
                COLUMN_PRODUCT_CARBOHYDRATE + " DOUBLE, " +
                COLUMN_USER_ID + " INTEGER );");
    }

    public void selectTable() {
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Log.d("TABLE_FOOD_MENU", cursor.getInt(indexId) + " " +
                    cursor.getInt(indexNumber) + " " +
                    cursor.getString(indexName) + " " +
                    cursor.getInt(indexDayOfWeek) + " " +
                    cursor.getInt(indexEating) + " " +
                    cursor.getInt(indexWeight) + " " +
                    cursor.getString(indexProductName) + " " +
                    cursor.getDouble(indexProductCalories) + " " +
                    cursor.getDouble(indexProductProtein) + " " +
                    cursor.getDouble(indexProductFat) + " " +
                    cursor.getDouble(indexProductCarbohydrate) + " " +
                    cursor.getInt(indexUserId));
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
    }

    public boolean isExistMenu(String name) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_NAME + " = ?  AND " +
                        COLUMN_USER_ID + " = ? ",
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

    public boolean isExistProduct(String nameProduct, String weight, int dayOfWeek, int modeEating) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_NAME + " = ? AND " +
                        COLUMN_DAY_OF_WEEK + " = ? AND " +
                        COLUMN_EATING + " = ? AND " +
                        COLUMN_WEIGHT + " = ? AND " +
                        COLUMN_PRODUCT_NAME + " = ? AND " +
                        COLUMN_USER_ID  + " = ? ",
                new String[] {
                        userFoodMenu.getName(),
                        String.valueOf(dayOfWeek),
                        String.valueOf(modeEating),
                        weight,
                        nameProduct,
                        String.valueOf(userFoodMenu.getUserId())
                });
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

    public void selectMenu(int numberMenu) {
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE " +
                COLUMN_NUMBER + " = ? AND " +
                COLUMN_USER_ID + " = ?",
                new String[] {String.valueOf(numberMenu), String.valueOf(userFoodMenu.getUserId()) });
        cursor.moveToFirst();

        userFoodMenu.setNumber(numberMenu);
        userFoodMenu.setName(cursor.getString(indexName));

        ArrayList<ValueUserFoodMenuHelper> productsInFoodMenu = new ArrayList<>();
        ValueUserFoodMenuHelper buffer;
        while (!cursor.isAfterLast()) {
            buffer = new ValueUserFoodMenuHelper();

            buffer.setDayOfWeek(cursor.getInt(indexDayOfWeek));
            buffer.setEating(cursor.getInt(indexEating));
            buffer.setWeight(cursor.getInt(indexWeight));
            buffer.setProductName(cursor.getString(indexProductName));
            buffer.setProductCalories(cursor.getDouble(indexProductCalories));
            buffer.setProductProtein(cursor.getDouble(indexProductProtein));
            buffer.setProductFat(cursor.getDouble(indexProductFat));
            buffer.setProductCarbohyd(cursor.getDouble(indexProductCarbohydrate));

            productsInFoodMenu.add(buffer);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        userFoodMenu.setProductsInFoodMenu(productsInFoodMenu);
    }

    public void addNewMenu() {
        database = dbHelper.getWritableDatabase();

        String updateQuery =
            "UPDATE " +
                TABLE_NAME +
                " SET " +
                COLUMN_NUMBER + " = " + COLUMN_NUMBER + " + 1 " +
                "WHERE " +
                COLUMN_USER_ID + " = ? ";

        SQLiteStatement statement = database.compileStatement(updateQuery);

        database.beginTransaction();
        try {
            statement.clearBindings();
            statement.bindLong(1, userFoodMenu.getUserId());
            statement.execute();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        database.close();

        insertMenu();
    }

    public void insertMenu() {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NUMBER, 0);
        cv.put(COLUMN_NAME, userFoodMenu.getName());
        cv.put(COLUMN_DAY_OF_WEEK, 1);
        cv.put(COLUMN_EATING, 1);
        cv.put(COLUMN_WEIGHT, 0);
        cv.put(COLUMN_PRODUCT_NAME, "Новое меню");
        cv.put(COLUMN_PRODUCT_CALORIES, 0);
        cv.put(COLUMN_PRODUCT_PROTEIN, 0);
        cv.put(COLUMN_PRODUCT_FAT, 0);
        cv.put(COLUMN_PRODUCT_CARBOHYDRATE, 0);
        cv.put(COLUMN_USER_ID, userFoodMenu.getUserId());
        database.insert(TABLE_NAME, null, cv);

        database.close();
    }

    public void insertProduct(ValueUserFoodMenuHelper product) {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NUMBER, userFoodMenu.getNumber());
        cv.put(COLUMN_NAME, userFoodMenu.getName());
        cv.put(COLUMN_DAY_OF_WEEK, product.getDayOfWeek());
        cv.put(COLUMN_EATING, product.getEating());
        cv.put(COLUMN_WEIGHT, product.getWeight());
        cv.put(COLUMN_PRODUCT_NAME, product.getProductName());
        cv.put(COLUMN_PRODUCT_CALORIES, product.getProductCalories());
        cv.put(COLUMN_PRODUCT_PROTEIN, product.getProductProtein());
        cv.put(COLUMN_PRODUCT_FAT, product.getProductFat());
        cv.put(COLUMN_PRODUCT_CARBOHYDRATE, product.getProductCarbohyd());
        cv.put(COLUMN_USER_ID, userFoodMenu.getUserId());
        database.insert(TABLE_NAME, null, cv);

        database.close();
    }

    public void deleteRecord(int position) {
        database = dbHelper.getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_NAME + " = ? AND " + COLUMN_USER_ID  + " = ? ",
                new String[] { userFoodMenu.getNameMenus().get(position), String.valueOf(userFoodMenu.getUserId()) });
        database.close();
    }

    public void deleteRecord(ValueUserFoodMenuHelper helper) {
        database = dbHelper.getWritableDatabase();
        database.delete(TABLE_NAME,
                COLUMN_NAME + " = ? AND " +
                COLUMN_DAY_OF_WEEK + " = ? AND " +
                COLUMN_EATING + " = ? AND " +
                COLUMN_WEIGHT + " = ? AND " +
                COLUMN_PRODUCT_NAME + " = ? AND " +
                COLUMN_USER_ID  + " = ? ",
                new String[] {
                        userFoodMenu.getName(),
                        String.valueOf(helper.getDayOfWeek()),
                        String.valueOf(helper.getEating()),
                        String.valueOf(helper.getWeight()),
                        helper.getProductName(),
                        String.valueOf(userFoodMenu.getUserId())
                });
        database.close();
    }

    public void updatePosition(int number) {
        database = dbHelper.getWritableDatabase();

        String updateQuery =
                "UPDATE " +
                        TABLE_NAME +
                        " SET " +
                        COLUMN_NUMBER + " = " + COLUMN_NUMBER + " - 1 " +
                        "WHERE " +
                        COLUMN_USER_ID + " = ? AND " +
                        COLUMN_NUMBER + " >= ? ";

        SQLiteStatement statement = database.compileStatement(updateQuery);

        database.beginTransaction();
        try {
            statement.clearBindings();
            statement.bindLong(1, userFoodMenu.getUserId());
            statement.bindLong(2, number);
            statement.execute();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        database.close();
    }

    public void updateAllPosition() {
        database = dbHelper.getWritableDatabase();

        String updateQuery =
                "UPDATE " +
                        TABLE_NAME +
                        " SET " +
                        COLUMN_NUMBER + " = ? " +
                        "WHERE " +
                        COLUMN_NAME + " = ? AND " +
                        COLUMN_USER_ID + " = ? ";

        SQLiteStatement statement = database.compileStatement(updateQuery);

        database.beginTransaction();
        try {
            for (int i = 0; i < userFoodMenu.getNameMenus().size(); i++) {
                statement.clearBindings();
                statement.bindLong(1, i);
                statement.bindString(2, userFoodMenu.getNameMenus().get(i));
                statement.bindLong(3, userFoodMenu.getUserId());
                statement.execute();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        database.close();
    }

    public void updateNameMenu() {
        database = dbHelper.getWritableDatabase();

        String updateQuery =
                "UPDATE " +
                        TABLE_NAME +
                        " SET " +
                        COLUMN_NAME + " = ? " +
                        "WHERE " +
                        COLUMN_NUMBER + " = ? AND " +
                        COLUMN_USER_ID + " = ? ";

        SQLiteStatement statement = database.compileStatement(updateQuery);

        database.beginTransaction();
        try {
            statement.clearBindings();
            statement.bindString(1, userFoodMenu.getName());
            statement.bindLong(2, userFoodMenu.getNumber());
            statement.bindLong(3, userFoodMenu.getUserId());
            statement.execute();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        database.close();
    }

    public void selectNamesMenu() {
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT DISTINCT " + COLUMN_NAME +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USER_ID + " = ? " +
                " ORDER BY " + COLUMN_NUMBER,
                new String[] { String.valueOf(userFoodMenu.getUserId())} );
        cursor.moveToFirst();

        ArrayList<String> namesMenu = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            namesMenu.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        userFoodMenu.setNameMenus(namesMenu);
    }

}
