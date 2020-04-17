package com.example.health;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TableValueWeight {

    private final String TABLE_NAME = "ValueWeight";
    private final String COLUMN_ID = "id";
    private final String COLUMN_WEIGHT = "weight";
    private final String COLUMN_DAY = "day";
    private final String COLUMN_MONTH = "month";
    private final String COLUMN_YEAR = "year";
    private final String COLUMN_USER_ID = "user_id";

    private int indexId;
    private int indexWeight;
    private int indexDay;
    private int indexMonth;
    private int indexYear;
    private int indexUserId;

    private ValueWeight valueWeight;

    private DBHelperValueWeight dbHelper;
    private SQLiteDatabase database;

    public TableValueWeight(Context context) {

        dbHelper = new DBHelperValueWeight(context);
        valueWeight = ValueWeight.getInstance();

        database = dbHelper.getWritableDatabase();
        createTable();

        Cursor cursor = database.query(TABLE_NAME,null, null, null, null, null, null);

        indexId = cursor.getColumnIndex(COLUMN_ID);
        indexWeight = cursor.getColumnIndex(COLUMN_WEIGHT);
        indexDay = cursor.getColumnIndex(COLUMN_DAY);
        indexMonth = cursor.getColumnIndex(COLUMN_MONTH);
        indexYear = cursor.getColumnIndex(COLUMN_YEAR);
        indexUserId = cursor.getColumnIndex(COLUMN_USER_ID);

        cursor.close();
        database.close();
        selectTable();
    }

    private void createTable() {

        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WEIGHT + " DOUBLE, " +
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

            Log.d("TABLE_WEIGHT", cursor.getInt(indexId) + " " +
                    cursor.getDouble(indexWeight) + " " +
                    cursor.getInt(indexDay) + " " +
                    cursor.getInt(indexMonth) + " " +
                    cursor.getInt(indexYear) + " " +
                    cursor.getInt(indexUserId));
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
    }

    public boolean isRecordExist() {
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
                        String.valueOf(valueWeight.getDay()),
                        String.valueOf(valueWeight.getMonth()),
                        String.valueOf(valueWeight.getYear()),
                        String.valueOf(valueWeight.getUserId())
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

    public void insertFirstRecord(float weight, long userId) {
        Calendar date = Calendar.getInstance();
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_DAY, date.get(Calendar.DAY_OF_MONTH));
        cv.put(COLUMN_MONTH, date.get(Calendar.MONTH) + 1);
        cv.put(COLUMN_YEAR, date.get(Calendar.YEAR));
        cv.put(COLUMN_USER_ID, userId);
        database.insert(TABLE_NAME, null, cv);

        database.close();
    }

    public void insertRecord() {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_WEIGHT, valueWeight.getWeight());
        cv.put(COLUMN_DAY, valueWeight.getDay());
        cv.put(COLUMN_MONTH, valueWeight.getMonth());
        cv.put(COLUMN_YEAR, valueWeight.getYear());
        cv.put(COLUMN_USER_ID, valueWeight.getUserId());
        database.insert(TABLE_NAME, null, cv);

        database.close();
    }

    public void updateRecord() {
        database = dbHelper.getWritableDatabase();

        String updateQuery =
                "UPDATE " +
                        TABLE_NAME +
                        " SET " +
                        COLUMN_WEIGHT + " = ? " +
                        "WHERE " +
                        COLUMN_DAY + " = ? AND " +
                        COLUMN_MONTH + " = ? AND " +
                        COLUMN_YEAR + " = ? AND " +
                        COLUMN_USER_ID + " = ? ";

        SQLiteStatement statement = database.compileStatement(updateQuery);

        database.beginTransaction();
        try {
            statement.clearBindings();

            statement.bindDouble(1, valueWeight.getWeight());
            statement.bindLong(2, valueWeight.getDay());
            statement.bindLong(3, valueWeight.getMonth());
            statement.bindLong(4, valueWeight.getYear());
            statement.bindLong(5, valueWeight.getUserId());

            statement.execute();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        database.close();
    }

    public List<ValueWeightHelper> selectLimit5() {
        List<ValueWeightHelper> listValue = new ArrayList<>();
        ValueWeightHelper value;

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_USER_ID + " = ? " +
                        " ORDER BY " +
                        COLUMN_YEAR + " DESC, " +
                        COLUMN_MONTH + " DESC, " +
                        COLUMN_DAY + " DESC" +
                        " LIMIT 5",
                new String[] {
                        String.valueOf(valueWeight.getUserId())
                });
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueWeightHelper();
            value.setWeight(cursor.getDouble(indexWeight));
            value.setDay(cursor.getInt(indexDay));
            value.setMonth(cursor.getInt(indexMonth));
            value.setYear(cursor.getInt(indexYear));
            listValue.add(value);

            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return listValue;
    }

    public List<ValueWeightHelper> selectFullInfo() {
        List<ValueWeightHelper> listValue = new ArrayList<>();
        ValueWeightHelper value;

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_USER_ID + " = ? " +
                        " ORDER BY " +
                        COLUMN_YEAR + " DESC, " +
                        COLUMN_MONTH + " DESC, " +
                        COLUMN_DAY + " DESC",
                new String[] {
                        String.valueOf(valueWeight.getUserId())
                });
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueWeightHelper();
            value.setWeight(cursor.getDouble(indexWeight));
            value.setDay(cursor.getInt(indexDay));
            value.setMonth(cursor.getInt(indexMonth));
            value.setYear(cursor.getInt(indexYear));
            listValue.add(value);

            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return listValue;
    }

    public void deleteRecord(ValueWeightHelper helper) {
        database = dbHelper.getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_DAY + " = ? AND " +
                        COLUMN_MONTH + " = ? AND " +
                        COLUMN_YEAR + " = ? AND " +
                        COLUMN_USER_ID  + " = ? ",
                new String[] {
                        String.valueOf(helper.getDay()),
                        String.valueOf(helper.getMonth()),
                        String.valueOf(helper.getYear()),
                        String.valueOf(valueWeight.getUserId())
                });
        database.close();
    }

}