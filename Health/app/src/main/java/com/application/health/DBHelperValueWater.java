package com.application.health;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperValueWater extends SQLiteOpenHelper {
    private static String DB_NAME = "Health.db";
    private static int DB_VERSION = 1;
    private final String TABLE_NAME = "ValueWater";
    private final String COLUMN_ID = "id";
    private final String COLUMN_VALUE = "value";
    private final String COLUMN_GOAL_VALUE = "goal_value";
    private final String COLUMN_DAY = "day";
    private final String COLUMN_MONTH = "month";
    private final String COLUMN_YEAR = "year";
    private final String COLUMN_USER_ID = "user_id";

    private final Context context;

    public DBHelperValueWater(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) { }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
