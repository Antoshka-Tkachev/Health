package com.example.health;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperNutrControl extends SQLiteOpenHelper {
    private static String DB_NAME = "Health.db";
    private static int DB_VERSION = 1;
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

    private final Context context;

    public DBHelperNutrControl(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) { }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
