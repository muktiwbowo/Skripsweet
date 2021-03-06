package com.muktiwbowo.skripsweet.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.kasus.KasusActivity;
import com.muktiwbowo.skripsweet.konsul.KonsulActivity;
import com.muktiwbowo.skripsweet.login.LoginActivity;
import com.muktiwbowo.skripsweet.login.Session;
import com.muktiwbowo.skripsweet.obat.ObatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ImageView ivObat, ivKonsul, ivKasus, ivAbout;
    private TextView name;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ivObat = (ImageView) findViewById(R.id.img2);
        ivKonsul = (ImageView) findViewById(R.id.img1);
        ivKasus = (ImageView) findViewById(R.id.img);
        ivAbout = (ImageView) findViewById(R.id.img3);
        name = (TextView) findViewById(R.id.name);

        session = new Session(this);

        HashMap<String, String> user = session.getUsername();
        String nameUser = user.get(Session.KEY_USER);
        name.setText(nameUser);

        ivAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
        ivKasus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KasusActivity.class));
            }
        });
        ivKonsul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KonsulActivity.class));
            }
        });
        ivObat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ObatActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void logOut(){
        @SuppressLint("WrongConstant") SharedPreferences preferences =getSharedPreferences("users", Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(new Intent(MainActivity.this, LoginActivity.class)));
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
