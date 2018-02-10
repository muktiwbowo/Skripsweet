package com.muktiwbowo.skripsweet.konsul;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muktiwbowo.skripsweet.R;
import com.muktiwbowo.skripsweet.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import io.apptik.widget.multiselectspinner.MultiSelectSpinner;

public class KonsulActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RequestQueue requestQueue, reqObat, reqGejala, reqPenyakit, reqHabbit;
    Button btnCheck;
    EditText edtNama,edtUsia,edtBB;
    RadioGroup rgGender;
    RadioButton rbGender;
    MultiSelectSpinner spinnerGejala, spinnerObat, spinnerPenyakit, spinnerHabbit;
    ArrayList<String> obat, gejala, penyakit, habbit;
    JSONArray resultObat, resultGejala, resultPenyakit, resultHabbit;
    List<KonsulGejala> listGejala;
    List<KonsulObat> listObat;
    List<KonsulPenyakit> listPenyakit;
    List<KonsulHabbit> listHabbit;
    List<String> idGejalaDipilih, idObatDipilih, idPenyakitDipilih, idHabbitDipilih;
    private ProgressDialog progressDialog;
    String hasil_t, solusi;
    private TextView hasil, rekomendasi;
    SwipeRefreshLayout refreshKonsul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsul);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        reqObat = Volley.newRequestQueue(this);
        reqGejala = Volley.newRequestQueue(this);
        reqPenyakit = Volley.newRequestQueue(this);
        reqHabbit = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);

        obat = new ArrayList<String>();
        gejala = new ArrayList<String>();
        penyakit = new ArrayList<String>();
        habbit = new ArrayList<String>();
        listGejala = new ArrayList<>();
        listObat = new ArrayList<>();
        listPenyakit = new ArrayList<>();
        listHabbit = new ArrayList<>();

        refreshKonsul = (SwipeRefreshLayout) findViewById(R.id.refreshKonsul);
        spinnerGejala = (MultiSelectSpinner) findViewById(R.id.spinerGejala);
        spinnerPenyakit = (MultiSelectSpinner) findViewById(R.id.spinerPenyakit);
        spinnerObat = (MultiSelectSpinner) findViewById(R.id.spinerObat);
        spinnerHabbit = (MultiSelectSpinner) findViewById(R.id.spinerHabbit);
        rgGender = (RadioGroup) findViewById(R.id.rb_gender);
        edtNama = (EditText) findViewById(R.id.edt_name);
        edtUsia = (EditText) findViewById(R.id.edt_age);
        edtBB = (EditText) findViewById(R.id.edt_weight);
        btnCheck = (Button) findViewById(R.id.btn_check);

        refreshKonsul.setOnRefreshListener(this);
        refreshKonsul.post(new Runnable() {
            @Override
            public void run() {
                refreshKonsul.setRefreshing(true);
                getSpinnerGejala();
                getSpinnerPenyakit();
                getSpinnerObat();
                getSpinnerHabbit();
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Url.urlRekomendasi, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);

                            hasil_t = obj.getString("hasil_t");
                            solusi = obj.getString("rekomendasi");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        AlertDialog.Builder alertDialogBuilder =
                                new AlertDialog.Builder(KonsulActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.form_hasil, null);
                        alertDialogBuilder.setView(dialogView);
                        hasil = (TextView) dialogView.findViewById(R.id.hasil);
                        rekomendasi = (TextView) dialogView.findViewById(R.id.solusi);
                        hasil.setText(hasil_t);
                        rekomendasi.setText(solusi);
                        alertDialogBuilder.setTitle("Result");
                        alertDialogBuilder
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //Toast.makeText(KonsulActivity.this, response, Toast.LENGTH_SHORT).show();
                        //Log.d("Response", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(KonsulActivity.this, error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("nama_pasien", edtNama.getText().toString());
                        params.put("usia", edtUsia.getText().toString());
                        int selectedid = rgGender.getCheckedRadioButtonId();
                        rbGender = (RadioButton) findViewById(selectedid);
                        String gender = rbGender.getText().toString();
                        params.put("gender",gender);
                        params.put("berat_badan", edtBB.getText().toString());
                        if (idGejalaDipilih == null) {
                            return null;
                        } else {
                            for (int i = 0; i < idGejalaDipilih.size(); i++) {
                                params.put("id_gejala[" + i + "]", idGejalaDipilih.get(i));
                            }
                        }
                        if (idObatDipilih == null) {
                            return null;
                        } else {
                            for (int j = 0; j < idObatDipilih.size(); j++) {
                                params.put("kode_obat[" + j + "]", idObatDipilih.get(j));
                            }
                        }
                        if (idPenyakitDipilih == null) {
                            return null;
                        } else {
                            for (int k = 0; k < idPenyakitDipilih.size(); k++) {
                                params.put("kode_penyakit[" + k + "]", idPenyakitDipilih.get(k));
                            }
                        }
                        if (idHabbitDipilih == null){
                            return null;
                        }else {
                            for (int l = 0; l<idHabbitDipilih.size(); l++){
                                params.put("id_habbit["+ l + "]", idHabbitDipilih.get(l));
                            }
                        }
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    private void getSpinnerHabbit() {
        refreshKonsul.setRefreshing(true);
        StringRequest requestHabbit = new StringRequest(Request.Method.GET, Url.urlHabbit, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    resultHabbit = object.getJSONArray("habbit");
                    getDataHabbit(resultHabbit);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KonsulActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                refreshKonsul.setRefreshing(false);
            }
        });
        reqHabbit.add(requestHabbit);
    }

    private void getDataHabbit(JSONArray resultHabbit) {
        refreshKonsul.setRefreshing(false);
        for (int i=0; i<resultHabbit.length(); i++){
            try {
                JSONObject obj = resultHabbit.getJSONObject(i);
                habbit.add(obj.getString("jenis_habbit"));
                KonsulHabbit habbit = new KonsulHabbit();
                habbit.setIdHabbit(obj.getString("id_habbit"));
                habbit.setJenisHabbit(obj.getString("jenis_habbit"));
                listHabbit.add(habbit);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerHabbit.setItems(habbit).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

                idHabbitDipilih = new ArrayList<>();
                //StringBuilder builder = new StringBuilder();

                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        //builder.append(gejala.get(i).getIdGejala()).append(" ");
                        idHabbitDipilih.add(listHabbit.get(i).getIdHabbit());
                    }
                }
            }
        })
                .setAllCheckedText("Semua Terpilih")
                .setAllUncheckedText("Pilih Habbit")
                .setSelectAll(false)
                .setTitle("Pilih Habbit");
    }

    private void getSpinnerGejala() {
        refreshKonsul.setRefreshing(true);
        StringRequest requestGejala = new StringRequest(Url.urlGejala, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    resultGejala = object.getJSONArray("gejala");
                    getDataGejala(resultGejala);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KonsulActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                refreshKonsul.setRefreshing(false);
            }
        });
        reqGejala.add(requestGejala);
    }

    private void getDataGejala(JSONArray resultGejala) {
        refreshKonsul.setRefreshing(false);
        for (int i = 0; i < resultGejala.length(); i++) {
            try {
                JSONObject obj = resultGejala.getJSONObject(i);
                gejala.add(obj.getString("nama_gejala"));
                KonsulGejala gejala = new KonsulGejala();
                gejala.setIdGejala(obj.getString("id_gejala"));
                gejala.setNamaGejala(obj.getString("nama_gejala"));
                listGejala.add(gejala);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerGejala.setItems(gejala).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

                idGejalaDipilih = new ArrayList<>();
                //StringBuilder builder = new StringBuilder();

                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        //builder.append(gejala.get(i).getIdGejala()).append(" ");
                        idGejalaDipilih.add(listGejala.get(i).getIdGejala());
                    }
                }
            }
        })
                .setAllCheckedText("Semua Terpilih")
                .setAllUncheckedText("Pilih Gejala")
                .setSelectAll(false)
                .setTitle("Pilih Gejala");
    }

    private void getSpinnerPenyakit() {
        refreshKonsul.setRefreshing(true);
        StringRequest requestPenyakit = new StringRequest(Url.urlPenyakit, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    resultPenyakit = object.getJSONArray("penyakit");
                    getDataPenyakit(resultPenyakit);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KonsulActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                refreshKonsul.setRefreshing(false);
            }
        });
        reqPenyakit.add(requestPenyakit);
    }

    private void getDataPenyakit(JSONArray resultPenyakit) {
        refreshKonsul.setRefreshing(false);
        for (int i = 0; i < resultPenyakit.length(); i++) {
            try {
                JSONObject obj = resultPenyakit.getJSONObject(i);
                penyakit.add(obj.getString("nama_penyakit"));
                KonsulPenyakit p = new KonsulPenyakit();
                p.setIdPenyakit(obj.getString("kode_penyakit"));
                p.setNamaPenyakit(obj.getString("nama_penyakit"));
                listPenyakit.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerPenyakit.setItems(penyakit).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                idPenyakitDipilih = new ArrayList<>();
                //StringBuilder builder = new StringBuilder();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        //builder.append(gejala.get(i).getIdGejala()).append(" ");
                        idPenyakitDipilih.add(listPenyakit.get(i).getIdPenyakit());
                    }
                }
            }
        })
                .setAllCheckedText("Semua Penyakit Terpilih")
                .setAllUncheckedText("Pilih Penyakit")
                .setSelectAll(false)
                .setTitle("Pilih Penyakit");
    }

    public void getSpinnerObat() {
        refreshKonsul.setRefreshing(true);
        StringRequest requestObat = new StringRequest(Url.urlObat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    resultObat = jsonObject.getJSONArray("obat");
                    getDataObat(resultObat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KonsulActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                refreshKonsul.setRefreshing(false);
            }
        });
        reqObat.add(requestObat);
    }

    private void getDataObat(JSONArray resultObat) {
        refreshKonsul.setRefreshing(false);
        for (int i = 0; i < resultObat.length(); i++) {
            try {
                JSONObject obj = resultObat.getJSONObject(i);
                obat.add(obj.getString("nama_obat"));
                KonsulObat obat = new KonsulObat();
                obat.setIdObat(obj.getString("kode_obat"));
                obat.setNamaObat(obj.getString("nama_obat"));
                listObat.add(obat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerObat.setItems(obat).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                idObatDipilih = new ArrayList<>();
                //StringBuilder builder = new StringBuilder();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        //builder.append(gejala.get(i).getIdGejala()).append(" ");
                        idObatDipilih.add(listObat.get(i).getIdObat());
                    }
                }
            }
        })
                .setAllCheckedText("Semua Terpilih")
                .setAllUncheckedText("Pilih Obat")
                .setSelectAll(false)
                .setTitle("Pilih Obat");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRefresh() {
        listPenyakit.clear();
        obat.clear();
        listGejala.clear();
        gejala.clear();
        listObat.clear();
        penyakit.clear();
        listHabbit.clear();
        habbit.clear();
        getSpinnerPenyakit();
        getSpinnerObat();
        getSpinnerGejala();
        getSpinnerHabbit();
    }
}