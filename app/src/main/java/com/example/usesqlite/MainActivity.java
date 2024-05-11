package com.example.usesqlite;

import static com.example.usesqlite.MyDataHelper.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextUserId, editTextUsername, editTextPassword;
    Button btnSave, btnSearch, btnEdit, btnDelete, btnListUser;
    private MyDataHelper myDatabase;
    private ListView userListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editTextUserId = findViewById(R.id.edituserid);
        editTextUsername = findViewById(R.id.editusername);
        editTextPassword = findViewById(R.id.edituserpassword);

        // Initialize database helper
        myDatabase = new MyDataHelper(this);

        // Set up click listeners for buttons

        // Save Data
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

        // Search Data
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUserProfile();
            }
        });

        // Edit Data
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserProfile();
            }
        });

        // Delete Data
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserProfile();
            }
        });

        // User List
        btnListUser = findViewById(R.id.btnListUser);
        btnListUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MainTwo activity when this button is clicked
                Intent i = new Intent(MainActivity.this, MainTwo.class);
                startActivity(i);
            }
        });

        Intent i = getIntent();
        if (i != null) {
            int userId = i.getIntExtra("userId", -1);
            String userName = i.getStringExtra("userName");
            String password = i.getStringExtra("password");

            // Check if userId is valid (-1 indicates no value was passed)
            if (userId != -1 && userName != null && password != null) {
                // Display the userId, userName, and password
                TextView userIdTextView = findViewById(R.id.edituserid);
                editTextUserId.setText(String.valueOf(userId));

                TextView userNameTextView = findViewById(R.id.editusername);
                editTextUsername.setText(userName);

                TextView passwordTextView = findViewById(R.id.edituserpassword);
                editTextPassword.setText(password);
            }
        }
    }

    // Method to save user profile data to the database
    private void saveUserProfile() {
        SQLiteDatabase db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_NAME, editTextUsername.getText().toString());
        values.put(COLUMN_USER_PASSWORD, editTextPassword.getText().toString());

        long newRowId = db.insert(TABLE_USER_PROFILES, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "User profile saved with ID: " + newRowId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving user profile", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    // Method to search for a user profile by ID in the database
    private void searchUserProfile() {
        SQLiteDatabase db = myDatabase.getReadableDatabase();

        String[] projection = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };

        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {editTextUserId.getText().toString()};

        Cursor cursor = db.query(
                TABLE_USER_PROFILES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex(MyDataHelper.COLUMN_USER_NAME));
            @SuppressLint("Range") String userPassword = cursor.getString(cursor.getColumnIndex(MyDataHelper.COLUMN_USER_PASSWORD));

            editTextUsername.setText(userName);
            editTextPassword.setText(userPassword);
        } else {
            Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show();
            editTextUsername.setText("");
            editTextPassword.setText("");
        }
        cursor.close();
        db.close();
    }

    // Method to edit an existing user profile in the database
    private void editUserProfile() {
        SQLiteDatabase db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDataHelper.COLUMN_USER_NAME, editTextUsername.getText().toString());
        values.put(MyDataHelper.COLUMN_USER_PASSWORD, editTextPassword.getText().toString());

        String selection = MyDataHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {editTextUserId.getText().toString()};

        int count = db.update(
                MyDataHelper.TABLE_USER_PROFILES,
                values,
                selection,
                selectionArgs
        );

        if (count > 0) {
            Toast.makeText(this, "User profile updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating user profile", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    // Method to delete a user profile from the database
    private void deleteUserProfile() {
        SQLiteDatabase db = myDatabase.getWritableDatabase();

        String selection = MyDataHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {editTextUserId.getText().toString()};

        int deletedRows = db.delete(MyDataHelper.TABLE_USER_PROFILES, selection, selectionArgs);

        if (deletedRows > 0) {
            Toast.makeText(this, "User profile deleted", Toast.LENGTH_SHORT).show();
            editTextUserId.setText("");
            editTextUsername.setText("");
            editTextPassword.setText("");
        } else {
            Toast.makeText(this, "Error deleting user profile", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    // Method to close the database connection when the activity is destroyed
    @Override
    protected void onDestroy() {
        myDatabase.close();
        super.onDestroy();
    }
}
