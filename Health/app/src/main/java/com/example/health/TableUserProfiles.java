package com.example.health;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class TableUserProfiles {

    private final String TABLE_NAME = "UserProfiles";
    private final String COLUMN_ID = "id";
    private final String COLUMN_LOGIN = "login";
    private final String COLUMN_PASSWORD = "password";
    private final String COLUMN_FIRST_NAME = "first_name";
    private final String COLUMN_LAST_NAME = "last_name";
    private final String COLUMN_HEIGHT = "height";
    private final String COLUMN_WEIGHT = "weight";
    private final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    private final String COLUMN_GENDER = "gender";
    private final String COLUMN_REMEMBER = "remember";


    private int indexId;
    private int indexLogin;
    private int indexPassword;
    private int indexFirstName;
    private int indexLastName;
    private int indexHeight;
    private int indexWeight;
    private int indexDateOfBirth;
    private int indexGender;
    private int indexRemember;

    private UserProfile userProfile;

    private DBHelperUserProfiles dbHelper;
    private SQLiteDatabase database;

    TableUserProfiles(Context context) {
        dbHelper = new DBHelperUserProfiles(context);
        userProfile = UserProfile.getInstance();

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME,null, null, null, null, null, null);

        indexId = cursor.getColumnIndex(COLUMN_ID);
        indexLogin = cursor.getColumnIndex(COLUMN_LOGIN);
        indexPassword = cursor.getColumnIndex(COLUMN_PASSWORD);
        indexFirstName = cursor.getColumnIndex(COLUMN_FIRST_NAME);
        indexLastName = cursor.getColumnIndex(COLUMN_LAST_NAME);
        indexHeight = cursor.getColumnIndex(COLUMN_HEIGHT);
        indexWeight = cursor.getColumnIndex(COLUMN_WEIGHT);
        indexDateOfBirth = cursor.getColumnIndex(COLUMN_DATE_OF_BIRTH);
        indexGender = cursor.getColumnIndex(COLUMN_GENDER);
        indexRemember = cursor.getColumnIndex(COLUMN_REMEMBER);

        cursor.close();
        database.close();
    }

    public void signIn() {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                    " WHERE " +
                    COLUMN_LOGIN + " = \'" +  userProfile.getLogin() + "\'" + " AND " +
                    COLUMN_PASSWORD + " = \'" +  userProfile.getPassword() + "\'", null);
        cursor.moveToFirst();

        userProfile.setId(cursor.getInt(indexId));
        userProfile.setFirstName(cursor.getString(indexFirstName));
        userProfile.setLastName(cursor.getString(indexLastName));
        userProfile.setHeight(cursor.getFloat(indexHeight));
        userProfile.setWeight(cursor.getFloat(indexWeight));
        userProfile.setGender(cursor.getString(indexGender));
        userProfile.setDateOfBirth(cursor.getString(indexDateOfBirth));

        int age = userProfile.ageCalculation(userProfile.getDateOfBirth());
        userProfile.setAge(age);

        database.execSQL("UPDATE " + TABLE_NAME +
                " SET " + COLUMN_REMEMBER + " = " + userProfile.getRemember() +
                " WHERE " + COLUMN_LOGIN + " = \'" + userProfile.getLogin() + "\'");

        database.close();
        cursor.close();
    }

    public void signUp() {

        //Запрос на добавление
        String insertQuery = "INSERT INTO " + TABLE_NAME +
                "(" +
                COLUMN_LOGIN + ", " +
                COLUMN_PASSWORD + ", " +
                COLUMN_FIRST_NAME + ", " +
                COLUMN_LAST_NAME + ", " +
                COLUMN_HEIGHT + ", " +
                COLUMN_WEIGHT + ", " +
                COLUMN_DATE_OF_BIRTH + ", " +
                COLUMN_GENDER + ", " +
                COLUMN_REMEMBER +
                ") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        database = dbHelper.getWritableDatabase();

        SQLiteStatement statement = database.compileStatement(insertQuery);

        //добавляем user'a
        database.beginTransaction();
        try {
            statement.clearBindings();

            statement.bindString(1, userProfile.getLogin());
            statement.bindString(2, userProfile.getPassword());
            statement.bindString(3, userProfile.getFirstName());
            statement.bindString(4, userProfile.getLastName());
            statement.bindDouble(5, userProfile.getHeight());
            statement.bindDouble(6, userProfile.getWeight());
            statement.bindString(7, userProfile.getDateOfBirth());
            statement.bindString(8, userProfile.getGender());
            statement.bindLong(9, userProfile.getRemember());

            statement.execute();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        //Запрашиваем id добавившейся строки
        Cursor cursor = database.rawQuery(
                "SELECT " + COLUMN_ID +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_LOGIN + " = \'" +  userProfile.getLogin() + "\'", null);
        cursor.moveToFirst();
        userProfile.setId(cursor.getInt(0));

        database.close();

    }

    public void selectTable() {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Log.d("TABLE_USER", cursor.getInt(indexId) + " " +
                    cursor.getString(indexLogin) + " " +
                    cursor.getString(indexPassword) + " " +
                    cursor.getString(indexFirstName) + " " +
                    cursor.getString(indexLastName) + " " +
                    cursor.getFloat(indexHeight) + " " +
                    cursor.getFloat(indexWeight) + " " +
                    cursor.getString(indexDateOfBirth) + " " +
                    cursor.getString(indexGender) + " " +
                    cursor.getInt(indexRemember));
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

    }

    public boolean isLoginExist(String login) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_LOGIN + " = \'" +  login + "\'", null);
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

    public boolean isUserProfileExist(String login, String password) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " +
                        COLUMN_LOGIN + " = \'" +  login + "\'" + " AND " +
                        COLUMN_PASSWORD + " = \'" +  password + "\'", null);
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

    public void close() {
        dbHelper.close();
    }

}
