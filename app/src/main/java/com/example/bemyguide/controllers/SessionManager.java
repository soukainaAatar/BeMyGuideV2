package com.example.bemyguide.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.bemyguide.models.User;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    public SharedPreferences getPref() {
        return pref;
    }

    public Editor getEditor() {
        return editor;
    }

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "log_user";


    // User name (make variable public to access from outside)
    public static final String KEY_ID = "id";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(User user) {

        // Storing name in pref
        editor.putInt(KEY_ID, user.getId());

        // Storing email in pref
        editor.putString(KEY_EMAIL, user.getEmail());

        // commit changes
        editor.commit();
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    public int getUserId() {
        // Clearing all data from Shared Preferences
       return pref.getInt(KEY_ID,0);

    }

    public String getUserEmail() {
        // Clearing all data from Shared Preferences
        return pref.getString(KEY_EMAIL,"");

    }
}

