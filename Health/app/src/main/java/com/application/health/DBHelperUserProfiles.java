package com.application.health;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperUserProfiles extends SQLiteOpenHelper {
    private static String DB_NAME = "Health.db";
    private static int DB_VERSION = 1;
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


    private final Context context;

    public DBHelperUserProfiles(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=on;");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOGIN + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_HEIGHT + " REAL, " +
                COLUMN_WEIGHT + " REAL, " +
                COLUMN_DATE_OF_BIRTH + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_REMEMBER + " INTEGER, " +
                COLUMN_USER_PICTURE + " BLOB, " +
                "FOREIGN KEY ( " + COLUMN_ID + " )  REFERENCES ValueWater (user_id), " +
                "FOREIGN KEY ( " + COLUMN_ID + " )  REFERENCES ValueSleep (user_id), " +
                "FOREIGN KEY ( " + COLUMN_ID + " )  REFERENCES UserFoodMenu (user_id), " +
                "FOREIGN KEY ( " + COLUMN_ID + " )  REFERENCES NutritionControl (user_id)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
