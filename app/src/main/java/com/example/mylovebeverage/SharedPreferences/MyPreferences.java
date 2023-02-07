package com.example.mylovebeverage.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    private static final String PREFERENCE_NAME = "my_preferences";
    private static Boolean CheckSignedIn = false;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String POSITION = "position";

    private SharedPreferences sharedPreferences;

    public MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void savePosition(String position) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(POSITION, position);
        editor.apply();
    }

    public void saveUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }

    public void savePassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(USERNAME, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    public String getPosition() {
        return sharedPreferences.getString(POSITION, "");
    }

    public void saveKeyCheck(Boolean Check) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        CheckSignedIn = Check;
        editor.putBoolean("CheckSignedIn", CheckSignedIn);
        editor.apply();
    }
    public Boolean getKeyCheck() {
        return sharedPreferences.getBoolean("CheckSignedIn", CheckSignedIn);
    }
}
