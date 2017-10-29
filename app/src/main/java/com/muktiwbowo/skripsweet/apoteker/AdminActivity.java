package com.muktiwbowo.skripsweet.apoteker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.LoginActivity;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.kasus.DetailKasusActivity;
import com.muktiwbowo.skripsweet.kasus.Kasus;
import com.muktiwbowo.skripsweet.kasus.KasusActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private static final String url = "http://192.168.58.1/nyoba/getkasus.php";
    private List<Admin> list = new ArrayList<>();
    private ListView listView;
    private AdminAdapter adminAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.list);
        adminAdapter = new AdminAdapter(this,list);
        listView.setAdapter(adminAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Admin a = (Admin) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(AdminActivity.this, UpdateDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("idKasus", a.getKasusId());
                extras.putString("namaKasus", a.getKasusNama());
                extras.putString("namaPasien", a.getPasienNama());
                extras.putString("gender", a.getGendePasien());
                extras.putString("usia", a.getUsiaPasien());
                extras.putString("beratBadan", a.getBadanBerat());
                extras.putString("namaGejala", a.getGejalaNama());
                extras.putString("namaObat", a.getObatNama());
                extras.putString("namaPenyakit", a.getPenyakitNama());
                extras.putString("rekomendasi", a.getSolusi());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Admin a = new Admin();
                        a.setKasusId(obj.getString("kode_kasus"));
                        a.setKasusNama(obj.getString("nama_kasus"));
                        a.setPasienNama(obj.getString("nama_pasien"));
                        a.setUsiaPasien(obj.getString("usia"));
                        a.setGendePasien(obj.getString("gender"));
                        a.setBadanBerat(obj.getString("berat_badan"));
                        a.setGejalaNama(obj.getString("nama_gejala"));
                        a.setObatNama(obj.getString("nama_obat"));
                        a.setPenyakitNama(obj.getString("nama_penyakit"));
                        a.setSolusi(obj.getString("rekomendasi"));
                        list.add(a);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adminAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder add = new AlertDialog.Builder(AdminActivity.this);
                add.setMessage(error.getMessage()).setCancelable(true);
                AlertDialog alert = add.create();
                alert.setTitle("Error!!!");
                alert.show();
            }
        });
        requestQueue.add(jsonArrayRequest);
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
        startActivity(new Intent(new Intent(AdminActivity.this, LoginActivity.class)));
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
