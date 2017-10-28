package com.muktiwbowo.skripsweet.kasus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.muktiwbowo.skripsweet.R;

public class DetailKasusActivity extends AppCompatActivity {

    TextView namaKasus, namaPasien, gender, usia, beratBadan, namaGejala,
            namaObat, namaPenyakit, rekomendasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kasus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        namaKasus = (TextView) findViewById(R.id.nama_kasus);
        namaPasien = (TextView) findViewById(R.id.nama_pasien);
        gender = (TextView) findViewById(R.id.gender);
        usia = (TextView) findViewById(R.id.usia);
        beratBadan = (TextView) findViewById(R.id.berat_badan);
        namaGejala = (TextView) findViewById(R.id.nama_gejala);
        namaObat = (TextView) findViewById(R.id.nama_obat);
        namaPenyakit = (TextView) findViewById(R.id.nama_penyakit);
        rekomendasi = (TextView) findViewById(R.id.rekomendasi);

        Bundle extras = getIntent().getExtras();
        String NamaKasus = extras.getString("namaKasus");
        String NamaPasien = extras.getString("namaPasien");
        String Gender = extras.getString("gender");
        String Usia = extras.getString("usia");
        String BeratBadan = extras.getString("beratBadan");
        String NamaGejala = extras.getString("namaGejala");
        String NamaObat = extras.getString("namaObat");
        String NamaPenyakit = extras.getString("namaPenyakit");
        String Rekomendasi = extras.getString("rekomendasi");

        namaKasus.setText(NamaKasus);
        namaPasien.setText(NamaPasien);
        if (Gender.equals("L")){
            gender.setText("Laki-laki");
        }else {
            gender.setText("Perempuan");
        }
        usia.setText(Usia);
        beratBadan.setText(BeratBadan);
        namaGejala.setText(NamaGejala);
        namaObat.setText(NamaObat);
        namaPenyakit.setText(NamaPenyakit);
        rekomendasi.setText(Rekomendasi);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
