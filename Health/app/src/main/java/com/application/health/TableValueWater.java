package com.application.health;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TableValueWater {

    private final String TABLE_NAME = "ValueWater";
    private final String COLUMN_ID = "id";
    private final String COLUMN_VALUE = "value";
    private final String COLUMN_GOAL_VALUE = "goal_value";
    private final String COLUMN_DAY = "day";
    private final String COLUMN_MONTH = "month";
    private final String COLUMN_YEAR = "year";
    private final String COLUMN_USER_ID = "user_id";

    private int indexId;
    private int indexValue;
    private int indexGoalValue;
    private int indexDay;
    private int indexMonth;
    private int indexYear;
    private int indexUserId;

    private ValueWater valueWater;

    private DBHelperValueWater dbHelper;
    private SQLiteDatabase database;

    public TableValueWater(Context context) {

        dbHelper = new DBHelperValueWater(context);
        valueWater = ValueWater.getInstance();

        database = dbHelper.getWritableDatabase();
        createTable();

        Cursor cursor = database.query(TABLE_NAME,null, null, null, null, null, null);

        indexId = cursor.getColumnIndex(COLUMN_ID);
        indexValue = cursor.getColumnIndex(COLUMN_VALUE);
        indexGoalValue = cursor.getColumnIndex(COLUMN_GOAL_VALUE);
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
                COLUMN_VALUE + " INTEGER, " +
                COLUMN_GOAL_VALUE + " INTEGER, " +
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
                    cursor.getInt(indexValue) + " " +
                    cursor.getInt(indexGoalValue) + " " +
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
                        String.valueOf(valueWater.getDay()),
                        String.valueOf(valueWater.getMonth()),
                        String.valueOf(valueWater.getYear()),
                        String.valueOf(valueWater.getUserId())
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

    public void selectRecord() {
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_DAY + " = ? AND " +
                        COLUMN_MONTH + " = ? AND " +
                        COLUMN_YEAR + " = ? AND " +
                        COLUMN_USER_ID + " = ? ",
                new String[] {
                        String.valueOf(valueWater.getDay()),
                        String.valueOf(valueWater.getMonth()),
                        String.valueOf(valueWater.getYear()),
                        String.valueOf(valueWater.getUserId())
                });
        cursor.moveToFirst();

        valueWater.setValue(cursor.getInt(indexValue));
        valueWater.setGoalValue(cursor.getInt(indexGoalValue));

        cursor.close();
        database.close();
    }

    public void selectValue() {
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_DAY + " = ? AND " +
                        COLUMN_MONTH + " = ? AND " +
                        COLUMN_YEAR + " = ? AND " +
                        COLUMN_USER_ID + " = ? ",
                new String[] {
                        String.valueOf(valueWater.getDay()),
                        String.valueOf(valueWater.getMonth()),
                        String.valueOf(valueWater.getYear()),
                        String.valueOf(valueWater.getUserId())
                });
        cursor.moveToFirst();

        valueWater.setValue(cursor.getInt(indexValue));

        cursor.close();
        database.close();
    }

    public void insertRecord() {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_VALUE, valueWater.getValue());
        cv.put(COLUMN_GOAL_VALUE, valueWater.getGoalValue());
        cv.put(COLUMN_DAY, valueWater.getDay());
        cv.put(COLUMN_MONTH, valueWater.getMonth());
        cv.put(COLUMN_YEAR, valueWater.getYear());
        cv.put(COLUMN_USER_ID, valueWater.getUserId());
        database.insert(TABLE_NAME, null, cv);

        database.close();
    }

    public void updateRecord() {
        database = dbHelper.getWritableDatabase();

        String updateQuery =
                "UPDATE " +
                    TABLE_NAME +
                " SET " +
                    COLUMN_VALUE + " = ?, " +
                    COLUMN_GOAL_VALUE + " = ? " +
                "WHERE " +
                    COLUMN_DAY + " = ? AND " +
                    COLUMN_MONTH + " = ? AND " +
                    COLUMN_YEAR + " = ? AND " +
                    COLUMN_USER_ID + " = ? ";

        SQLiteStatement statement = database.compileStatement(updateQuery);

        database.beginTransaction();
        try {
            statement.clearBindings();

            statement.bindLong(1, valueWater.getValue());
            statement.bindLong(2, valueWater.getGoalValue());
            statement.bindLong(3, valueWater.getDay());
            statement.bindLong(4, valueWater.getMonth());
            statement.bindLong(5, valueWater.getYear());
            statement.bindLong(6, valueWater.getUserId());

            statement.execute();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        database.close();
    }

    public List<ValueWaterHelper> selectWeekInfo(Calendar start, Calendar end) {
        List<ValueWaterHelper> listValue = new ArrayList<>();
        ValueWaterHelper value;

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
                        String.valueOf(valueWater.getUserId())
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
                        String.valueOf(valueWater.getUserId())
                });
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueWaterHelper();
            value.setValue(cursor.getInt(indexValue));
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

    public List<ValueWaterHelper> selectMonthInfo(Calendar date) {
        List<ValueWaterHelper> listValue = new ArrayList<>();
        ValueWaterHelper value;

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
                        String.valueOf(valueWater.getUserId())
                });
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueWaterHelper();
            value.setValue(cursor.getInt(indexValue));
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

    public List<ValueWaterHelper> selectYearInfo(Calendar date) {
        List<ValueWaterHelper> listValue = new ArrayList<>();
        ValueWaterHelper value;

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
                        String.valueOf(valueWater.getUserId())
                });
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueWaterHelper();
            value.setValue(cursor.getInt(indexValue));
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

    public void close() {
        dbHelper.close();
    }

}
