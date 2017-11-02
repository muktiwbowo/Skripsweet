package com.muktiwbowo.skripsweet.kasus;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KasusActivity extends AppCompatActivity {

    private static final String tag = KasusActivity.class.getSimpleName();
    private static final String url = "https://dabudabu.000webhostapp.com/farnotifphp/getkasus.php";
    private List<Kasus> list = new ArrayList<Kasus>();
    private ListView listView;
    private KasusAdapter kasusAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.list);
        kasusAdapter = new KasusAdapter(this,list);
        listView.setAdapter(kasusAdapter);

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
                extras.putString("rekomendasi", k.getRekomendasi());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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
                                k.setRekomendasi(obj.getString("rekomendasi"));
                                list.add(k);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        kasusAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder add = new AlertDialog.Builder(KasusActivity.this);
                add.setMessage(error.getMessage()).setCancelable(true);
                AlertDialog alert = add.create();
                alert.setTitle("Error!!!");
                alert.show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}