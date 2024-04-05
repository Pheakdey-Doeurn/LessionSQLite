package com.example.usesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDataHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "user_profiles.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    public static final String TABLE_USER_PROFILES = "user_profiles";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_PASSWORD = "user_password";

    // SQL query to create the user_profiles table
    private static final String SQL_CREATE_TABLE_USER_PROFILES =
            "CREATE TABLE " + TABLE_USER_PROFILES + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_USER_PASSWORD + " TEXT)";
    public MyDataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the user_profiles table
        db.execSQL(SQL_CREATE_TABLE_USER_PROFILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILES);
        // Create table again
        onCreate(db);
    }
}
