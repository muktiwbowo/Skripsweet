package com.muktiwbowo.skripsweet;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by muktiwbowo on 09/10/17.
 */

public class Session {
    private Context context;
    private SharedPreferences sharedPreferences;

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

    public boolean isLoggedIn(){
        boolean isLoggedIn = this.sharedPreferences.getBoolean("isLoggedIn", false);
        return isLoggedIn;
    }

    public String getRole(){
        return this.sharedPreferences.getString("role", null);
    }
}
