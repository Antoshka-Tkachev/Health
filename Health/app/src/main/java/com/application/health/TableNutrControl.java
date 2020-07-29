package com.application.health;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TableNutrControl {

    private final String TABLE_NAME = "NutritionControl";
    private final String COLUMN_ID = "id";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_CALORIES = "calories";
    private final String COLUMN_PROTEIN = "protein";
    private final String COLUMN_FAT = "fat";
    private final String COLUMN_CARBOHYDRATE = "carbohydrate";
    private final String COLUMN_WEIGHT = "weight";
    private final String COLUMN_DAY = "day";
    private final String COLUMN_MONTH = "month";
    private final String COLUMN_YEAR = "year";
    private final String COLUMN_USER_ID = "user_id";

    private int indexId;
    private int indexName;
    private int indexCalories;
    private int indexProtein;
    private int indexFat;
    private int indexCarbohydrate;
    private int indexWeight;
    private int indexDay;
    private int indexMonth;
    private int indexYear;
    private int indexUserId;

    private UserProfile userProfile;
    private DBHelperNutrControl dbHelper;
    private SQLiteDatabase database;
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");


    public TableNutrControl(Context context) {

        dbHelper = new DBHelperNutrControl(context);
        userProfile = UserProfile.getInstance();
        database = dbHelper.getWritableDatabase();
        createTable();

        Cursor cursor = database.query(TABLE_NAME,null, null, null, null, null, null);
        indexId = cursor.getColumnIndex(COLUMN_ID);
        indexName = cursor.getColumnIndex(COLUMN_NAME);
        indexCalories = cursor.getColumnIndex(COLUMN_CALORIES);
        indexProtein = cursor.getColumnIndex(COLUMN_PROTEIN);
        indexFat = cursor.getColumnIndex(COLUMN_FAT);
        indexCarbohydrate = cursor.getColumnIndex(COLUMN_CARBOHYDRATE);
        indexWeight = cursor.getColumnIndex(COLUMN_WEIGHT);
        indexDay = cursor.getColumnIndex(COLUMN_DAY);
        indexMonth = cursor.getColumnIndex(COLUMN_MONTH);
        indexYear = cursor.getColumnIndex(COLUMN_YEAR);
        indexUserId = cursor.getColumnIndex(COLUMN_USER_ID);

        cursor.close();
        database.close();
    }

    private void createTable() {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CALORIES + " DOUBLE, " +
                COLUMN_PROTEIN + " DOUBLE, " +
                COLUMN_FAT + " DOUBLE, " +
                COLUMN_CARBOHYDRATE + " DOUBLE, " +
                COLUMN_WEIGHT + " INTEGER, " +
                COLUMN_DAY + " INTEGER, " +
                COLUMN_MONTH + " INTEGER, " +
                COLUMN_YEAR + " INTEGER, " +
                COLUMN_USER_ID + " INTEGER );");
    }

    public void selectTable() {
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Log.d("TABLE_WATER", cursor.getInt(indexId) + " " +
                    cursor.getString(indexName) + " " +
                    cursor.getDouble(indexCalories) + " " +
                    cursor.getDouble(indexProtein) + " " +
                    cursor.getDouble(indexFat) + " " +
                    cursor.getDouble(indexCarbohydrate) + " " +
                    cursor.getInt(indexWeight) + " " +
                    cursor.getInt(indexDay) + " " +
                    cursor.getInt(indexMonth) + " " +
                    cursor.getInt(indexYear) + " " +
                    cursor.getInt(indexUserId));
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
    }

    public boolean isExistProduct(String name, String weight, Calendar date) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_DAY + " = ?  AND " +
                        COLUMN_MONTH + " = ?  AND " +
                        COLUMN_YEAR + " = ?  AND " +
                        COLUMN_NAME + " = ?  AND " +
                        COLUMN_WEIGHT + " = ?  AND " +
                        COLUMN_USER_ID + " = ?  ",
                new String[] {
                        String.valueOf(date.get(Calendar.DAY_OF_MONTH)),
                        String.valueOf(date.get(Calendar.MONTH) + 1),
                        String.valueOf(date.get(Calendar.YEAR)),
                        name,
                        weight,
                        String.valueOf(userProfile.getId())
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

    public void insertDay(Calendar date) {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, "Новый день");
        cv.put(COLUMN_CALORIES, 0);
        cv.put(COLUMN_PROTEIN, 0);
        cv.put(COLUMN_FAT, 0);
        cv.put(COLUMN_CARBOHYDRATE, 0);
        cv.put(COLUMN_WEIGHT, 0);
        cv.put(COLUMN_DAY, date.get(Calendar.DAY_OF_MONTH));
        cv.put(COLUMN_MONTH, date.get(Calendar.MONTH) + 1);
        cv.put(COLUMN_YEAR, date.get(Calendar.YEAR));
        cv.put(COLUMN_USER_ID, userProfile.getId());
        database.insert(TABLE_NAME, null, cv);

        database.close();
    }

    public void insertProduct(ValueUserFoodMenuHelper product, Calendar date) {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, product.getProductName());
        cv.put(COLUMN_WEIGHT, product.getWeight());
        cv.put(COLUMN_CALORIES, product.getProductCalories());
        cv.put(COLUMN_PROTEIN, product.getProductProtein());
        cv.put(COLUMN_FAT, product.getProductFat());
        cv.put(COLUMN_CARBOHYDRATE, product.getProductCarbohyd());
        cv.put(COLUMN_DAY, date.get(Calendar.DAY_OF_MONTH));
        cv.put(COLUMN_MONTH, date.get(Calendar.MONTH) + 1);
        cv.put(COLUMN_YEAR, date.get(Calendar.YEAR));
        cv.put(COLUMN_USER_ID, userProfile.getId());
        database.insert(TABLE_NAME, null, cv);

        database.close();
    }

    public ArrayList<ValueUserFoodMenuHelper> selectProducts(Calendar date) {
        ArrayList<ValueUserFoodMenuHelper> dates = new ArrayList<>();
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " +
                        TABLE_NAME +
                        " WHERE " +
                        COLUMN_DAY + " = ? AND " +
                        COLUMN_MONTH + " = ? AND " +
                        COLUMN_YEAR + " = ? AND " +
                        COLUMN_USER_ID + " = ? ",
                new String[] {
                        String.valueOf(date.get(Calendar.DAY_OF_MONTH)),
                        String.valueOf(date.get(Calendar.MONTH) + 1),
                        String.valueOf(date.get(Calendar.YEAR)),
                        String.valueOf(userProfile.getId())
                });

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ValueUserFoodMenuHelper buffer = new ValueUserFoodMenuHelper();
            buffer.setProductName(cursor.getString(indexName));
            buffer.setWeight(cursor.getInt(indexWeight));
            buffer.setProductProtein(cursor.getDouble(indexProtein));
            buffer.setProductFat(cursor.getDouble(indexFat));
            buffer.setProductCarbohyd(cursor.getDouble(indexCarbohydrate));
            buffer.setProductCalories(cursor.getDouble(indexCalories));
            dates.add(buffer);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return dates;
    }

    public ArrayList<String> selectDates() {
        Calendar date = Calendar.getInstance();
        ArrayList<String> dates = new ArrayList<>();
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT DISTINCT " + COLUMN_DAY + ", " + COLUMN_MONTH + ", " + COLUMN_YEAR +
                        " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_USER_ID + " = ?" +
                        " ORDER BY " +
                        COLUMN_YEAR + " DESC, " +
                        COLUMN_MONTH + " DESC, " +
                        COLUMN_DAY + " DESC",
                new String[] { String.valueOf(userProfile.getId()) });

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            date.clear();
            date.set(cursor.getInt(2), cursor.getInt(1) - 1, cursor.getInt(0));
            dates.add(format.format(date.getTime()));
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return dates;
    }

    public void deleteDate(Calendar date) {
        database = dbHelper.getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_DAY + " = ? AND " +
                        COLUMN_MONTH + " = ? AND " +
                        COLUMN_YEAR + " = ? AND " +
                        COLUMN_USER_ID  + " = ? ",
                new String[] {
                        String.valueOf(date.get(Calendar.DAY_OF_MONTH)),
                        String.valueOf(date.get(Calendar.MONTH) + 1),
                        String.valueOf(date.get(Calendar.YEAR)),
                        String.valueOf(userProfile.getId()) });
        database.close();
    }

    public void deleteProduct(ValueUserFoodMenuHelper product, Calendar date) {
        database = dbHelper.getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_DAY + " = ? AND " +
                        COLUMN_MONTH + " = ? AND " +
                        COLUMN_YEAR + " = ? AND " +
                        COLUMN_NAME + " = ? AND " +
                        COLUMN_WEIGHT + " = ? AND " +
                        COLUMN_USER_ID  + " = ? ",
                new String[] {
                        String.valueOf(date.get(Calendar.DAY_OF_MONTH)),
                        String.valueOf(date.get(Calendar.MONTH) + 1),
                        String.valueOf(date.get(Calendar.YEAR)),
                        product.getProductName(),
                        String.valueOf(product.getWeight()),
                        String.valueOf(userProfile.getId()) });
        database.close();
    }


    public List<ValueUserFoodMenuHelper> selectWeekInfo(Calendar start, Calendar end) {
        List<ValueUserFoodMenuHelper> listValue = new ArrayList<>();
        ValueUserFoodMenuHelper value;

        database = dbHelper.getWritableDatabase();

        Cursor cursor;
        if (start.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
            cursor = database.rawQuery(
                    "SELECT * FROM " + TABLE_NAME +
                            " WHERE " +
                            COLUMN_DAY + " >= ? AND " +
                            COLUMN_DAY + " <= ? AND " +
                            COLUMN_MONTH + " = ? AND " +
                            COLUMN_YEAR + " = ?  AND " +
                            COLUMN_USER_ID + " = ? " +
                            "ORDER BY " + COLUMN_MONTH + ", " + COLUMN_DAY,
                    new String[]{
                            String.valueOf(start.get(Calendar.DAY_OF_MONTH)),
                            String.valueOf(end.get(Calendar.DAY_OF_MONTH)),
                            String.valueOf(end.get(Calendar.MONTH) + 1),
                            String.valueOf(end.get(Calendar.YEAR)),
                            String.valueOf(userProfile.getId())
                    });
        } else {
            cursor = database.rawQuery(
                    "SELECT * FROM " + TABLE_NAME +
                            " WHERE (( " +
                            COLUMN_DAY + " >= ? AND " +
                            COLUMN_DAY + " <= ? AND " +
                            COLUMN_MONTH + " = ? AND " +
                            COLUMN_YEAR + " = ? ) OR (" +
                            COLUMN_DAY + " >= ? AND " +
                            COLUMN_DAY + " <= ? AND " +
                            COLUMN_MONTH + " = ? AND " +
                            COLUMN_YEAR + " = ? )) AND " +
                            COLUMN_USER_ID + " = ? " +
                            "ORDER BY " + COLUMN_MONTH + ", " + COLUMN_DAY,
                    new String[]{
                            String.valueOf(start.get(Calendar.DAY_OF_MONTH)),
                            String.valueOf(start.getActualMaximum(Calendar.DAY_OF_MONTH)),
                            String.valueOf(start.get(Calendar.MONTH) + 1),
                            String.valueOf(start.get(Calendar.YEAR)),
                            String.valueOf(1),
                            String.valueOf(end.get(Calendar.DAY_OF_MONTH)),
                            String.valueOf(end.get(Calendar.MONTH) + 1),
                            String.valueOf(end.get(Calendar.YEAR)),
                            String.valueOf(userProfile.getId())
                    });
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueUserFoodMenuHelper();
            value.setWeight(cursor.getInt(indexWeight));
            value.setProductProtein(cursor.getInt(indexProtein));
            value.setProductFat(cursor.getInt(indexFat));
            value.setProductCarbohyd(cursor.getInt(indexCarbohydrate));
            value.setProductCalories(cursor.getInt(indexCalories));
            value.setDayOfMonth(cursor.getInt(indexDay));
            value.setMonth(cursor.getInt(indexMonth));
            value.setYear(cursor.getInt(indexYear));
            listValue.add(value);

            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return listValue;
    }

    public List<ValueUserFoodMenuHelper> selectMonthInfo(Calendar date) {
        List<ValueUserFoodMenuHelper> listValue = new ArrayList<>();
        ValueUserFoodMenuHelper value;

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_MONTH + " = ? AND " +
                        COLUMN_YEAR + " = ? AND " +
                        COLUMN_USER_ID + " = ? " +
                        "ORDER BY " + COLUMN_DAY,
                new String[] {
                        String.valueOf(date.get(Calendar.MONTH) + 1),
                        String.valueOf(date.get(Calendar.YEAR)),
                        String.valueOf(userProfile.getId())
                });
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueUserFoodMenuHelper();
            value.setWeight(cursor.getInt(indexWeight));
            value.setProductProtein(cursor.getInt(indexProtein));
            value.setProductFat(cursor.getInt(indexFat));
            value.setProductCarbohyd(cursor.getInt(indexCarbohydrate));
            value.setProductCalories(cursor.getInt(indexCalories));
            value.setDayOfMonth(cursor.getInt(indexDay));
            value.setMonth(cursor.getInt(indexMonth));
            value.setYear(cursor.getInt(indexYear));
            listValue.add(value);

            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return listValue;
    }

    public List<ValueUserFoodMenuHelper> selectYearInfo(Calendar date) {
        List<ValueUserFoodMenuHelper> listValue = new ArrayList<>();
        ValueUserFoodMenuHelper value;

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_YEAR + " = ? AND " +
                        COLUMN_USER_ID + " = ? " +
                        "ORDER BY " +
                        COLUMN_MONTH + ", " + COLUMN_DAY,
                new String[] {
                        String.valueOf(date.get(Calendar.YEAR)),
                        String.valueOf(userProfile.getId())
                });
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueUserFoodMenuHelper();
            value.setWeight(cursor.getInt(indexWeight));
            value.setProductProtein(cursor.getInt(indexProtein));
            value.setProductFat(cursor.getInt(indexFat));
            value.setProductCarbohyd(cursor.getInt(indexCarbohydrate));
            value.setProductCalories(cursor.getInt(indexCalories));
            value.setDayOfMonth(cursor.getInt(indexDay));
            value.setMonth(cursor.getInt(indexMonth));
            value.setYear(cursor.getInt(indexYear));
            listValue.add(value);

            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return listValue;
    }

}
