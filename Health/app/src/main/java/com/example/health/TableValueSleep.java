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

public class TableValueSleep {

    private final String TABLE_NAME = "ValueSleep";
    private final String COLUMN_ID = "id";
    private final String COLUMN_FALLING_ASLEEP = "falling_asleep";
    private final String COLUMN_WAKING_UP = "waking_up";
    private final String COLUMN_DURING_SLEEP = "during_sleep";
    private final String COLUMN_DAY = "day";
    private final String COLUMN_MONTH = "month";
    private final String COLUMN_YEAR = "year";
    private final String COLUMN_USER_ID = "user_id";

    private int indexId;
    private int indexFallingAsleep;
    private int indexWakingUp;
    private int indexDuringSleep;
    private int indexDay;
    private int indexMonth;
    private int indexYear;
    private int indexUserId;

    private ValueSleep valueSleep;

    private DBHelperValueSleep dbHelper;
    private SQLiteDatabase database;

    public TableValueSleep(Context context) {

        dbHelper = new DBHelperValueSleep(context);
        valueSleep = ValueSleep.getInstance();

        database = dbHelper.getWritableDatabase();
        createTable();

        Cursor cursor = database.query(TABLE_NAME,null, null, null, null, null, null);

        indexId = cursor.getColumnIndex(COLUMN_ID);
        indexFallingAsleep = cursor.getColumnIndex(COLUMN_FALLING_ASLEEP);
        indexWakingUp = cursor.getColumnIndex(COLUMN_WAKING_UP);
        indexDuringSleep = cursor.getColumnIndex(COLUMN_DURING_SLEEP);
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
                COLUMN_FALLING_ASLEEP + " INTEGER, " +
                COLUMN_WAKING_UP + " INTEGER, " +
                COLUMN_DURING_SLEEP + " INTEGER, " +
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
                    cursor.getInt(indexFallingAsleep) + " " +
                    cursor.getInt(indexWakingUp) + " " +
                    cursor.getInt(indexDuringSleep) + " " +
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
                        String.valueOf(valueSleep.getDay()),
                        String.valueOf(valueSleep.getMonth()),
                        String.valueOf(valueSleep.getYear()),
                        String.valueOf(valueSleep.getUserId())
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
                        String.valueOf(valueSleep.getDay()),
                        String.valueOf(valueSleep.getMonth()),
                        String.valueOf(valueSleep.getYear()),
                        String.valueOf(valueSleep.getUserId())
                });
        cursor.moveToFirst();

        valueSleep.setFallingAsleep(cursor.getInt(indexFallingAsleep));
        valueSleep.setWakingUp(cursor.getInt(indexWakingUp));
        valueSleep.setDuringSleep(cursor.getInt(indexDuringSleep));

        cursor.close();
        database.close();
    }

    public void insertRecord() {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FALLING_ASLEEP, valueSleep.getFallingAsleep());
        cv.put(COLUMN_WAKING_UP, valueSleep.getWakingUp());
        cv.put(COLUMN_DURING_SLEEP, valueSleep.getDuringSleep());
        cv.put(COLUMN_DAY, valueSleep.getDay());
        cv.put(COLUMN_MONTH, valueSleep.getMonth());
        cv.put(COLUMN_YEAR, valueSleep.getYear());
        cv.put(COLUMN_USER_ID, valueSleep.getUserId());
        database.insert(TABLE_NAME, null, cv);

        database.close();
    }

    public void updateRecord() {
        database = dbHelper.getWritableDatabase();

        String updateQuery =
                "UPDATE " +
                    TABLE_NAME +
                " SET " +
                    COLUMN_FALLING_ASLEEP + " = ?, " +
                    COLUMN_WAKING_UP + " = ?, " +
                    COLUMN_DURING_SLEEP + " = ? " +
                "WHERE " +
                    COLUMN_DAY + " = ? AND " +
                    COLUMN_MONTH + " = ? AND " +
                    COLUMN_YEAR + " = ? AND " +
                    COLUMN_USER_ID + " = ? ";

        SQLiteStatement statement = database.compileStatement(updateQuery);

        database.beginTransaction();
        try {
            statement.clearBindings();

            statement.bindLong(1, valueSleep.getFallingAsleep());
            statement.bindLong(2, valueSleep.getWakingUp());
            statement.bindLong(3, valueSleep.getDuringSleep());
            statement.bindLong(4, valueSleep.getDay());
            statement.bindLong(5, valueSleep.getMonth());
            statement.bindLong(6, valueSleep.getYear());
            statement.bindLong(7, valueSleep.getUserId());

            statement.execute();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        database.close();
    }

    public List<ValueSleepHelper> selectWeekInfo(Calendar start, Calendar end) {
        List<ValueSleepHelper> listValue = new ArrayList<>();
        ValueSleepHelper value;

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
                            String.valueOf(valueSleep.getUserId())
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
                            String.valueOf(valueSleep.getUserId())
                    });
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueSleepHelper();
            value.setFallingAsleep(cursor.getInt(indexFallingAsleep));
            value.setWakingUp(cursor.getInt(indexWakingUp));
            value.setDuringSleep(cursor.getInt(indexDuringSleep));
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

    public List<ValueSleepHelper> selectMonthInfo(Calendar date) {
        List<ValueSleepHelper> listValue = new ArrayList<>();
        ValueSleepHelper value;

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
                        String.valueOf(valueSleep.getUserId())
                });
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueSleepHelper();
            value.setFallingAsleep(cursor.getInt(indexFallingAsleep));
            value.setWakingUp(cursor.getInt(indexWakingUp));
            value.setDuringSleep(cursor.getInt(indexDuringSleep));
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

    public List<ValueSleepHelper> selectYearInfo(Calendar date) {
        List<ValueSleepHelper> listValue = new ArrayList<>();
        ValueSleepHelper value;

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
                        String.valueOf(valueSleep.getUserId())
                });
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            value = new ValueSleepHelper();
            value.setFallingAsleep(cursor.getInt(indexFallingAsleep));
            value.setWakingUp(cursor.getInt(indexWakingUp));
            value.setDuringSleep(cursor.getInt(indexDuringSleep));
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
