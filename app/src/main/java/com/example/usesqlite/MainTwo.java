package com.example.usesqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainTwo extends AppCompatActivity {

    private ListView userListView;
    private SearchView searchView;
    private UserListAdapter adapter;
    private MyDataHelper myDatabase;
    private Button btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_two);

        searchView = findViewById(R.id.searchView);
        userListView = findViewById(R.id.userListView);
        btnback = findViewById(R.id.btnback);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainTwo.this, MainActivity.class);
                startActivity(i);
            }
        });

        // Initialize myDatabase
        myDatabase = new MyDataHelper(this);

        // Set up the search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Retrieve and display search results based on the query
                displaySearchResults(newText);
                return true;
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserProfile selectedProfile = (UserProfile) adapter.getItem(position);

                Intent i = new Intent(MainTwo.this, MainActivity.class);
                i.putExtra("userId", selectedProfile.getUserId());
                i.putExtra("userName", selectedProfile.getUserName());
                i.putExtra("password", selectedProfile.getPassword());

                startActivity(i);
            }
        });
        // Display the initial user list
        displayUserList();
    }

    private void displayUserList() {
        // Retrieve user profiles from the database and display them
        List<UserProfile> userProfiles = myDatabase.getAllUserProfiles();
        adapter = new UserListAdapter(MainTwo.this, userProfiles);
        userListView.setAdapter(adapter);
    }

    private void displaySearchResults(String query) {
        // Retrieve search results from the database and display them
        List<UserProfile> searchResults = myDatabase.searchUserProfiles(query);
        adapter = new UserListAdapter(MainTwo.this, searchResults);
        userListView.setAdapter(adapter);
    }
}

