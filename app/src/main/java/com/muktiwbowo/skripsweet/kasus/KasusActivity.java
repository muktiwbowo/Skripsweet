package com.muktiwbowo.skripsweet.kasus;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KasusActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String tag = KasusActivity.class.getSimpleName();
    private List<Kasus> list = new ArrayList<Kasus>();
    private ListView listView;
    private KasusAdapter kasusAdapter;
    SwipeRefreshLayout refreshKasus;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.list);
        refreshKasus = (SwipeRefreshLayout) findViewById(R.id.refreshKasus);
        kasusAdapter = new KasusAdapter(this,list);
        listView.setAdapter(kasusAdapter);
        refreshKasus.setOnRefreshListener(this);
        refreshKasus.post(new Runnable() {
            @Override
            public void run() {
                refreshKasus.setRefreshing(true);
                getKasus();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Kasus k = (Kasus) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(KasusActivity.this, DetailKasusActivity.class);
                Bundle extras = new Bundle();
                extras.putString("namaKasus", k.getNamaKasus());
                extras.putString("namaPasien", k.getNamaPasien());
                extras.putString("gender", k.getGender());
                extras.putString("usia", k.getUsia());
                extras.putString("beratBadan", k.getBeratBadan());
                extras.putString("namaGejala", k.getNamaGejala());
                extras.putString("namaObat", k.getNamaObat());
                extras.putString("namaPenyakit", k.getNamaPenyakit());
                extras.putString("jenisHabbit",k.getJenisHabbit());
                extras.putString("rekomendasi", k.getRekomendasi());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    private void getKasus(){
        refreshKasus.setRefreshing(true);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url.urlKasus, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Kasus k = new Kasus();
                                    k.setIdKasus(obj.getString("kode_kasus"));
                                    k.setNamaKasus(obj.getString("nama_kasus"));
                                    k.setNamaPenyakit(obj.getString("nama_penyakit"));
                                    k.setNamaPasien(obj.getString("nama_pasien"));
                                    k.setUsia(obj.getString("usia"));
                                    k.setGender(obj.getString("gender"));
                                    k.setBeratBadan(obj.getString("berat_badan"));
                                    k.setNamaGejala(obj.getString("nama_gejala"));
                                    k.setNamaObat(obj.getString("nama_obat"));
                                    k.setJenisHabbit(obj.getString("jenis_habbit"));
                                    k.setRekomendasi(obj.getString("rekomendasi"));
                                    list.add(k);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            kasusAdapter.notifyDataSetChanged();
                        }
                        refreshKasus.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KasusActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                refreshKasus.setRefreshing(false);
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRefresh() {
     list.clear();
     kasusAdapter.notifyDataSetChanged();
     getKasus();
    }
}