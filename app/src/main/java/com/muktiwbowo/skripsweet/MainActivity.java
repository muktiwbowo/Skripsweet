package com.muktiwbowo.skripsweet;

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
import android.widget.Button;
import android.widget.ImageView;

import com.muktiwbowo.skripsweet.kasus.KasusActivity;
import com.muktiwbowo.skripsweet.konsul.KonsulActivity;
import com.muktiwbowo.skripsweet.obat.ObatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView ivObat, ivKonsul, ivKasus, ivAbout;

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
