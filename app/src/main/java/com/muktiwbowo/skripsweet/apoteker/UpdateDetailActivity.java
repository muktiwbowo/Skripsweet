package com.muktiwbowo.skripsweet.apoteker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateDetailActivity extends AppCompatActivity {

    private static final String url = "https://dabudabu.000webhostapp.com/farnotifphp/updatekasus.php";
    EditText edtId, edtNamaKasus, edtNamaPasien, edtGender, edtUsia,
            edtBB, edtGejala, edtObat, edtPenyakit, edtSolusi;
    Button btnUpdate;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        edtId = (EditText) findViewById(R.id.idKasus);
        edtNamaKasus = (EditText) findViewById(R.id.namaKasus);
        edtNamaPasien = (EditText) findViewById(R.id.namaPasien);
        edtGender = (EditText) findViewById(R.id.genderPasien);
        edtUsia = (EditText) findViewById(R.id.usiaPasien);
        edtBB = (EditText) findViewById(R.id.beratBadan);
        edtGejala = (EditText) findViewById(R.id.namaGejala);
        edtObat = (EditText) findViewById(R.id.namaObat);
        edtPenyakit = (EditText) findViewById(R.id.namaPenyakit);
        edtSolusi = (EditText) findViewById(R.id.solusi);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        Bundle extras = getIntent().getExtras();
        String idKasus = extras.getString("idKasus");
        String namaKasus = extras.getString("namaKasus");
        String namaPasien = extras.getString("namaPasien");
        String genderPasien = extras.getString("gender");
        String usiaPasien = extras.getString("usia");
        String bbPasien = extras.getString("beratBadan");
        String namaGejala = extras.getString("namaGejala");
        String namaObat = extras.getString("namaObat");
        String namaPenyakit = extras.getString("namaPenyakit");
        String solusi = extras.getString("rekomendasi");

        edtId.setText(idKasus);
        edtId.setVisibility(View.GONE);
            edtNamaKasus.setText(namaKasus);
            edtNamaPasien.setText(namaPasien);
        if (genderPasien.equals("L")){
            edtGender.setText("Laki-laki");
        }else {
            edtGender.setText("Perempuan");
        }

            edtUsia.setText(usiaPasien);
            edtBB.setText(bbPasien);
            edtGejala.setText(namaGejala);
            edtObat.setText(namaObat);
            edtPenyakit.setText(namaPenyakit);
            edtSolusi.setText(solusi);

        
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    Toast.makeText(UpdateDetailActivity.this, "pesan : "+res.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(UpdateDetailActivity.this, AdminActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateDetailActivity.this, "pesan : Gagal update data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kode_kasus", edtId.getText().toString());
                params.put("nama_kasus", edtNamaKasus.getText().toString());
                params.put("nama_pasien", edtNamaPasien.getText().toString());
                params.put("gender", edtGender.getText().toString());
                params.put("usia", edtUsia.getText().toString());
                params.put("berat_badan", edtBB.getText().toString());
                params.put("nama_geja;a", edtGender.getText().toString());
                params.put("nama_obat", edtObat.getText().toString());
                params.put("nama_penyakit", edtPenyakit.getText().toString());
                params.put("rekomendasi", edtSolusi.getText().toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
