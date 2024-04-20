package com.example.usesqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<UserProfile> {
    private Context mContext;
    private List<UserProfile> mUserProfiles;

    public UserListAdapter(Context context, List<UserProfile> userProfiles) {
        super(context, 0, userProfiles);
        mContext = context;
        mUserProfiles = userProfiles;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        }

        // Get the UserProfile object located at this position in the list
        UserProfile currentUserProfile = mUserProfiles.get(position);

        // Set the TextViews in the layout with the values from the UserProfile object
        TextView userIdTextView = listItem.findViewById(R.id.textViewUserId);
        userIdTextView.setText("ID: " + currentUserProfile.getUserId());

        TextView userNameTextView = listItem.findViewById(R.id.textViewUsername);
        userNameTextView.setText("Name: " + currentUserProfile.getUserName());

        TextView passwordTextView = listItem.findViewById(R.id.textViewPassword);
        passwordTextView.setText("Password: " + currentUserProfile.getPassword());


        return listItem;
    }
}

