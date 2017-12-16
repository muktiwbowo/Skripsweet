package com.muktiwbowo.skripsweet.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by muktiwbowo on 09/10/17.
 */

public class Session {
    private Context context;
    private SharedPreferences sharedPreferences;
    public static final String KEY_USER = "username";

    @SuppressLint("WrongConstant")
    public Session(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("users", Context.MODE_APPEND);
    }

    public void storeData(String data, String role){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data", data);
        editor.putString("role", role);
        editor.putBoolean("isLoggedIn", true);
        editor.commit();
    }
    public void storeUsername(String data, String username){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data", data);
        editor.putString(KEY_USER, username);
        editor.commit();
    }

    public boolean isLoggedIn(){
        boolean isLoggedIn = this.sharedPreferences.getBoolean("isLoggedIn", false);
        return isLoggedIn;
    }

    public String getRole(){
        return this.sharedPreferences.getString("role", null);
    }

    public HashMap<String,String> getUsername(){
        HashMap<String,String> user = new HashMap<>();
        user.put(KEY_USER, sharedPreferences.getString(KEY_USER,null));
        return user;
    }
}
