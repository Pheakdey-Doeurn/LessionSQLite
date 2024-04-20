package com.example.usesqlite;

import static com.example.usesqlite.MyDataHelper.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

        editTextUserId = findViewById(R.id.edituserid);
        editTextUsername = findViewById(R.id.editusername);
        editTextPassword = findViewById(R.id.edituserpassword);
        userListView = findViewById(R.id.userListView);

        myDatabase = new MyDataHelper(this);


//  Save Data
        btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
//  Search Data
        btnSearch=findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUserProfile();
            }
        });
//  Edit Data
        btnEdit=findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserProfile();
            }
        });
//  Delete Data
        btnDelete=findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserProfile();
            }
        });
        // User List
        btnListUser=findViewById(R.id.btnListUser);
        btnListUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUserList();
            }
        });


    }


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
    private void displayUserList() {
        // Retrieve user profiles from the database
        List<UserProfile> userProfiles = myDatabase.getAllUserProfiles();

        // Display user profiles using a suitable method (e.g., dialog, new activity, RecyclerView)
        // For demonstration, let's display the user profiles in a Toast
//        StringBuilder userListMessage = new StringBuilder("User List:\n");
//        for (UserProfile userProfile : userProfiles) {
//            userListMessage.append("ID: ").append(userProfile.getUserId())
//                    .append(", Name: ").append(userProfile.getUserName())
//                    .append(", Password: ").append(userProfile.getPassword())
//                    .append("\n\n");
//        }
//        Toast.makeText(MainActivity.this, userListMessage.toString(), Toast.LENGTH_LONG).show();


        UserListAdapter adapter = new UserListAdapter(MainActivity.this, userProfiles);
        // Set the adapter to the ListView
        userListView.setAdapter(adapter);
    }
    @Override
    protected void onDestroy() {
        myDatabase.close();
        super.onDestroy();
    }
}