package com.muktiwbowo.skripsweet.konsul;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import io.apptik.widget.multiselectspinner.MultiSelectSpinner;

public class KonsulActivity extends AppCompatActivity {

    RequestQueue requestQueue, reqObat, reqGejala, reqPenyakit;
    Button btnCheck;
    MultiSelectSpinner spinnerGejala, spinnerObat, spinnerPenyakit;
    ArrayList<String> obat, gejala, penyakit;
    JSONArray resultObat, resultGejala, resultPenyakit;
    List<KonsulGejala> listGejala;
    List<KonsulObat> listObat;
    List<KonsulPenyakit> listPenyakit;
    List<String> idGejalaDipilih, idObatDipilih, idPenyakitDipilih;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsul);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        reqObat = Volley.newRequestQueue(this);
        reqGejala = Volley.newRequestQueue(this);
        reqPenyakit = Volley.newRequestQueue(this);

        progressDialog = new ProgressDialog(this);

        obat = new ArrayList<String>();
        gejala = new ArrayList<String>();
        penyakit = new ArrayList<String>();
        listGejala = new ArrayList<>();
        listObat = new ArrayList<>();
        listPenyakit = new ArrayList<>();

        spinnerGejala = (MultiSelectSpinner) findViewById(R.id.spinerGejala);
        spinnerPenyakit = (MultiSelectSpinner) findViewById(R.id.spinerPenyakit);
        spinnerObat = (MultiSelectSpinner) findViewById(R.id.spinerObat);
        btnCheck = (Button) findViewById(R.id.btn_check);

        getSpinnerGejala();
        getSpinnerPenyakit();
        getSpinnerObat();

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

                        AlertDialog.Builder alertDialogBuilder =
                                new AlertDialog.Builder(KonsulActivity.this);
                        alertDialogBuilder.setTitle("Result");
                        alertDialogBuilder.setMessage(response);
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
                        for (int i = 0; i < idGejalaDipilih.size(); i++) {
                            params.put("id_gejala[" + i + "]", idGejalaDipilih.get(i));
                        }
                        for (int j = 0; j < idObatDipilih.size(); j++) {
                            params.put("kode_obat[" + j + "]", idObatDipilih.get(j));
                        }
                        for (int k = 0; k < idPenyakitDipilih.size(); k++) {
                            params.put("kode_penyakit[" + k + "]", idObatDipilih.get(k));
                        }
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    private void getSpinnerGejala() {
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
            }
        });
        reqGejala.add(requestGejala);
    }

    private void getDataGejala(JSONArray resultGejala) {
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
            }
        });
        reqPenyakit.add(requestPenyakit);
    }

    private void getDataPenyakit(JSONArray resultPenyakit) {
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

            }
        });
        reqObat.add(requestObat);
    }

    private void getDataObat(JSONArray resultObat) {
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
}