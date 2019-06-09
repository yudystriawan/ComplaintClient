package com.example.complaintclient.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.complaintclient.LoginActivity;
import com.example.complaintclient.MainActivity;

import java.util.HashMap;

public class SessionManager {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
    }

    public void storeToken(String token) {
        editor = preferences.edit();
        editor.putString("TOKEN", token);
        editor.putBoolean("IS_LOGIN", true);
        editor.apply();
    }

    public void createSession(String username, String role, String instance) {
        editor = preferences.edit();
        editor.putString("ROLE", role);
        editor.putString("INSTANCE", instance);
        editor.putString("USERNAME", username);
        editor.apply();
    }

    public HashMap<String, String> getSession() {

        HashMap<String, String> userDetails = new HashMap<>();
        userDetails.put("USERNAME", preferences.getString("USERNAME", null));
        userDetails.put("ROLE", preferences.getString("ROLE", null));
        userDetails.put("INSTANCE", preferences.getString("INSTANCE", null));

        return userDetails;
    }


    public String getToken() {
        return preferences.getString("TOKEN", null);
    }

    public String getUsername() {
        return preferences.getString("USERNAME", null);
    }

    public String getRole() {
        return preferences.getString("ROLE", null);
    }

    private boolean isLogin() {
        return preferences.getBoolean("IS_LOGIN", false);
    }

    public void checkLogin() {
        if (!isLogin()) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }

    public void destroy() {
        editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


}
