package com.example.health;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
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
    private final String COLUMN_USER_PICTURE = "userPicture";

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
    private int indexUserPicture;

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
        indexUserPicture = cursor.getColumnIndex(COLUMN_USER_PICTURE);

        cursor.close();
        database.close();
    }

    public void signIn() {

        //Считываем данные в userProfile
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                    " WHERE " +
                    COLUMN_LOGIN + " = ? AND " +
                    COLUMN_PASSWORD + " = ? ", new String[] {userProfile.getLogin(), userProfile.getPassword()});
        cursor.moveToFirst();

        userProfile.setId(cursor.getInt(indexId));
        userProfile.setFirstName(cursor.getString(indexFirstName));
        userProfile.setLastName(cursor.getString(indexLastName));
        userProfile.setHeight(cursor.getFloat(indexHeight));
        userProfile.setWeight(cursor.getFloat(indexWeight));
        userProfile.setGender(cursor.getString(indexGender));
        userProfile.setDateOfBirth(cursor.getString(indexDateOfBirth));

        Bitmap userPicture = BitmapUtility.getImage(cursor.getBlob(indexUserPicture));
        userProfile.setUserPicture(userPicture);

        int age = userProfile.ageCalculation(userProfile.getDateOfBirth());
        userProfile.setAge(age);

        //Запрос на обновление
        //Обновляем колонку "Запонмить"
        String updateQuery = "UPDATE " + TABLE_NAME +
                " SET " + COLUMN_REMEMBER + " = ? " +
                " WHERE " + COLUMN_LOGIN + " = ? ";

        SQLiteStatement statement = database.compileStatement(updateQuery);

        database.beginTransaction();
        try {
            statement.clearBindings();

            statement.bindLong(1, userProfile.getRemember());
            statement.bindString(2, userProfile.getLogin());

            statement.execute();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        cursor.close();
        database.close();
    }

    public void signUp() {

        database = dbHelper.getWritableDatabase();

        ContentValues cv = new  ContentValues();
        cv.put(COLUMN_LOGIN, userProfile.getLogin());
        cv.put(COLUMN_PASSWORD, userProfile.getPassword());
        cv.put(COLUMN_FIRST_NAME, userProfile.getFirstName());
        cv.put(COLUMN_LAST_NAME, userProfile.getLastName());
        cv.put(COLUMN_HEIGHT, userProfile.getHeight());
        cv.put(COLUMN_WEIGHT, userProfile.getWeight());
        cv.put(COLUMN_DATE_OF_BIRTH, userProfile.getDateOfBirth());
        cv.put(COLUMN_GENDER, userProfile.getGender());
        cv.put(COLUMN_REMEMBER, userProfile.getRemember());
        byte[] userPicture = BitmapUtility.getBytes(userProfile.getUserPicture());
        cv.put(COLUMN_USER_PICTURE, userPicture);

        database.insert(TABLE_NAME, null, cv );

        //Запрашиваем id добавившейся строки
        Cursor cursor = database.rawQuery(
                "SELECT " + COLUMN_ID +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_LOGIN + " = ? ", new String[] { userProfile.getLogin() });
        cursor.moveToFirst();
        userProfile.setId(cursor.getInt(0));

        cursor.close();
        database.close();
    }

    public void logOut() {
        database = dbHelper.getWritableDatabase();

        String updateQuery = "UPDATE " + TABLE_NAME +
                " SET " + COLUMN_REMEMBER + " = ? " +
                " WHERE " + COLUMN_LOGIN + " = ? ";

        SQLiteStatement statement = database.compileStatement(updateQuery);

        database.beginTransaction();
        try {
            statement.clearBindings();

            statement.bindLong(1, userProfile.getRemember());
            statement.bindString(2, userProfile.getLogin());

            statement.execute();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        database.close();

    }

    public void selectTable() {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();

        byte[] userPicture;

        while (!cursor.isAfterLast()) {

            userPicture = cursor.getBlob(indexUserPicture);

            Log.d("TABLE_USER", cursor.getInt(indexId) + " " +
                    cursor.getString(indexLogin) + " " +
                    cursor.getString(indexPassword) + " " +
                    cursor.getString(indexFirstName) + " " +
                    cursor.getString(indexLastName) + " " +
                    cursor.getFloat(indexHeight) + " " +
                    cursor.getFloat(indexWeight) + " " +
                    cursor.getString(indexDateOfBirth) + " " +
                    cursor.getString(indexGender) + " " +
                    cursor.getString(indexRemember) + " " +
                    userPicture.length);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

    }

    public boolean isLoginExist(String login) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_LOGIN + " = ? ", new String[] { login } );
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
                        COLUMN_LOGIN + " = ? AND " +
                        COLUMN_PASSWORD + " = ? ", new String[] { login, password });
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
