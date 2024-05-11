package com.example.usesqlite;

public class UserProfile {
    private Integer userId;
    private String userName;
    private String password;

    public UserProfile(Integer userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserPassword() {
        return password;
    }

    public void setUserId(int userId) {
    }

    public void setuserName(String userName) {
    }

    public void setUserPassword(String Password) {
    }
}

