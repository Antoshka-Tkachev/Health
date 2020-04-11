package com.example.health;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperUserFoodMenu extends SQLiteOpenHelper {

    private static String DB_NAME = "Health.db";
    private static int DB_VERSION = 1;

    private final Context context;

    public DBHelperUserFoodMenu(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
