package com.example.usesqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

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

    public MyDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    // Method to retrieve all user profiles from the database
    public List<UserProfile> getAllUserProfiles() {
        List<UserProfile> userProfiles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_PROFILES, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract user profile data from the cursor
                @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD));
                // Create a UserProfile object and add it to the list
                UserProfile userProfile = new UserProfile(userId, userName, password);
                userProfiles.add(userProfile);
            } while (cursor.moveToNext());
            cursor.close(); // Close the cursor to release resources
        }
        db.close(); // Close the database connection
        return userProfiles;
    }

    // Method to search for user profiles based on a query string
    public List<UserProfile> searchUserProfiles(String query) {
        List<UserProfile> searchResults = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER_PROFILES +
                " WHERE " + COLUMN_USER_NAME + " LIKE '%" + query + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(0);
                String userName = cursor.getString(1);
                String password = cursor.getString(2);
                UserProfile userProfile = new UserProfile(userId, userName, password);
                searchResults.add(userProfile);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return searchResults;
    }
}
